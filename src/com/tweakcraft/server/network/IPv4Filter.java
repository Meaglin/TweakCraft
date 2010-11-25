package com.tweakcraft.server.network;

import java.net.InetAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.mmocore.network.IAcceptFilter;

public class IPv4Filter implements IAcceptFilter, Runnable
{
	private HashMap<Integer, Flood> _ipFloodMap;
	private static final long SLEEP_TIME = 5000;
	
	public IPv4Filter()
	{
		_ipFloodMap = new HashMap<Integer, Flood>();
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}
	/**
	 * 
	 * @param ip
	 * @return
	 */
	private static final int hash(byte[] ip)
	{
		return ip[0] & 0xFF | ip[1] << 8 & 0xFF00 | ip[2] << 16 & 0xFF0000 | ip[3] << 24 & 0xFF000000;
	}
	
	protected static final class Flood
	{
		long lastAccess;
		int trys;
		
		Flood()
		{
			lastAccess = System.currentTimeMillis();
			trys = 0;
		}
	}
	
	@Override
	public boolean accept(SocketChannel sc)
	{
		InetAddress addr = sc.socket().getInetAddress();
		int h = hash(addr.getAddress());
		
		long current = System.currentTimeMillis();
		Flood f;
		synchronized (_ipFloodMap)
		{
			f = _ipFloodMap.get(h);
		}
		if (f != null)
		{
			if (f.trys == -1)
			{
				f.lastAccess = current;
				return false;
			}
			
			if (f.lastAccess + 1000 > current)
			{
				f.lastAccess = current;
				
				if (f.trys >= 3)
				{
					f.trys = -1;
					return false;
				}
				
				f.trys++;
			}
			else
			{
				f.lastAccess = current;
			}
		}
		else
		{
			synchronized (_ipFloodMap)
			{
				_ipFloodMap.put(h, new Flood());
			}
		}
		
		return true;
	}

	@Override
	public void run()
	{
		while (true)
		{
			long reference = System.currentTimeMillis() - (1000 * 300);
			ArrayList<Integer> toRemove = new ArrayList<Integer>(50);
			
			synchronized (_ipFloodMap)
			{
				for (Entry<Integer, Flood> e : _ipFloodMap.entrySet())
				{
					Flood f = e.getValue();
					if (f.lastAccess < reference)
						toRemove.add(e.getKey());
				}
			}
			
			synchronized (_ipFloodMap)
			{
				for (Integer i : toRemove)
				{
					_ipFloodMap.remove(i);
				}
			}
			
			try
			{
				Thread.sleep(SLEEP_TIME);
			}
			catch (InterruptedException e)
			{
				
			}
		}
	}
	
}