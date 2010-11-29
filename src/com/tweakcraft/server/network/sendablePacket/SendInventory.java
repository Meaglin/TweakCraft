
package com.tweakcraft.server.network.sendablePacket;

import com.tweakcraft.server.instance.Player;
import com.tweakcraft.server.model.Inventory;
import com.tweakcraft.server.model.Item;
import com.tweakcraft.server.network.BaseSendablePacket;

/**
 *
 * @author Meaglin
 */
public class SendInventory extends BaseSendablePacket{

    private int _type;
    private Inventory _inv;

    public SendInventory(Player p){
	_type = -1;
	_inv = p.getInventory();
    }
    public SendInventory(int type, Inventory inv){
	_type = type;
	_inv = inv;
    }
    @Override
    protected void writeImpl() {
	writeByte(0x05);
	writeInt(_type);
	writeShort(_inv.getSize());
//	int size = 0;
//	for(Item i : _inv.getContents())
//	    if(i == null)
//		size += 2;
//	    else
//		size += 5;
//
//	writeInt(size);
	for(Item i : _inv.getContents())
	    if(i == null){
		writeShort(-1);
	    }else{
		writeShort(i.getType());
		writeByte(i.getAmount());
		writeShort(i.getHealth());
	    }
    }

}
