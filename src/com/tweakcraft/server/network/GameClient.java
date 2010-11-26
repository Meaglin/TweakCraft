package com.tweakcraft.server.network;

import com.tweakcraft.server.instance.Player;
import java.nio.ByteBuffer;
import org.mmocore.network.MMOClient;
import org.mmocore.network.MMOConnection;

/**
 *
 * @author Meaglin
 */
public class GameClient extends MMOClient<MMOConnection<GameClient>> {

    private GameCrypt _crypt;
    private Player _player;

    public GameClient(MMOConnection<GameClient> con) {
	super(con);
	_crypt = new GameCrypt();
    }

    public void sendPacket(BaseSendablePacket pk){
	getConnection().sendPacket(pk);
    }
    
    @Override
    public boolean decrypt(ByteBuffer buf, int size) {
	_crypt.decrypt(buf.array(), buf.position(), size);
	return true;
    }

    @Override
    public boolean encrypt(final ByteBuffer buf, final int size) {
	_crypt.encrypt(buf.array(), buf.position(), size);
	buf.position(buf.position() + size);
	return true;
    }

    @Override
    protected void onDisconnection() {
	// TODO
	System.out.println("dc");
    }

    @Override
    protected void onForcedDisconnection() {
	// TODO
	System.out.println("dcd");
    }

    public Player getPlayer() {
	return _player;
    }

    public void setPlayer(Player player) {
	_player = player;
    }
}
