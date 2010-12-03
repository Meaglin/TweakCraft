
package com.tweakcraft.server.network.receivablePacket;

import com.tweakcraft.server.network.BaseReceivablePacket;

/**
 *
 * @author Meaglin
 */
public class RequestChangePosition extends BaseReceivablePacket{

    private double _x,_y,_z,_stance;
    private boolean _onGround;
    @Override
    protected void readImpl() {
	_x = readDouble();
	_z = readDouble();
	_stance = readDouble();
	_y = readDouble();
	_onGround = readByte() == 1 ? true : false;
    }

    @Override
    public void runImpl() {
	getClient().getPlayer().onMove(1, _x, _y, _z, _stance, 0, 0);
    }

}
