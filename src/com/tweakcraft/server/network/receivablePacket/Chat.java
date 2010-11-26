/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tweakcraft.server.network.receivablePacket;

import com.tweakcraft.server.network.BaseReceivablePacket;

/**
 *
 * @author Meaglin
 */
public class Chat extends BaseReceivablePacket{

    private String _message;




    protected void readImpl() {
	_message = readString(readShort());
    }


    public void runImpl() {
	System.out.println("We received a chat: " + _message);
    }

}
