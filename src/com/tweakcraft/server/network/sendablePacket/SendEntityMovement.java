
package com.tweakcraft.server.network.sendablePacket;

import com.tweakcraft.server.network.BaseSendablePacket;

/**
 *
 * @author Meaglin
 */
public class SendEntityMovement extends BaseSendablePacket {

    private int _id;
    private double _x,_y,_z;
    
    public SendEntityMovement(int id,double oldx,double oldy,double oldz,double newx,double newy,double newz){
	_id = id;
	_x = newx - oldx;
	_y = newy - oldy;
	_z = newz - oldz;
    }
    @Override
    protected void writeImpl() {
	writeByte(0x1F);
	writeInt(_id);
	writeByte((byte)_x);
	writeByte((byte)_z);
	writeByte((byte)_y);
    }

}
