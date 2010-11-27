package com.tweakcraft.server.model;

/**
 *
 * @author Meaglin
 */
public class Item {

    private int _type,_amount,_health;

    public Item(int type,int amount){
	_type = type;
	_amount = amount;
	_health = 0;
    }

    public Item(int type, int amount, int health) {
	_type = type;
	_amount = amount;
	_health = health;
    }
    public int getType(){
	return _type;
    }
    public int getAmount(){
	return _amount;
    }
    public int getHealth(){
	return _health;
    }
}
