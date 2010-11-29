package com.tweakcraft.server.network.receivablePacket;

import com.tweakcraft.server.model.Item;
import com.tweakcraft.server.network.BaseReceivablePacket;

/**
 *
 * @author Meaglin
 */
public class RequestInventoryUpdate extends BaseReceivablePacket{

    private int _type,_count;
    private Item[] _items;
    @Override
    protected void readImpl() {
	_type = readInt();
	_count = readShort();
	_items = new Item[(_type == -1 ? 36 : 4)];
	for(int i = 0;i < (_type == -1 ? 36 : 4); i++){
	    short type = (short) readShort();
	    if(type == -1)
	    {
		_items[i] = new Item();
	    }else{
		_items[i] = new Item(type,(byte)readByte(),(short)readShort());
	    }

	}
    }

    @Override
    public void runImpl() {
	// TODO: Implement.
    }

}
