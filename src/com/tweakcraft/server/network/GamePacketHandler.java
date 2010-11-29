package com.tweakcraft.server.network;

import com.tweakcraft.server.ThreadPoolManager;
import com.tweakcraft.server.network.receivablePacket.*;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

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

    private static Logger _log = Logger.getLogger(GamePacketHandler.class.getName());

    public ReceivablePacket<GameClient> handlePacket(ByteBuffer buf,
	    GameClient client) {


	ReceivablePacket<GameClient> packet = null;
	buf.position(0);

	int opcode = buf.get()& 0xFF;

	_log.info("receive packet " + opcode);

	switch (opcode) {
	    case 0x01:
		packet = new RequestLogin();
		break;
	    case 0x02:
		packet = new RequestHandshake();
		break;
	    case 0x03:
		packet = new Chat();
		break;
	    case 0x05:
		packet = new RequestInventoryUpdate();
		break;
	    case 0x07:
		packet = new RequestUseEntity();
		break;
	    case 0x09:
		packet = new RequestRespawn();
		break;
	    case 0x0B:
		packet = new RequestChangePosition();
		break;
	    default:
		System.out.println("Unknown packet with opcode : " + opcode);
		break;
	}

	return packet;
    }


    public GameClient create(MMOConnection<GameClient> con) {
	return new GameClient(con);
    }

    public void execute(ReceivablePacket<GameClient> rp) {
	ThreadPoolManager.getInstance().execute(rp);
    }

    public int getPacketSize(ByteBuffer bb, GameClient t) {
	ByteBuffer buf = bb.duplicate();
	buf.position(0);

	int packetId = buf.get();

	//_log.info(packetId + "");

	switch (packetId) {
	    // Ping
	    case 0x00:
		return 0;
	    // RequestLogin
	    case 0x01:
		int userlen = getShort(buf.get(5), buf.get(6));
		int passlen = getShort(buf.get(7 + userlen), buf.get(8 + userlen));
                
		return 6 + userlen + 2 + passlen + 10;
	    // HandShake
	    case 0x02:
		return 3 + getShort(buf.get(), buf.get());
	    // Chat
	    case 0x03:
		return getShort(buf.get(), buf.get());
	    // Inventory
	    case 0x05:
		int type = buf.getInt();
		int size = 0;
		switch (type) {
		    case -1:
			for (int i = 0; i < 36; i++)
			    if (buf.getShort() == -1)
				size += 2;
			    else {
				size += 5;
				// move the buffer ahead.
				buf.get();
				buf.getShort();
			    }
			break;
		    case -2:
		    case -3:
			for (int i = 0; i < 4; i++)
			    if (buf.getShort() == -1)
				size += 2;
			    else {
				size += 5;
				// move the buffer ahead.
				buf.get();
				buf.getShort();
			    }
			break;
		}
		return 7 + size;
	    // Request Use Entity ?
	    case 0x07:
		return 10;
	    // Request Respawn.
	    case 0x09:
		return 2;

	    //Player position
	    case 0x0B:
		return 34;
	    //Player look
	    case 0x0C:
		return 10;
	    //Player position+look
	    case 0x0D:
		return 42;
	    default:
		return 0;

	}
    }

    private static int getShort(byte b1,byte b2){
	return (((b1 & 0xFF ) << 8) | (b2 & 0xFF));
    }
}
