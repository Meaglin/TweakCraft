
package com.tweakcraft.server.model;

import com.tweakcraft.server.instance.Player;
import com.tweakcraft.server.nbt.Nbt;
import com.tweakcraft.server.nbt.Tag;
import com.tweakcraft.server.network.sendablePacket.PreChunk;
import com.tweakcraft.server.network.sendablePacket.SendChunk;
import java.util.logging.Logger;
import java.util.zip.Deflater;
import javolution.util.FastMap;

/**
 *
 * @author Meaglin
 */
public class Chunk {
    //bottom left x,y of the chunk.
    private int _x,_y;
    private FastMap<Integer, Block> _blocks;
    private FastMap<Integer, Player> _players;

    protected static final Logger _log = Logger.getLogger(Chunk.class.getName());

    public Chunk(int x,int y){
	//_log.info("loading chunk");
	//_blocks = new Block[64][64][128];
	//_blocks = blocks;
    	_blocks = new FastMap<Integer, Block>().shared();
	_players = new FastMap<Integer, Player>().shared();

	//_log.info("loading chunk 2");
	_x = (x >> 6) << 6;
	_y = (y >> 6) << 6;
	load();
	_log.info("loaded chunk x:" + _x + " y:" + _y);
    }
    private void load(){
	int startx = _x >> 4,starty = _y >> 4;
	for(int x = startx;x < startx+4;x++)
	    for(int y = starty;y < starty+4;y++){
		Block[][][] blocks = getChunkBlocks(x,y);

		for(int i = 0;i < 16;i++)
		    for(int o = 0;o < 16;o++)
			for(int p = 0;p < 128;p++)
			    _blocks.put((((i + 16 * (x - startx)) << 16) + ((o + 16 * (y - starty)) << 8) + p), blocks[i][o][p]);
	    }
    }
    public int getX(){
	return _x;
    }
    public int getY(){
	return _y;
    }
    public synchronized Block getBlock(int x,int y,int z){
	int indexx = x - getX(), indexy = y - getY();
	if(z > 127 || z < 0 || indexx < 0 || indexx > 63 || indexy < 0 || indexy > 63)
	    return null;
	else{
	    int location =  indexx << 16 + indexy << 8 + z;
	    return _blocks.get(location);
	    
	}

    }
    private Block[][][] getChunkBlocks(int x, int y) {

	Block[][][] rt = new Block[16][16][128];

	Tag chunk = Nbt.getChunk(x << 4, y << 4, false);
	Tag level = chunk.findTagByName("Level");

	//if(level.findTagByName("Blocks") == null)
	//    _log.info("wut");
	
	byte[] blocks = (byte[]) level.findTagByName("Blocks").getValue();
	byte[] blocksData = (byte[]) level.findTagByName("Data").getValue();
	byte[] blocksLight = (byte[]) level.findTagByName("BlockLight").getValue();
	byte[] blocksSkyLight = (byte[]) level.findTagByName("SkyLight").getValue();

	int curx = 0, cury = 0, curz = 0;
	byte blockType, blockData, blockLight, blockSkyLight;
	for (int i = 0; i < 32768; i++) {

	    // even number
	    if (Math.floor(i / 2) == Math.round(i / 2)) {

		blockData = (byte) ((blocksData[(int) Math.floor(i / 2)] & 0xF0) >> 4);
		blockLight = (byte) ((blocksLight[(int) Math.floor(i / 2)] & 0xF0) >> 4);
		blockSkyLight = (byte) ((blocksSkyLight[(int) Math.floor(i / 2)] & 0xF0) >> 4);
	    //uneven number
	    } else {
		blockData = (byte) ((blocksData[(int) Math.floor(i / 2)] & 0x0F));
		blockLight = (byte) ((blocksLight[(int) Math.floor(i / 2)] & 0x0F));
		blockSkyLight = (byte) ((blocksSkyLight[(int) Math.floor(i / 2)] & 0x0F));
	    }
	    blockType = (byte) (blocks[i] & 0xFF);
//	    if(blockType != 7 && blockType != 87 && blockType != 0)
//		System.out.println(i + "= x:" + curx + " y:" + cury + " z:" + curz + " t:" + blockType + " d:" + blockData + " l:" + blockLight + " sl:" + blockSkyLight);
	    rt[curx][cury][curz] = new Block((curx + (x << 4)), (cury + (y << 4)), curz, blockType, blockData, blockLight, blockSkyLight);

	    if (cury == 15 && curz == 127) {
		curx++;
		cury = 0;
		curz = 0;
	    }
	    else if (curz == 127) {
		cury++;
		curz = 0;
	    }
	    else
		curz++;
	}

	return rt;
    }
    public synchronized void registerPlayer(Player player){
	if(_players.containsKey(player.getId())){
	    _log.info("Player already in this chunk ! x:" + _x + " y:" + _y);
	    return;
	}
	else _log.info("register player");
	_players.put(player.getId(), player);

	int startx = _x >> 4,starty = _y >> 4;
	byte[] blocks,blocksO;
	Deflater deflater;
	for(int chunkx = startx;chunkx < startx+4;chunkx++)
	    for(int chunky = starty;chunky < starty+4;chunky++){

		player.sendPacket(new PreChunk(chunkx,chunky,true));

		blocks = new byte[81920];
		deflater = new Deflater(1);

		int index = 0;
		for(int x = 0;x < 16; x++)
		    for(int y = 0;y < 16; y++ )
			for(int z = 0;z < 128; z++ ){
			    index = z + y * 128 + x * 128 * 16;
			    //if(index > 32000)
				//System.out.println(index + "= x:" + x + " y:" + y + " z:" + z);
			    int id = (((x + 16 * (chunkx-startx)) << 16) + ((y + 16 * (chunky-starty)) << 8) + z);
			    Block block  = _blocks.get(id);
			    //System.out.println(index + "= x:" + i + " y:" + o + " z:" + p);
			    blocks[index] = (byte)block.getType();
//			    blocks[index] = 1;
			    if (Math.floor(index / 2) == Math.round(index / 2)) {
				blocks[(int)Math.floor(index/2) + 32678] |= (byte)(block.getData() << 4);
				blocks[(int)Math.floor(index/2) + 49152] |= (byte)(block.getLight() << 4);
				blocks[(int)Math.floor(index/2) + 65536] |= (byte)(block.getSkyLight() << 4);
			    }else{
				blocks[(int)Math.floor(index/2) + 32678] |= (byte)block.getData();
				blocks[(int)Math.floor(index/2) + 49152] |= (byte)block.getLight();
				blocks[(int)Math.floor(index/2) + 65536] |= (byte)block.getSkyLight();
			    }
			    //System.out.println(index + "["+(chunkx-startx)+"]["+(chunky-starty)+"]["+((x + 16 * (chunkx-startx)) << 16)+"]= x:" + x + " y:" + y + " z:" + z + " t:" + block.getType() + " d:" + block.getData() + " l:" + block.getLight() + " sl:" + block.getSkyLight());
			}

		deflater.setInput(blocks);
		deflater.finish();
		blocksO = new byte[81695];
		int size = deflater.deflate(blocksO);
		player.sendPacket(new SendChunk((chunkx << 4),(chunky << 4),0,15,15,127,size,blocksO));
		//System.out.println("sent chunk " + chunkx + " " + chunky + " " + size);

		deflater.end();
	    }
    }
    private void sendChunkData(int chunkx,int chunky,Player player){

    }
    public synchronized void forget(Player player){

	if(!_players.containsKey(player.getId()))
	    return;

	int startx = _x << 2,starty = _y << 2;

	for(int x = startx;x < startx+4;x++)
	    for(int y = starty;y < starty+4;y++){
		player.sendPacket(new PreChunk(x,y,false));
	    }

	_players.remove(player.getId());
    }
}
