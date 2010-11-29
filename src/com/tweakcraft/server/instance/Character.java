package com.tweakcraft.server.instance;

import java.util.logging.Logger;

/**
 * Character class, base class for all moving entity's.
 * @author Meaglin
 */
public class Character {

    private double _x, _y, _z, _stance;
    private float _rotation, _pitch;
    private final int _entityId;
    protected static final Logger _log = Logger.getLogger(Character.class.getName());

    public Character(int id) {
	_entityId = id;
    }

    public double getX() {
	return _x;
    }

    public double getY() {
	return _y;
    }

    public double getZ() {
	return _z;
    }

    public float getPitch() {
	return _pitch;
    }

    public float getRotation() {
	return _rotation;
    }

    public int getId() {
	return _entityId;
    }

    public double getStance() {
	return _stance;
    }


    protected void setX(double x) {
	_x = x;
    }

    protected void setY(double y) {
	_y = y;
    }

    protected void setZ(double z) {
	_z = z;
    }

    public void setPitch(float pitch) {
	_pitch = pitch;
    }

    public void setRotation(float rotation) {
	_rotation = rotation;
    }

    public void setStance(double stance) {
	_stance = stance;
    }
}
