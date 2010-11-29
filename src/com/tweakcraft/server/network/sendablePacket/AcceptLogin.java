package com.tweakcraft.server.network.sendablePacket;

import com.tweakcraft.server.network.BaseSendablePacket;
import java.io.DataInputStream;

/**
 *
 * @author Meaglin
 */
public class AcceptLogin extends BaseSendablePacket{

    private final int _playerId;
    private final long _mapSeed;
    private final byte _dimension;
    private final String _unk,_unk2;

     public AcceptLogin(int playerId,long mapSeed,byte dimension){
	_playerId = playerId;
	_mapSeed = mapSeed;
	_dimension = dimension;
	_unk = null;
	_unk2 = null;
    }

    public AcceptLogin(int playerId,String unknown,String unknown2,long mapSeed,byte dimension){
	_playerId = playerId;
	_mapSeed = mapSeed;
	_dimension = dimension;
	_unk = unknown;
	_unk2 = unknown2;
    }

    @Override
    protected void writeImpl() {
	writeByte(0x01);
	writeInt(_playerId);
	writeString(_unk);	// Server name ?
	writeString(_unk2);	// Motd ?
	writeLong(_mapSeed);
	writeByte(_dimension);	// 0 = normal word; -1 = hell word;
    }
}
