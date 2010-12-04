
package com.tweakcraft.server.network.receivablePacket;

import com.tweakcraft.server.network.BaseReceivablePacket;

/**
 *
 * @author Meaglin
 */
public class RequestDropItem extends BaseReceivablePacket{

    private int _entityId,_itemId,_count,_x,_y,_z,_rotation,_pitch,_roll;

    @Override
    protected void readImpl() {
	_entityId = readInt();
	_itemId = readShort();
	_count = readByte();
	_x = readInt();
	_z = readInt();
	_y = readInt();
	_rotation = readByte();
	_pitch = readByte();
	_roll = readByte();
    }

    @Override
    public void runImpl() {
	//TODO: Implement.
    }

}
