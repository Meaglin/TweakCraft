
package com.tweakcraft.server.network.receivablePacket;

import com.tweakcraft.server.network.BaseReceivablePacket;

/**
 *
 * @author Meaglin
 */
public class RequestArmAnimation extends BaseReceivablePacket{

    private int _entityId;
    private boolean _animate;
    @Override
    protected void readImpl() {
	_entityId = readInt();
	_animate = readByte() == 0x01;
    }

    @Override
    public void runImpl() {
	//TODO: Implement.
    }

}
