
package com.tweakcraft.server.network.sendablePacket;

import com.tweakcraft.server.network.BaseSendablePacket;

/**
 *
 * @author Meaglin
 */
public class SendEntityMovementAndLook extends BaseSendablePacket {

    public int _id;
    private double _x,_y,_z;
    private float _rot,_pitch;
    public SendEntityMovementAndLook(int id,double oldx,double oldy,double oldz,float oldRot,float oldPitch,double newx,double newy,double newz,float newRot,float newPitch){
	_id = id;
	_x = newx - oldx;
	_y = newy - oldy;
	_z = newz - oldz;
	_rot = newRot - oldRot;
	_pitch = newPitch - oldPitch;
    }
    @Override
    protected void writeImpl() {
	writeByte(0x21);
	writeInt(_id);
	writeByte((byte)_x);
	writeByte((byte)_z);
	writeByte((byte)_y);
	writeByte((byte)_rot);
	writeByte((byte)_pitch);
    }

}
