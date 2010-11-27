package com.tweakcraft.server.instance;

import com.tweakcraft.server.Config;
import com.tweakcraft.server.network.BaseSendablePacket;
import com.tweakcraft.server.network.GameClient;
import com.tweakcraft.server.network.sendablePacket.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
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

    public void onLoginRequest(int protocol, String username, String password, Long mapSeed, byte dimension) throws MalformedURLException, IOException{
	System.out.println("Login req prot"+protocol+"u:"+username+" p "+password+" mS "+mapSeed );
        //Okay, user wants to login, we'll have to check whether the user is who claims he is
        String urlStr = Config.LOGIN_HANDLER+"user=" + username + "&serverId="+Config.SERVER_HASH; //+global_hash;
        //System.out.println("Server req:"+ urlStr);
        URL url = new URL(urlStr);
        URLConnection conn = url.openConnection ();
        // Get the response
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String serverResponse;
        serverResponse = rd.readLine();
        //System.out.println("Server response:"+ serverResponse+":");
        if(serverResponse.equals("YES")){
            sendPacket(new ErrorMessage("Not supported yet."));
        }else{
            sendPacket(new ErrorMessage("Failed to login: User not premium"));
        }
    }
    // not implemented yet.
    public void onLogin(){

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
