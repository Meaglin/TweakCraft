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

	if(opcode != 0 && opcode < 10)
	    System.out.println("receive packet " + opcode);

	switch (opcode) {
	    case 0x00:
		packet = new Ping();
		break;
	    case 0x01:
		packet = new RequestLogin();
		break;
	    case 0x02:
		packet = new RequestHandshake();
		break;
	    case 0x03:
		packet = new RequestChat();
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
	    case 0x0A:
		packet = new RequestChangeOnGround();
		break;
	    case 0x0B:
		packet = new RequestChangePosition();
		break;
	    case 0x0C:
		packet = new RequestChangeLook();
		break;
	    case 0x0D:
		packet = new RequestChangePositionAndLook();
		break;
	    case 0xFF:
		packet = new RequestDisconnect();
		break;
	    default:
		System.out.println("Unknown packet with opcode : " + opcode);
		break;
	}

	return packet;
    }


    public GameClient create(MMOConnection<GameClient> con) {
	return  new GameClient(con);
    }

    public void execute(ReceivablePacket<GameClient> rp) {
	ThreadPoolManager.getInstance().execute(rp);
    }

    public int getPacketSize(ByteBuffer bb, GameClient t) {
	ByteBuffer buf = bb.duplicate();
	buf.position(0);

	int packetId = buf.get();
	//if(packetId != 0)_log.info(packetId + " " + buf.remaining());
	
//	if(true)return bb.remaining();

	int packetSize = 0;
	switch (packetId) {
	    // Ping
	    case 0x00:
		packetSize =  1;
		break;
	    // RequestLogin
	    case 0x01:
		int userlen = getShort(buf.get(5), buf.get(6));
		int passlen = getShort(buf.get(7 + userlen), buf.get(8 + userlen));
                
		packetSize =  6 + userlen + 2 + passlen + 10;
		break;
	    // HandShake
	    case 0x02:
		packetSize =  3 + getShort(buf.get(), buf.get());
		break;
	    // Chat
	    case 0x03:
		    packetSize =  3 + getShort(buf.get(1), buf.get(2));
		break;
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
		packetSize =  7 + size;
		break;
	    // Request Use Entity ?
	    case 0x07:
		packetSize =  10;
		break;
	    // Request Respawn.
	    case 0x09:
		packetSize =  2;
		break;
	    //Player on ground
	    case 0x0A:
		packetSize =  2;
		break;
	    //Player position
	    case 0x0B:
		packetSize =  34;
		break;
	    //Player look
	    case 0x0C:
		packetSize =  10;
		break;
	    //Player position+look
	    case 0x0D:
		packetSize =  42;
		break;
	    //Player Digging / left clicking a block
	    case 0x0E:
		packetSize =  12;
		break;
	    //Player Block Placement / right clicking a block.
	    case 0x0F:
		packetSize =  13;
		break;
	    // Item Hold Change.
	    case 0x10:
		packetSize =  7;
		break;
	    // Arm swing(left click into distance).
	    case 0x12:
		packetSize =  6;
		break;
	    // Drop item.
	    case 0x15:
		packetSize =  23;
		break;
	    //Disconnect.
	    case 0xFF:
		packetSize =  3 + getShort(buf.get(),buf.get());
		break;
	    default:
		packetSize =  0;
		break;

	}
//	if(packetId > 0)
	    _log.info(packetId + " " + buf.remaining() + " " + packetSize);
	return packetSize;
    }

    private static int getShort(byte b1,byte b2){
	return (((b1 & 0xFF ) << 8) | (b2 & 0xFF));
    }
}
