package com.tweakcraft.server.instance;

/**
 * Character class, base class for all moving entity's.
 * @author Meaglin
 */
public class Character {
    private double _x,_y,_z;
    public Character(){

    }
    public double getX(){
	return _x;
    }
    public double getY(){
	return _y;
    }
    public double getZ(){
	return _z;
    }
    protected void setX(double x){
	_x = x;
    }
    protected void setY(double y){
	_y = y;
    }
    protected void setZ(double z){
	_z = z;
    }
}
