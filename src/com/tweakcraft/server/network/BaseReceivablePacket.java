/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tweakcraft.server.network;

import org.mmocore.network.ReceivablePacket;

/**
 *
 * @author Meaglin
 */
public abstract class BaseReceivablePacket extends ReceivablePacket<GameClient> {

    protected abstract void readImpl();
    public abstract void runImpl();

    @Override
    protected boolean read() {
	try {
	    readImpl();
	    return true;
	} catch (Exception e) {
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
