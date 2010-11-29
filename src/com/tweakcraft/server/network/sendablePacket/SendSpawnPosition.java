
package com.tweakcraft.server.network.sendablePacket;

import com.tweakcraft.server.network.BaseSendablePacket;

/**
 *
 * @author Meaglin
 */
public class SendSpawnPosition extends BaseSendablePacket {

    private final int _x,_y,_z;
    public SendSpawnPosition(int x,int y,int z){
	_x = x;
	_y = y;
	_z = z;
    }
    @Override
    protected void writeImpl() {
	writeByte(0x06);
	writeInt(_x);
	writeInt(_z);
	writeInt(_y);
    }

}
