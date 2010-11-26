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
public class Handshake extends BaseSendablePacket {

    @Override
    protected void writeImpl() {
	writeC(0x02);
    }

}
