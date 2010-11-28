package com.tweakcraft.server.instance;

import com.tweakcraft.server.Config;
import com.tweakcraft.server.network.BaseSendablePacket;
import com.tweakcraft.server.network.GameClient;
import com.tweakcraft.server.network.sendablePacket.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.URLConnection;
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
	System.out.println("Login req prot"+protocol+"u:"+username+" p "+password+" mS "+mapSeed );

	if(!Config.OFFLINE_MODE && !validUser(username))
	    sendPacket(new ErrorMessage("Failed to login: User not premium"));
	else
	    sendPacket(new ErrorMessage("Not supported yet."));
    }

    private boolean validUser(String username) {
	//Okay, user wants to login, we'll have to check whether the user is who claims he is
	String urlStr = Config.LOGIN_HANDLER + "user=" + username + "&serverId=" + Config.SERVER_HASH; //+global_hash;
	//System.out.println("Server req:"+ urlStr);
	String serverResponse = "";
	BufferedReader rd = null;
	InputStreamReader str = null;
	try{
	    URL url = new URL(urlStr);
	    URLConnection conn = url.openConnection();

	    str = new InputStreamReader(conn.getInputStream());
	    rd = new BufferedReader(str);

	    // Get the response
	    serverResponse = rd.readLine();
	}catch(Exception e){
	    e.printStackTrace();
	}finally {
	    try{
		if(str != null)str.close();
		if(rd != null) rd.close();
	    }catch(Exception e){}
	}
	if (serverResponse.equals("YES"))
	    return true;
	else
	    return false;
    }

    // not implemented yet.
    public void onLogin() {
    }

    public void onHandShake(String message){
	sendPacket(new SendHandshake());
    }

    public void onDisconnect(){
	
    }

    public GameClient getClient(){
	return _client;
    }


    public void sendPacket(BaseSendablePacket packet){
	getClient().sendPacket(packet);
    }
}
