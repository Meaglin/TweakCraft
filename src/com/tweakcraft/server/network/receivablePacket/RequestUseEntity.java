package com.tweakcraft.server.network.receivablePacket;

import com.tweakcraft.server.network.BaseReceivablePacket;

/**
 *
 * @author Meaglin
 */
public class RequestUseEntity extends BaseReceivablePacket{

    private int _userId,_entityId;
    private boolean _isPunshing;
    @Override
    protected void readImpl() {
	_userId = readInt();
	_entityId = readInt();
	_isPunshing = (readByte() == 0x01 ? true : false);
    }

    @Override
    public void runImpl() {
	// TODO: Implement
    }

}
