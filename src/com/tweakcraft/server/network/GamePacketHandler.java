package com.tweakcraft.server.network;

import com.tweakcraft.server.ThreadPoolManager;
import java.nio.ByteBuffer;

import org.mmocore.network.IClientFactory;
import org.mmocore.network.IMMOExecutor;
import org.mmocore.network.IPacketHandler;
import org.mmocore.network.MMOConnection;
import org.mmocore.network.ReceivablePacket;

/**
 *
 * @author Meaglin
 */
public class GamePacketHandler implements IPacketHandler<GameClient>, IClientFactory<GameClient>, IMMOExecutor<GameClient> {

    @Override
    public ReceivablePacket<GameClient> handlePacket(ByteBuffer buf,
	    GameClient client) {


	ReceivablePacket<GameClient> packet = null;

	int opcode = buf.get() & 0xFF;

	switch (opcode) {
	    case 0x00:
		//packet = new packet();
		break;
	}

	return packet;
    }

    @Override
    public GameClient create(MMOConnection<GameClient> con) {
	return new GameClient(con);
    }

    @Override
    public void execute(ReceivablePacket<GameClient> packet) {
	ThreadPoolManager.getInstance().execute(packet);
    }
}
