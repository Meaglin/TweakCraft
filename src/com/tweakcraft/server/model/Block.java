package com.tweakcraft.server.model;

/**
 *
 * @author Meaglin
 */
public class Block {

    private int _x, _y, _z;
    private byte _type, _data, _light, _skyLight;

    public Block(int x, int y, int z, int type, int data, int light, int skylight) {
	_x = x;
	_y = y;
	_z = z;
	_type = (byte) type;
	_data = (byte) data;
	_light = (byte) light;
	_skyLight = (byte) skylight;
    }

    public int getX() {
	return _x;
    }

    public int getY() {
	return _y;
    }

    public int getZ() {
	return _z;
    }

    public synchronized byte getType() {
	return _type;
    }

    public synchronized byte getData() {
	return _data;
    }

    public synchronized byte getLight() {
	return _light;
    }

    public synchronized byte getSkyLight() {
	return _skyLight;
    }

    public void setX(int x) {
	_x = x;
    }

    public void setY(int y) {
	_y = y;
    }

    public void setZ(int z) {
	_z = z;
    }

    public synchronized void setType(byte type) {
	_type = type;
    }

    public synchronized void setData(byte data) {
	_data = data;
    }

    public synchronized void setLight(byte light) {
	_light = light;
    }

    public synchronized void setSkyLight(byte skylight) {
	_skyLight = skylight;
    }
}
