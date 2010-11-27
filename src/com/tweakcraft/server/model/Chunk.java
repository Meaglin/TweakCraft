
package com.tweakcraft.server.model;

import javolution.util.FastList;

/**
 *
 * @author Meaglin
 */
public class Chunk {
    private int _x,_y;
    private FastList<FastList<FastList<Block>>> _blocks;
    public Chunk(int x,int y,Block[][][] blocks){
	//_blocks = new Block[64][64][128];
	//_blocks = blocks;
	_blocks = new FastList<FastList<FastList<Block>>>().shared();
	_x = x;
	_y = y;
    }
    public int getX(){
	return _x;
    }
    public int getY(){
	return _y;
    }
    public synchronized Block getBlock(int x,int y,int z){
	if(z > 127 || z < 0 || x >= ((getX() + 1) << 6) || x < (getX() << 6) || y >= ((getY() + 1) << 6) || y < (getY() << 6))
	    return null;
	else
	    return _blocks.get(x-getX()).get(y-getY()).get(z);
    }
}
