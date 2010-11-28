
package com.tweakcraft.server.model;

import com.tweakcraft.server.nbt.Nbt;
import com.tweakcraft.server.nbt.Tag;
import javolution.util.FastMap;

/**
 *
 * @author Meaglin
 */
public class Chunk {
    //bottom left x,y of the chunk.
    private int _x,_y;
    private FastMap<Integer, Block> _blocks;
    public Chunk(int x,int y){
	//_blocks = new Block[64][64][128];
	//_blocks = blocks;
	_blocks = new FastMap<Integer, Block>().shared();

	_x = (x >> 6) << 6;
	_y = (y >> 6) << 6;
	load();
    }
    private void load(){
	int startx = _x << 2,starty = _y << 2;
	for(int x = startx;x < startx+4;x++)
	    for(int y = starty;y < starty+4;y++){
		Block[][][] blocks = getChunkBlocks(x,y);

		for(int i = 0;i< 64;i++)
		    for(int o = 0;o < 64;i++)
			for(int p = 0;p < 128;p++)
			    _blocks.put(i << 16 + o << 8 + p, blocks[i][o][p]);
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

	Tag chunk = Nbt.getChunk(x, y, false);
	Tag level = chunk.findTagByName("level");

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
	    rt[curx][cury][curz] = new Block(curx + x >> 4, cury + y >> 4, curz, blockType, blockData, blockLight, blockSkyLight);

	    if (curx == 15 && curz == 127) {
		curx = 0;
		cury++;
		curz = 0;
	    }
	    if (curz == 127) {
		curz = 0;
		curx++;
	    }
	    curz++;
	}

	return rt;
    }
}
