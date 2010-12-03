
package com.tweakcraft.server.network.sendablePacket;

import com.tweakcraft.server.network.BaseSendablePacket;

/**
 *
 * @author Meaglin
 */
public class SendEntityLook extends BaseSendablePacket{

    private int _id;
    private float _rot,_pitch;

    public SendEntityLook(int id,float oldRot,float oldPitch,float newRot,float newPitch){
	_id = id;
	_rot = newRot - oldRot;
	_pitch = newPitch - oldPitch;

    }
    @Override
    protected void writeImpl() {
	writeByte(0x20);
	writeInt(_id);
	writeByte((byte)_rot);
	writeByte((byte)_pitch);
    }

}
