
package com.tweakcraft.server.network.sendablePacket;

import com.tweakcraft.server.network.BaseSendablePacket;

/**
 *
 * @author Meaglin
 */
public class PreChunk extends BaseSendablePacket {

    private int _x,_y;
    private boolean _load;
    public PreChunk(int x, int y, boolean load){
	_x = x;
	_y = y;
	_load = load;
    }
    @Override
    protected void writeImpl() {
	writeByte(0x32);
	writeInt(_x);
	writeInt(_y);
	writeByte(_load ? 0x01 : 0x00);
    }

}
