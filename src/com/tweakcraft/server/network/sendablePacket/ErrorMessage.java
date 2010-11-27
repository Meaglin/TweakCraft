package com.tweakcraft.server.network.sendablePacket;

import com.tweakcraft.server.network.BaseSendablePacket;

/**
 *
 * @author Meaglin
 */
public class ErrorMessage extends BaseSendablePacket{

    private String _error;
    public ErrorMessage(String message){
	_error = message;
    }
    @Override
    protected void writeImpl() {
	writeByte(0xFF);
	writeString(_error);
    }

}
