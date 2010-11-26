package com.tweakcraft.server.instance;

import com.tweakcraft.server.network.BaseSendablePacket;
import com.tweakcraft.server.network.GameClient;
import com.tweakcraft.server.network.sendablePacket.*;

/**
 * Player class.
 * @author Meaglin
 */
public class Player extends Character{

    private GameClient _client;
    public Player(GameClient client){
	_client = client;
    }
    public void onLoginRequest(int protocol, String username, String password, Long mapSeed, byte dimension){
	sendPacket(new ErrorMessage("Not supported yet."));
    }
    public void onLogin(){

    }
    public GameClient getClient(){
	return _client;
    }
    public void sendPacket(BaseSendablePacket packet){
	getClient().sendPacket(packet);
    }
}
