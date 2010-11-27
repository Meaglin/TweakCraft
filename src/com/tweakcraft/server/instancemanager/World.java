package com.tweakcraft.server.instancemanager;

import com.tweakcraft.server.model.Chunk;
import javolution.util.FastMap;

/**
 *
 * @author Meaglin
 */
public class World {

    private FastMap<Integer,FastMap<Integer,Chunk>> _chunks;
    private World() {
	_chunks = new FastMap<Integer,FastMap<Integer,Chunk>>().shared();
    }

    public static World getInstance() {
        return WorldHolder.INSTANCE;
    }

    private static class WorldHolder {
        private static final World INSTANCE = new World();
    }
 }
