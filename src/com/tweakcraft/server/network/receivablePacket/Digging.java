
package com.tweakcraft.server.network.receivablePacket;

import com.tweakcraft.server.network.BaseReceivablePacket;

/**
 *
 * @author Meaglin
 */
public class Digging extends BaseReceivablePacket{

    private int _x,_y,_z,_action,_face;

    @Override
    protected void readImpl() {
	_action = readByte();
	_x = readInt();
	_z = readByte();
	_y = readInt();
	_face = readByte();
    }

    @Override
    public void runImpl() {
	//TODO: implement.
    }

}
