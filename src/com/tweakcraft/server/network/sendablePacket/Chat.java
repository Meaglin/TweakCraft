/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tweakcraft.server.network.sendablePacket;

import com.tweakcraft.server.network.BaseSendablePacket;

/**
 *
 * @author Meaglin
 */
public class Chat extends BaseSendablePacket {

    private String _message;
    public Chat(String message){
	_message = message;
    }
    @Override
    protected void writeImpl() {
	writeC(0x03);
	writeS(_message);
    }

}
