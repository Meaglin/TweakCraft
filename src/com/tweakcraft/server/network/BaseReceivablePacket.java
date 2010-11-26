/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tweakcraft.server.network;

import java.util.logging.Logger;
import org.mmocore.network.ReceivablePacket;

/**
 *
 * @author Meaglin
 */
public abstract class BaseReceivablePacket extends ReceivablePacket<GameClient> {

    protected static final Logger _log = Logger.getLogger(BaseReceivablePacket.class.getName());

    protected abstract void readImpl();
    public abstract void runImpl();

    @Override
    protected boolean read() {
	try {
	    readImpl();
	    return true;
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return false;
    }

    @Override
    public void run() {
	try {
	    runImpl();
	} catch (Throwable t) {}
    }

}
