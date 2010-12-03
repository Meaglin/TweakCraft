
package com.tweakcraft.server.network.sendablePacket;

import com.tweakcraft.server.instance.Player;
import com.tweakcraft.server.network.BaseSendablePacket;

/**
 *
 * @author Meaglin
 */
public class SendPosition extends BaseSendablePacket{

    private double _x,_y,_z,_stance;
    private boolean _onGround;
    public SendPosition(Player p){
	_x = p.getX();
	_y = p.getY();
	_z = p.getZ();
	_stance = p.getStance();
	_onGround = true;
    }
    public SendPosition(double x, double y,double z,double stance,boolean onGround){
	_x = x;
	_y = y;
	_z = z;
	_stance = stance;
	_onGround = onGround;
    }
    @Override
    protected void writeImpl() {
	writeByte(0x0B);
	writeDouble(_x);
	writeDouble(_z);
	writeDouble(_stance);
	writeDouble(_y);
	writeByte(_onGround ? 0x01 : 0x00);
    }

}
