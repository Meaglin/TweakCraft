package com.tweakcraft.server.instancemanager;

import com.tweakcraft.server.model.Block;
import com.tweakcraft.server.model.Chunk;
import javolution.util.FastMap;

/**
 *
 * @author Meaglin
 */
public class World {

    private FastMap<Long, Chunk> _chunks;

    private World() {
	_chunks = new FastMap<Long,Chunk>().shared();
    }


    public synchronized Chunk getChunk(int x,int y){
	int chunkx = x >> 6, chunky = y >> 6;
	long location = chunky << 32 + chunkx;

	if(_chunks.containsKey(location))
	    return _chunks.get(location);
	else
	{
	    Chunk c = new Chunk(x,y);
	    _chunks.put(location, c);
	    return c;
	}
    }

    public synchronized Block getBlock(int x,int y, int z){
	return getChunk(x,y).getBlock(x, y, z);
    }

    public static World getInstance() {
	return WorldHolder.INSTANCE;
    }

    private static class WorldHolder {

	private static final World INSTANCE = new World();
    }
}
