package com.tweakcraft.server.model;

/**
 *
 * @author Meaglin
 */
public class Block {
    private int _x,_y,_z,_type;

    public Block(int x,int y,int z,int type){
	_x = x;
	_y = y;
	_z = z;
	_type = type;
    }
    public int getX(){
	return _x;
    }
    public int getY(){
	return _y;
    }
    public int getZ(){
	return _z;
    }
    public synchronized int getType(){
	return _type;
    }
    public void setX(int x){
	_x = x;
    }
    public void setY(int y){
	_y = y;
    }
    public void setZ(int z){
	_z = z;
    }
    public synchronized void setType(int type){
	_type = type;
    }
}
