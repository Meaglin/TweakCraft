
package com.tweakcraft.server.network.receivablePacket;

import com.tweakcraft.server.network.BaseReceivablePacket;

/**
 *
 * @author Meaglin
 */
public class RequestChangeOnGround extends BaseReceivablePacket{

    private boolean _onGround;
    @Override
    protected void readImpl() {
	_onGround = readByte() == 0x01 ? true : false;
    }

    @Override
    public void runImpl() {
	//TODO: Implement.
    }

}
