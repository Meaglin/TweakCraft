package com.tweakcraft.server.model;

/**
 *
 * @author Meaglin
 */
public class Inventory {

    private int _size;
    private Item[] _contents;
    public Inventory(int size){
	_size = size;
	_contents = new Item[_size];
    }
    public int getSize(){
	return _size;
    }
    public Item[] getContents(){
	return _contents;
    }
    public void setSlot(int slot,Item item){
	if(slot < 0 || slot > _size-2)
	    return;
	_contents[slot] = item;
    }
    public Item getSlot(int slot){
	if(slot < 0 || slot > _size-2)
	    return new Item(-1,0);
	else
	    return _contents[slot];
    }

}
