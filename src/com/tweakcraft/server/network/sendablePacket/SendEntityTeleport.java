
package com.tweakcraft.server.network.sendablePacket;

import com.tweakcraft.server.network.BaseSendablePacket;

/**
 *
 * @author Meaglin
 */
public class SendEntityTeleport extends BaseSendablePacket {

    private int _id;
    private double _x,_y,_z;
    private float _rot,_pitch;
    public SendEntityTeleport(int id,double x,double y,double z,float rot,float pitch){
	_id = id;
	_x = x;
	_y = y;
	_z = z;
	_rot = rot;
	_pitch = pitch;
    }

    @Override
    protected void writeImpl() {
	writeByte(0x22);
	writeInt(_id);
	writeInt((int)_x);
	writeInt((int)_y);
	writeInt((int)_z);
	writeByte((byte)_rot);
	writeByte((byte)_pitch);
    }

}
