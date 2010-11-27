/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tweakcraft.server.network;

import java.util.logging.Logger;
import org.mmocore.network.SendablePacket;

/**
 *
 * @author Meaglin
 */
public abstract class BaseSendablePacket extends SendablePacket<GameClient> {

    protected static final Logger _log = Logger.getLogger(BaseSendablePacket.class.getName());

    @Override
    protected void write() {
	try {
	    writeImpl();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    protected abstract void writeImpl();
    
}
