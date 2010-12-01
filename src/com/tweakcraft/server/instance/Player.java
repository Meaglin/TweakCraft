package com.tweakcraft.server.instance;

import com.tweakcraft.server.Config;
import com.tweakcraft.server.idfactory.IdFactory;
import com.tweakcraft.server.instancemanager.World;
import com.tweakcraft.server.model.Chunk;
import com.tweakcraft.server.model.Inventory;
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
public class Player extends Character {

    private GameClient _client;
    private Inventory _craftTable, _armor, _inventory;

    public Player(GameClient client) {
	//TODO: implement proper id.
	super(IdFactory.getInstance().getNextId());
	setX(0);
	setY(0);
	setZ(128);
	setStance(getZ()+1.5);
	_client = client;
	_craftTable = new Inventory(4);
	_armor = new Inventory(4);
	_inventory = new Inventory(36);
    }

    public Inventory getInventory() {
	return _inventory;
    }

    public Inventory getCraftTable() {
	return _craftTable;
    }

    public Inventory getArmor() {
	return _armor;
    }

    public void updateInventory() {
	sendPacket(new SendInventory(-1, getInventory()));
    }

    public void updateArmor() {
	sendPacket(new SendInventory(-2, getArmor()));
    }

    public void updateCraftTable() {
	sendPacket(new SendInventory(-3, getCraftTable()));
    }

    public synchronized void onLoginRequest(int protocol, String username, String password, Long mapSeed, byte dimension) {
	System.out.println("Login req prot " + protocol + " u:" + username + " p:" + password + " mS:" + mapSeed);

	//sendPacket(new ErrorMessage("Failed to login: User not premium 2"));
	if (!Config.OFFLINE_MODE && !validUser(username))
	    sendPacket(new ErrorMessage("Failed to login: User not premium"));
	else
	    onLogin();
    }

    private boolean validUser(String username) {
	//Okay, user wants to login, we'll have to check whether the user is who claims he is
	String urlStr = Config.LOGIN_HANDLER + "user=" + username + "&serverId=" + Config.SERVER_HASH; //+global_hash;
	//System.out.println("Server req:"+ urlStr);
	String serverResponse = "";
	BufferedReader rd = null;
	InputStreamReader str = null;
	try {
	    URL url = new URL(urlStr);
	    URLConnection conn = url.openConnection();

	    str = new InputStreamReader(conn.getInputStream());
	    rd = new BufferedReader(str);

	    // Get the response
	    serverResponse = rd.readLine();
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    try {
		if (str != null)
		    str.close();
		if (rd != null)
		    rd.close();
	    } catch (Exception e) {
	    }
	}
	if (serverResponse.equals("YES"))
	    return true;
	else
	    return false;
    }

    // this is called when a player wants to login and passed the default validation.
    private void onLogin() {
	World.getInstance().registerPlayer(this);
	sendPacket(new AcceptLogin(getId(), (long) 0, (byte) 0));
	_log.info("sending chunks.");
	Chunk c = World.getInstance().getChunk(this);
	c.registerPlayer(this);
	for(int i = c.getChunkX();i <= c.getChunkX();i++)
	    for(int o = c.getChunkY();o <= c.getChunkY();o++){
		World.getInstance().getChunkByChunkLoc(i, o).registerPlayer(this);
	    }
	_log.info("send spawn.");
	sendPacket(new SendSpawnPosition((int) getX(), (int) getY(), (int) getZ()));
	_log.info("send inventory.");
	updateInventory();
	updateCraftTable();
	updateArmor();
	_log.info("send location.");
	sendPacket(new SendPositionAndLook(this));
    }

    //message = username
    public void onHandShake(String message) {
	sendPacket(new SendHandshake());
    }

    public void onDisconnect() {
	World.getInstance().forgetPlayer(this);
    }

    public GameClient getClient() {
	return _client;
    }

    public void sendPacket(BaseSendablePacket packet) {
	getClient().sendPacket(packet);
    }

    public void onChat(String message) {
	for(Player p : World.getInstance().getPlayers())
	    p.sendPacket(new Chat(message));
    }
}
