
package com.tweakcraft.server.network.receivablePacket;

import com.tweakcraft.server.network.BaseReceivablePacket;
import com.tweakcraft.server.network.sendablePacket.ErrorMessage;

/**
 *
 * @author Meaglin
 */
public class RequestDisconnect extends BaseReceivablePacket {

    private String _string;
    @Override
    protected void readImpl() {
	_string = readString(readShort());

    }

    @Override
    public void runImpl() {
	_log.info("disconnect : " + _string);

	getClient().getConnection().close(new ErrorMessage("bb"));
    }

}
