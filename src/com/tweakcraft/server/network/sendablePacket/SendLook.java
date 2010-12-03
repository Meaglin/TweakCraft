
package com.tweakcraft.server.network.sendablePacket;

import com.tweakcraft.server.instance.Player;
import com.tweakcraft.server.network.BaseSendablePacket;

/**
 *
 * @author Meaglin
 */
public class SendLook extends BaseSendablePacket{

    private float _pitch,_rotation;
    private boolean _onGround;

    public SendLook(Player p){
	_rotation = p.getRotation();
	_pitch = p.getPitch();
	_onGround = true;
    }
    public SendLook(float rotation,float pitch,boolean onGround){
	_rotation = rotation;
	_pitch = pitch;
	_onGround = onGround;
    }
    @Override
    protected void writeImpl() {
	writeByte(0x0C);
	writeFloat(_rotation);
	writeFloat(_pitch);
	writeByte(_onGround ? 0x01 : 0x00);
    }

}
