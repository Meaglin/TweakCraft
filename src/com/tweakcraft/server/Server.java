package com.tweakcraft.server;

import com.tweakcraft.server.network.GameClient;
import com.tweakcraft.server.network.GamePacketHandler;
import com.tweakcraft.server.network.IPv4Filter;
import java.io.IOException;
import org.mmocore.network.SelectorConfig;
import org.mmocore.network.SelectorThread;

/**
 *
 * @author Meaglin
 */
public class Server {

    private static Server _server;
    private SelectorThread<?> _selectorThread;

    public static void main(final String[] args) throws Exception {
	_server = new Server(args);
    }

    public Server(final String[] args) throws IOException {

	final SelectorConfig sc = new SelectorConfig();
	sc.MAX_READ_PER_PASS = 12;
	sc.MAX_SEND_PER_PASS = 12;
	sc.SLEEP_TIME = 20;
	sc.HELPER_BUFFER_COUNT = 20;

	final GamePacketHandler gph = new GamePacketHandler();
	_selectorThread = new SelectorThread<GameClient>(sc, gph, gph, gph, new IPv4Filter());
	try {
	    _selectorThread.openServerSocket(null, Config.PORT);
	} catch (IOException e) {
	    System.out.println("FATAL: Failed to open server socket. Reason: " + e.getMessage());

	    System.exit(1);
	}
	_selectorThread.start();
	System.out.println("Listening for players on port 1111.");
    }
}
