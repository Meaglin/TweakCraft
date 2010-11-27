package com.tweakcraft.server.network.receivablePacket;

import com.tweakcraft.server.network.BaseReceivablePacket;

/**
 *
 * @author Meaglin
 */
public class RequestRespawn extends BaseReceivablePacket{

    @Override
    protected void readImpl() {
	// always == 0, pointless to store it.
	readByte();
    }

    @Override
    public void runImpl() {
	// TODO: Implement.
    }

}
