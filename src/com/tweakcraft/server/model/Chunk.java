package com.tweakcraft.server.model;

import com.tweakcraft.server.ThreadPoolManager;
import com.tweakcraft.server.instance.Player;
import com.tweakcraft.server.instancemanager.World;
import com.tweakcraft.server.nbt.Nbt;
import com.tweakcraft.server.nbt.Tag;
import com.tweakcraft.server.network.BaseSendablePacket;
import com.tweakcraft.server.network.sendablePacket.PreChunk;
import com.tweakcraft.server.network.sendablePacket.SendCharInfo;
import com.tweakcraft.server.network.sendablePacket.SendChunk;
import com.tweakcraft.server.util.Rand;
import java.util.Arrays;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import javolution.util.FastMap;

/**
 *
 * @author Meaglin
 */
public class Chunk {
    //bottom left x,y of the chunk.
    private int _x, _y;
    private final byte[][][] _blocks;
    private final boolean[][] _compressed;
    private final int[][] _lastAction;
    private final ScheduledFuture<?>[][] _cleanupTasks;

    private final FastMap<Integer, Player> _players;
    private boolean _disabled = false;
    protected static final Logger _log = Logger.getLogger(Chunk.class.getName());

    public Chunk(int x, int y) {

	_blocks = new byte[4][4][];
	_compressed = new boolean[4][4];
	_lastAction = new int[4][4];
	_cleanupTasks = new ScheduledFuture<?>[4][4];

	_players = new FastMap<Integer, Player>().shared();

	_x = (x >> 6) << 6;
	_y = (y >> 6) << 6;
	load();
	//_log.info("loaded chunk x:" + _x + " y:" + _y);
    }

    private void load() {
	int startx = _x >> 4, starty = _y >> 4;
	for (int x = startx; x < startx + 4; x++)
	    for (int y = starty; y < starty + 4; y++) {
		byte[] blocks = getChunkBlocks(x, y);

		if (_disabled)
		    return;

		_blocks[x-startx][y-starty] = blocks;
		compressSubChunk(x-startx,y-starty);
	    }
	System.gc();
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

    public Block getBlock(int x, int y, int z) {
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
	synchronized (_blocks){
	    if(_compressed[chunkx][chunky])
		deCompressSubChunk(chunkx,chunky);

	    _lastAction[chunkx][chunky] = currentTimeInSeconds();
	    
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
	    if(i < 16384){
		rt[32768 + i] = blocksData[i];
		rt[49152 + i] = blocksLight[i];
		rt[65536 + i] = blocksSkyLight[i];
	    } 
	}
	chunk = null;
	level = null;
	blocks = null;
	blocksData = null;
	blocksLight = null;
	blocksSkyLight = null;
	try{
	    return rt;
	} finally {
	    rt = null;
	}
    }

    private synchronized void compressSubChunk(int x,int y){
	if(x > 3 || y > 3 || x < 0 || y < 0)
	    return;

	//already compressed block.
	if(_compressed[x][y])
	    return;

	_compressed[x][y] = true;

	Deflater deflater = new Deflater(1);
	deflater.setInput(_blocks[x][y]);
	deflater.finish();
	byte[] blocks = new byte[81695];
	int size = deflater.deflate(blocks);
	_blocks[x][y] = new byte[size];
	_blocks[x][y] = Arrays.copyOf(blocks, size);
	 //_log.info("Compressed ("+x+","+y+") size:"+size+".");
	blocks = null;
	deflater = null;
	
    }
    private synchronized void deCompressSubChunk(int x,int y){
	if(x > 3 || y > 3 || x < 0 || y < 0)
	    return;

	//already decompressed block.
	if(!_compressed[x][y])
	    return;

	_compressed[x][y] = false;
	byte[] blocks = new byte[81695];
	Inflater inflater = new Inflater();
	inflater.setInput(_blocks[x][y]);
	try {
	    inflater.inflate(blocks);
	} catch (DataFormatException ex) {
	    Logger.getLogger(Chunk.class.getName()).log(Level.SEVERE, null, ex);
	} finally {
	    inflater.end();
	    _blocks[x][y] = blocks;
	    if(_cleanupTasks[x][y] == null)
		_cleanupTasks[x][y] = ThreadPoolManager.getInstance().schedule(new SubChunkCleanupTask(x,y), 200000L);
	    blocks = null;
	    inflater = null;
	}
	
    }
    public synchronized void registerPlayer(Player player) {
	if (_players.containsKey(player.getId()) || _disabled) {
	    _log.info("Player already in this chunk ! x:" + _x + " y:" + _y);
	    return;
	}

	

	int startx = _x >> 4, starty = _y >> 4;
	byte[] blocksO;
	Deflater deflater;
	for (int chunkx = startx; chunkx < startx + 4; chunkx++)
	    for (int chunky = starty; chunky < starty + 4; chunky++) {

		player.sendPacket(new PreChunk(chunkx, chunky, true));

		if(_compressed[chunkx-startx][chunky-starty]){
		    player.sendPacket(new SendChunk((chunkx << 4), (chunky << 4), 0, 15, 15, 127, _blocks[chunkx-startx][chunky-starty].length, _blocks[chunkx-startx][chunky-starty]));
		}else{
		    deflater = new Deflater(1);

		    deflater.setInput(_blocks[chunkx-startx][chunky-starty]);
		    deflater.finish();
		    blocksO = new byte[81695];
		    int size = deflater.deflate(blocksO);
		    player.sendPacket(new SendChunk((chunkx << 4), (chunky << 4), 0, 15, 15, 127, size, blocksO));

		    deflater.end();
		}
	    }
	deflater = null;
	blocksO = null;
	
	for(Player p : _players.values())
	    player.sendPacket(new SendCharInfo(p));

	_players.put(player.getId(), player);
    }

    public synchronized void forget(Player player) {

	if (!_players.containsKey(player.getId()))
	    return;

	int startx = _x << 2, starty = _y << 2;

	//don't try sending anymore packets if the player disconnected.
	if(!player.getClient().getConnection().isClosed())
	    for (int x = startx; x < startx + 4; x++)
		for (int y = starty; y < starty + 4; y++)
		    player.sendPacket(new PreChunk(x, y, false));

	_players.remove(player.getId());
	//no1 in or around the chunk anymore.
	if(_players.isEmpty()){
	    //_log.info("Schedule unload task.");
	    //prevent too big io bursts by making this random.
	    ThreadPoolManager.getInstance().schedule(new ChunkCleanupTask(), Rand.get(250,350) * 1000L);
	}
    }

    public void broadcastPacket(BaseSendablePacket packet, Player player) {
	synchronized(_players){
	    for(Player p : _players.values())
		if(p.getId() != player.getId())
		    p.sendPacket(packet);
	}
    }

    class ChunkCleanupTask implements Runnable{
	public void run() {
	    if(!_players.isEmpty()){
		_log.info("players not empty ?");
		return;
		
	    }

	   for(int x = 0;x < 4;x++)
		for(int y = 0;y < 4;y++)
		    if(_lastAction[x][y] > currentTimeInSeconds()-200) {
			_log.info(currentTimeInSeconds() + " action not empty ? " + _lastAction[x][y]);
			return;
		    }


	    for(int x = 0;x < 4;x++)
		for(int y = 0;y < 4;y++){
		    if(_lastAction[x][y] > 0)
		    {
			//save chunk.
		    }
		    if(_cleanupTasks[x][y] != null)
			_cleanupTasks[x][y].cancel(false);
		    
		    _blocks[x][y] = null;
		}

	   _players.clear();
	   World.getInstance().unloadChunk(_x,_y);
	   _log.info("unloading chunk ("+_x+","+_y+").");
	}
    }
    class SubChunkCleanupTask implements Runnable{
	private final int _x,_y;
	public SubChunkCleanupTask(int x,int y){
	    _x = x;_y = y;
	}
	public void run() {
	    if(_lastAction[_x][_y] < currentTimeInSeconds() - 60)
		compressSubChunk(_x,_y);

	    _cleanupTasks[_x][_y] = null;
	}
    }

    // why doesn't java have this ?
    private static boolean isEven(int var){
	return ((var >> 1) << 1) == var;
    }
    private static int floorAndDivideBy2(int var){
	return (var >> 1);
    }
    private static int currentTimeInSeconds(){
	return (int) ((System.currentTimeMillis() / 500) >> 1);
    }
}
