/*
 * $Header: BitSetIDFactory.java, 24/08/2005 22:44:26 luisantonioa Exp $
 *
 * $Author: luisantonioa $
 * $Date: 24/08/2005 22:44:26 $
 * $Revision: 1 $
 * $Log: BitSetIDFactory.java,v $
 * Revision 1  24/08/2005 22:44:26  luisantonioa
 * Added copyright notice
 *
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.tweakcraft.server.idfactory;

import com.tweakcraft.server.ThreadPoolManager;
import java.util.BitSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import gnu.trove.PrimeFinder;


/**
 * This class ..
 *
 * @version $Revision: 1.2 $ $Date: 2004/06/27 08:12:59 $
 */

public class BitSetIDFactory extends IdFactory
{
	private static Logger _log = Logger.getLogger(BitSetIDFactory.class.getName());
	
	private BitSet _freeIds;
	private AtomicInteger _freeIdCount;
	private AtomicInteger _nextFreeId;
	
	protected class BitSetCapacityCheck implements Runnable
	{
		public void run()
		{
			synchronized (BitSetIDFactory.this)
			{
				if (reachingBitSetCapacity())
					increaseBitSetCapacity();
			}
		}
		
	}
	
	protected BitSetIDFactory()
	{
		super();
		
		synchronized(BitSetIDFactory.class)
		{
			ThreadPoolManager.getInstance().scheduleAtFixedRate(new BitSetCapacityCheck(), 30000, 30000);
			initialize();
		}
		_log.info("IDFactory: " + _freeIds.size() + " id's available.");
	}
	
	public void initialize()
	{
		try
		{
			_freeIds = new BitSet(PrimeFinder.nextPrime(100000));
			_freeIds.clear();
			_freeIdCount = new AtomicInteger(FREE_OBJECT_ID_SIZE);
			_nextFreeId = new AtomicInteger(FIRST_OID);
			_initialized = true;
		}
		catch (Exception e)
		{
			_initialized = false;
			_log.log(Level.SEVERE, "BitSet ID Factory could not be initialized correctly: " + e.getMessage(), e);
		}
	}
	
	/**
	 * 
	 * @see com.l2jserver.gameserver.idfactory.IdFactory#releaseId(int)
	 */
	@Override
	public synchronized void releaseId(int objectID)
	{
		if ((objectID - FIRST_OID) > -1)
		{
			_freeIds.clear(objectID - FIRST_OID);
			_freeIdCount.incrementAndGet();
		}
		else
			_log.warning("BitSet ID Factory: release objectID " + objectID + " failed (< " + FIRST_OID + ")");
	}
	
	/**
	 * 
	 * @see com.l2jserver.gameserver.idfactory.IdFactory#getNextId()
	 */
	@Override
	public synchronized int getNextId()
	{
		int newID = _nextFreeId.get();
		_freeIds.set(newID);
		_freeIdCount.decrementAndGet();
		
		int nextFree = _freeIds.nextClearBit(newID);
		
		if (nextFree < 0)
		{
			nextFree = _freeIds.nextClearBit(0);
		}
		if (nextFree < 0)
		{
			if (_freeIds.size() < FREE_OBJECT_ID_SIZE)
			{
				increaseBitSetCapacity();
			}
			else
			{
				throw new NullPointerException("Ran out of valid Id's.");
			}
		}
		
		_nextFreeId.set(nextFree);
		
		return newID + FIRST_OID;
	}
	
	/**
	 * 
	 * @see com.l2jserver.gameserver.idfactory.IdFactory#size()
	 */
	@Override
	public synchronized int size()
	{
		return _freeIdCount.get();
	}
	
	/**
	 * 
	 * @return
	 */
	protected synchronized int usedIdCount()
	{
		return (size() - FIRST_OID);
	}
	
	/**
	 * 
	 * @return
	 */
	protected synchronized boolean reachingBitSetCapacity()
	{
		return PrimeFinder.nextPrime(usedIdCount() * 11 / 10) > _freeIds.size();
	}
	
	protected synchronized void increaseBitSetCapacity()
	{
		BitSet newBitSet = new BitSet(PrimeFinder.nextPrime(usedIdCount() * 11 / 10));
		newBitSet.or(_freeIds);
		_freeIds = newBitSet;
	}
}
