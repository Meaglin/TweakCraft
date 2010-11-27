package com.tweakcraft.server.network.sendablePacket;

import com.tweakcraft.server.network.BaseSendablePacket;

/**
 *
 * @author Meaglin
 */
public class SendHandshake extends BaseSendablePacket {

    @Override
    protected void writeImpl() {
	writeByte(0x02);
	writeString("-");
    }

}
