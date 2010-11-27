/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tweakcraft.server.network.receivablePacket;

import com.tweakcraft.server.instance.Player;
import com.tweakcraft.server.network.BaseReceivablePacket;
import com.tweakcraft.server.network.sendablePacket.SendHandshake;

/**
 *
 * @author SjoerdHuininga
 */
public class RequestHandshake extends BaseReceivablePacket{

    private String _message;

    // message is often the username.
    protected void readImpl() {
	_message = readString(readShort());
    }


    public void runImpl() {
	getClient().setPlayer(new Player(getClient()));
	getClient().getPlayer().onHandShake(_message);
    }

}