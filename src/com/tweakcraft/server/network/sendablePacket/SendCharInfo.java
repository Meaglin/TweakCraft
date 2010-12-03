
package com.tweakcraft.server.network.sendablePacket;

import com.tweakcraft.server.instance.Player;
import com.tweakcraft.server.network.BaseSendablePacket;

/**
 *
 * @author Meaglin
 */
public class SendCharInfo extends BaseSendablePacket {

    private int _x,_y,_z,_id;
    private byte _rotation,_pitch;
    private short _itemInHand;
    private String _name;

    public SendCharInfo(Player p){
	_id = p.getId();
	_x = (int) p.getX();
	_y = (int) p.getY();
	_z = (int) p.getZ();
	_rotation = (byte) p.getRotation();
	_pitch = (byte) p.getPitch();
	_itemInHand = 1;
	_name = p.getUsername();
    }
    @Override
    protected void writeImpl() {
	writeByte(0x14);
	writeInt(_id);
	writeString(_name);
	writeInt(_x);
	writeInt(_z);
	writeInt(_y);
	writeByte(_rotation);
	writeByte(_pitch);
	writeShort(_itemInHand);
    }

}
