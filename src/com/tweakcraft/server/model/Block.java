package com.tweakcraft.server.model;

/**
 *
 * @author Meaglin
 */
public class Block {

    private int _x, _y, _z;
    private byte _type, _data, _light, _skyLight;

    public Block(int x, int y, int z, byte type, byte data, byte light, byte skylight) {
	_x = x;
	_y = y;
	_z = z;
	_type = type;
	_data = data;
	_light = light;
	_skyLight = skylight;
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

    public synchronized int getType() {
	return _type;
    }

    public synchronized int getData() {
	return _data;
    }

    public synchronized int getLight() {
	return _light;
    }

    public synchronized int getSkyLight() {
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
