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
    private int _x, _y;
    private byte[][][] _blocks;
    private FastMap<Integer, Player> _players;
    private boolean _disabled = false;
    protected static final Logger _log = Logger.getLogger(Chunk.class.getName());

    public Chunk(int x, int y) {
	_blocks = new byte[4][4][];
	_players = new FastMap<Integer, Player>().shared();

	_x = (x >> 6) << 6;
	_y = (y >> 6) << 6;
	load();
	_log.info("loaded chunk x:" + _x + " y:" + _y);
    }

    private void load() {
	int startx = _x >> 4, starty = _y >> 4;
	for (int x = startx; x < startx + 4; x++)
	    for (int y = starty; y < starty + 4; y++) {
		byte[] blocks = getChunkBlocks(x, y);

		if (_disabled)
		    return;

		_blocks[x-startx][y-starty] = blocks;
	    }
    }

    public int getX() {
	return _x;
    }

    public int getY() {
	return _y;
    }

    public int getChunkX() {
	return (_x >> 6);
    }

    public int getChunkY() {
	return (_y >> 6);
    }

    public synchronized Block getBlock(int x, int y, int z) {
	int indexx = x - getX(), indexy = y - getY();
	if (z > 127 || z < 0 || indexx < 0 || indexx > 63 || indexy < 0 || indexy > 63)
	    return null;
	else
	    return getChunkBlock(indexx, indexy, z);

    }

    private Block getChunkBlock(int x, int y, int z) {
	int index = z + 128*(y - ((y << 4) >> 4)) + 128*16*(x - ((x << 4) >> 4));
	byte type,data,light,skyLight;
	int chunkx = (x >> 4) - (_x << 2);
	int chunky = (y >> 4) - (_y << 2);
	type = _blocks[chunkx][chunky][index];
	if(isEven(index)){
	    data = (byte) ((_blocks[chunkx][chunky][index + 32768] & 0xF0) >> 4);
	    light = (byte) ((_blocks[chunkx][chunky][index + 49152] & 0xF0) >> 4);
	    skyLight = (byte) ((_blocks[chunkx][chunky][index + 65536] & 0xF0) >> 4);
	}else{
	    data = (byte) (_blocks[chunkx][chunky][index + 32768] & 0x0F);
	    light = (byte) (_blocks[chunkx][chunky][index + 49152] & 0x0F);
	    skyLight = (byte) (_blocks[chunkx][chunky][index + 65536] & 0x0F);
	}
	return new Block(x,y,z,type,data,light,skyLight);
    }

    private byte[] getChunkBlocks(int x, int y) {

	byte[] rt = new byte[81920];

	Tag chunk = Nbt.getChunk(x << 4, y << 4, false);
	if (chunk == null) {
	    _disabled = true;
	    return rt;
	}
	Tag level = chunk.findTagByName("Level");

	byte[] blocks = (byte[]) level.findTagByName("Blocks").getValue();
	byte[] blocksData = (byte[]) level.findTagByName("Data").getValue();
	byte[] blocksLight = (byte[]) level.findTagByName("BlockLight").getValue();
	byte[] blocksSkyLight = (byte[]) level.findTagByName("SkyLight").getValue();

	for (int i = 0; i < 32768; i++) {
	    rt[i] = blocks[i];

	    // even number
	    if (isEven(i)) {
		rt[32768 + floorAndDivideBy2(i)] = blocksData[floorAndDivideBy2(i)];
		rt[49152 + floorAndDivideBy2(i)] = blocksLight[floorAndDivideBy2(i)];
		rt[65536 + floorAndDivideBy2(i)] = blocksSkyLight[floorAndDivideBy2(i)];
	    } 

	}
	chunk = null;
	level = null;
	blocks = null;
	blocksData = null;
	blocksLight = null;
	blocksSkyLight = null;
	return rt;
    }

    public synchronized void registerPlayer(Player player) {
	if (_players.containsKey(player.getId()) || _disabled) {
	    _log.info("Player already in this chunk ! x:" + _x + " y:" + _y);
	    return;
	}

	_players.put(player.getId(), player);

	int startx = _x >> 4, starty = _y >> 4;
	byte[] blocks, blocksO;
	Deflater deflater;
	for (int chunkx = startx; chunkx < startx + 4; chunkx++)
	    for (int chunky = starty; chunky < starty + 4; chunky++) {

		player.sendPacket(new PreChunk(chunkx, chunky, true));

		blocks = _blocks[chunkx-startx][chunky-starty];
		deflater = new Deflater(1);

		deflater.setInput(blocks);
		deflater.finish();
		blocksO = new byte[81695];
		int size = deflater.deflate(blocksO);
		player.sendPacket(new SendChunk((chunkx << 4), (chunky << 4), 0, 15, 15, 127, size, blocksO));

		deflater.end();
	    }
	deflater = null;
	blocks = null;
    }

    public synchronized void forget(Player player) {

	if (!_players.containsKey(player.getId()))
	    return;

	int startx = _x << 2, starty = _y << 2;

	for (int x = startx; x < startx + 4; x++)
	    for (int y = starty; y < starty + 4; y++)
		player.sendPacket(new PreChunk(x, y, false));

	_players.remove(player.getId());
    }
    // why doesn't java have this ?
    private static boolean isEven(int var){
	return ((var >> 1) << 1) == var;
    }
    private static int floorAndDivideBy2(int var){
	return (var >> 1);
    }
}
