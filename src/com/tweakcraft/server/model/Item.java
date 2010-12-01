package com.tweakcraft.server.model;

import com.tweakcraft.server.nbt.Tag;

/**
 *
 * @author Meaglin
 */
public class Item {

    private short _type, _health;
    private byte _amount, _slot;

    public Item() {
	_type = 0;
	_amount = 0;
	_health = 0;
	_slot = 0;
    }

    public Item(int type, int amount) {
	_type = (short) type;
	_amount = (byte) amount;
	_health = 0;
	_slot = 0;
    }

    public Item(int type, int amount, int health) {
	_type = (short) type;
	_amount = (byte) amount;
	_health = (short) health;
	_slot = 0;
    }

    public Item(int type, int amount, int health, int slot) {
	_type = (short) type;
	_amount = (byte) amount;
	_health = (short) health;
	_slot = (byte) slot;
    }
    /* setThing(sst.getName(), sst.getType(), sst.getValue()); */

    public void setAttribute(String naam, Tag.Type type, Object value) {
	if (naam.trim().equalsIgnoreCase("id"))
	    _type = (Short) value;
	else if (naam.trim().equalsIgnoreCase("Damage"))
	    _health = (Short) value;
	else if (naam.trim().equalsIgnoreCase("Count"))
	    _amount = (Byte) value;
	else if (naam.trim().equalsIgnoreCase("Slot"))
	    _slot = (Byte) value;
    }

    public int getType() {
	return _type;
    }

    public int getAmount() {
	return _amount;
    }

    public int getHealth() {
	return _health;
    }

    public int getSlot() {
	return _slot;
    }

    public void setType(int type) {
	_type = (short)type;
    }

    public void setAmount(int amount) {
	_amount = (byte) amount;
    }

    public void setHealth(int health) {
	_health = (short) health;
    }

    public void setSlot(int slot) {
	_slot = (byte) slot;
    }
}
