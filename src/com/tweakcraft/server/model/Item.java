package com.tweakcraft.server.model;

import com.tweakcraft.server.nbt.Tag;

/**
 *
 * @author Meaglin
 */
public class Item {

    private Short _type,_health;
    private Byte  _amount,_slot;


    public Item()
    {
        _type = 0;
        _amount = 0;
        _health = 0;
        _slot = 0;
    }

    public Item(Short type,Byte amount){
	    _type = type;
	    _amount = amount;
	    _health = 0;
        _slot = 0;
    }

    public Item(Short type, Byte amount, Short health) {
	    _type = type;
	    _amount = amount;
	    _health = health;
        _slot = 0;
    }

    public Item(Short type, Byte amount, Short health, Byte slot) {
	    _type = type;
	    _amount = amount;
	    _health = health;
        _slot = slot;
    }
    /* setThing(sst.getName(), sst.getType(), sst.getValue()); */

    public void setThing(String naam, Tag.Type type, Object value)
    {
        if(naam.trim().equalsIgnoreCase("id"))
            _type = (Short) value;
        else if(naam.trim().equalsIgnoreCase("Damage"))
            _health = (Short) value;
        else if(naam.trim().equalsIgnoreCase("Count"))
            _amount = (Byte) value;
        else if(naam.trim().equalsIgnoreCase("Slot"))
            _slot = (Byte) value;
    }

    public Short getType(){
	    return _type;
    }

    public Byte getAmount(){
	    return _amount;
    }

    public Short getHealth(){
	    return _health;
    }

    public Short get_type() {
        return _type;
    }

    public void set_type(Short _type) {
        this._type = _type;
    }

    public Byte get_amount() {
        return _amount;
    }

    public void set_amount(Byte _amount) {
        this._amount = _amount;
    }

    public Short get_health() {
        return _health;
    }

    public void set_health(Short _health) {
        this._health = _health;
    }

    public Byte get_slot() {
        return _slot;
    }

    public void set_slot(Byte _slot) {
        this._slot = _slot;
    }
}
