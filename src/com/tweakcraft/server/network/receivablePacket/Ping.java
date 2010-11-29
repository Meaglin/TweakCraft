
package com.tweakcraft.server.network.receivablePacket;

import com.tweakcraft.server.network.BaseReceivablePacket;

/**
 *
 * @author Meaglin
 */
public class Ping extends BaseReceivablePacket{

    @Override
    protected void readImpl() {
	//nothing to do here.
    }

    @Override
    public void runImpl() {
	// implement ?
    }

}
