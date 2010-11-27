/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tweakcraft.server.network.sendablePacket;

import com.tweakcraft.server.network.BaseSendablePacket;

/**
 *
 * @author SjoerdHuininga
 */
public class SendHandshake extends BaseSendablePacket {

    @Override
    protected void writeImpl() {
	writeByte(0x02);
	writeString("-");
	_log.info("Send handshake");
    }

}
