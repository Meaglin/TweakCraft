
package com.tweakcraft.server.network.receivablePacket;

import com.tweakcraft.server.network.BaseReceivablePacket;

/**
 *
 * @author Meaglin
 */
public class RequestChangePositionAndLook extends BaseReceivablePacket {

    private double _x,_y,_z,_stance;
    private float _rotation,_pitch;
    private boolean _onGround;
    @Override
    protected void readImpl() {
	_x = readDouble();
	_z = readDouble();
	_stance = readDouble();
	_y = readDouble();
	_rotation = readFloat();
	_pitch = readFloat();
	_onGround = readByte() == 0x01 ? true : false;

    }

    @Override
    public void runImpl() {
	//TODO: Implement.
    }

}
