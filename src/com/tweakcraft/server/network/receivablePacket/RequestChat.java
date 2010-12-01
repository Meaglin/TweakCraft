package com.tweakcraft.server.network.receivablePacket;

import com.tweakcraft.server.network.BaseReceivablePacket;

/**
 *
 * @author Meaglin
 */
public class RequestChat extends BaseReceivablePacket{

    private String _message;
    
    protected void readImpl() {
	_message = readString(readShort());
    }


    public void runImpl() {
	getClient().getPlayer().onChat(_message);
    }

}
