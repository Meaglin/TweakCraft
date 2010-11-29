
package com.tweakcraft.server.network.receivablePacket;

import com.tweakcraft.server.network.BaseReceivablePacket;

/**
 *
 * @author Meaglin
 */
public class RequestChangeLook extends BaseReceivablePacket{

    private float _rotation,_pitch;
    private boolean _onGround;

    @Override
    protected void readImpl() {
	_rotation = readFloat();
	_pitch = readFloat();
	_onGround = readByte() == 0x01 ? true : false;
    }

    @Override
    public void runImpl() {
	//TODO: implement.
    }

}
