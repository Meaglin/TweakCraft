package com.tweakcraft.server.network.receivablePacket;

import com.tweakcraft.server.network.BaseReceivablePacket;

/**
 *
 * @author Meaglin
 */
public class RequestLogin extends BaseReceivablePacket {

    private int _protocol;
    private String _username,_password;
    private Long _mapSeed;
    private byte _dimension;
    @Override
    protected void readImpl() {
	_protocol = readInt();
	_username = readString(readShort());
	_password = readString(readShort());
	_mapSeed = readLong();
	_dimension = (byte)readByte();
    }

    @Override
    public void runImpl() {
	getClient().getPlayer().onLoginRequest(_protocol,_username,_password,_mapSeed,_dimension);
    }

}
