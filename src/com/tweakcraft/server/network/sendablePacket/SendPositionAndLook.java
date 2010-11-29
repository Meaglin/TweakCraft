
package com.tweakcraft.server.network.sendablePacket;

import com.tweakcraft.server.instance.Player;
import com.tweakcraft.server.network.BaseSendablePacket;

/**
 *
 * @author Meaglin
 */
public class SendPositionAndLook extends BaseSendablePacket{

    private double _x,_y,_z,_stance;
    private float _rotation,_pitch;
    private boolean _onGround;

    public SendPositionAndLook(double x,double y,double z,double stance,float rotation,float pitch,boolean onground){
	_x = x;
	_y = y;
	_z = z;
	_stance = stance;
	_rotation = rotation;
	_pitch = pitch;
	_onGround = onground;
    }

    public SendPositionAndLook(Player p) {
	_x = p.getX();
	_y = p.getY();
	_z = p.getZ();
	_stance = p.getStance();
	_rotation = p.getRotation();
	_pitch = p.getPitch();
	_onGround = true;
    }

    @Override
    protected void writeImpl() {
	writeByte(0x0D);
	writeDouble(_x);
	writeDouble(_stance);
	writeDouble(_z);
	writeDouble(_y);
	writeFloat(_rotation);
	writeFloat(_pitch);
	writeByte(_onGround ? 0x01 : 0x00);
    }

}
