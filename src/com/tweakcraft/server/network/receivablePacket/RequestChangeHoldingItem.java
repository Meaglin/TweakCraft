
package com.tweakcraft.server.network.receivablePacket;

import com.tweakcraft.server.network.BaseReceivablePacket;

/**
 *
 * @author Meaglin
 */
public class RequestChangeHoldingItem extends BaseReceivablePacket{

    private int _unused,_itemId;

    @Override
    protected void readImpl() {
	_unused = readInt();
	_itemId = readShort();
    }

    @Override
    public void runImpl() {
	//TODO: Implement.
    }

}
