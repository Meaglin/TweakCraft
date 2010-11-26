/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tweakcraft.server.network.receivablePacket;

import com.tweakcraft.server.network.BaseReceivablePacket;

/**
 *
 * @author SjoerdHuininga
 */
public class Handshake extends BaseReceivablePacket{

    private String _message;




    protected void readImpl() {
	_message = readS();
    }


    public void runImpl() {
	System.out.println("We received a handshake: " + _message);
    }

}