package com.tweakcraft.server.network.sendablePacket;

import com.tweakcraft.server.Config;
import com.tweakcraft.server.network.BaseSendablePacket;
import java.util.logging.Level;

/**
 *
 * @author Meaglin
 */
public class SendHandshake extends BaseSendablePacket {

    @Override
    protected void writeImpl() {
	writeByte(0x02);
	
	if(Config.OFFLINE_MODE)
	    writeString("-");
	else
	    writeString(Config.SERVER_HASH);

	//_log.log(Level.INFO, "Serverh hash:{0}", Config.SERVER_HASH);
        //Moet netter; dit is de 'unieke' serverhash, dient eigenlijk per server gemaakt te worden
        //deze hash is nodig om name verification te ondersteunen
        //Liefst dus eenmalig aanmaken en opslaan, dan ergens uit laden
        //~vistu
    }

}
