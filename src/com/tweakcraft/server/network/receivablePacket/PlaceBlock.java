
package com.tweakcraft.server.network.receivablePacket;

import com.tweakcraft.server.network.BaseReceivablePacket;

/**
 *
 * @author Meaglin
 */
public class PlaceBlock  extends BaseReceivablePacket{

    private int _x,_y,_z,_itemId,_direction;
    
    @Override
    protected void readImpl() {
	_itemId = readByte();
	_x = readInt();
	_z = readByte();
	_y = readInt();
	_direction = readByte();
    }

    @Override
    public void runImpl() {
	//TODO: Implement.
    }

}
