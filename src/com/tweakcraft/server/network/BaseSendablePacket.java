/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tweakcraft.server.network;

import org.mmocore.network.SendablePacket;

/**
 *
 * @author Meaglin
 */
public abstract class BaseSendablePacket extends SendablePacket<GameClient> {

    @Override
    protected void write() {
	try {
	    writeImpl();
	} catch (Exception e) {
	}
    }
    protected abstract void writeImpl();
    
}
