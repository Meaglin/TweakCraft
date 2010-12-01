
package com.tweakcraft.server.network.sendablePacket;

import com.tweakcraft.server.network.BaseSendablePacket;

/**
 *
 * @author Meaglin
 */
public class SendChunk extends BaseSendablePacket {

    private int _x,_y,_z,_sizeX,_sizeY,_sizeZ,_dataSize;
    private byte[] _data;
    public SendChunk(int x,int y,int z,int sizeX,int sizeY,int sizeZ,int dataSize,byte[] data){
	_x = x;_y = y;_z = z;
	_sizeX = sizeX;_sizeY = sizeY;_sizeZ = sizeZ;
	_dataSize = dataSize;
	_data = data;
    }


    @Override
    protected void writeImpl() {
	writeByte(0x33);
	writeInt(_x);
	writeShort(_z);
	writeInt(_y);
	writeByte(_sizeX);
	writeByte(_sizeZ);
	writeByte(_sizeY);
	writeInt(_dataSize);
	writeB(_data,0,_dataSize);
	_data = null;
    }

}
