package com.tweakcraft.server;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Meaglin
 */
public class ThreadPoolManager {

    private ThreadPoolExecutor _threadPool;
    private ScheduledThreadPoolExecutor _scheduledThreadPool;
    private static final long MAX_DELAY = Long.MAX_VALUE / 1000000 / 2;

    private ThreadPoolManager() {
	_threadPool = new ThreadPoolExecutor(1, 3, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory("General Pool", Thread.NORM_PRIORITY));
	_scheduledThreadPool = new ScheduledThreadPoolExecutor(1, new PriorityThreadFactory("GeneralSTPool", Thread.NORM_PRIORITY));
    }

    public static long validateDelay(long delay) {
	if (delay < 0)
	    delay = 0;
	else if (delay > MAX_DELAY)
	    delay = MAX_DELAY;
	return delay;
    }

    public ScheduledFuture<?> schedule(Runnable r, long delay) {
	try {
	    delay = ThreadPoolManager.validateDelay(delay);
	    return _scheduledThreadPool.schedule(r, delay, TimeUnit.MILLISECONDS);
	} catch (RejectedExecutionException e) {
	    return null;
	}
    }

    public void execute(Runnable r) {
	_threadPool.execute(r);
    }

    public boolean remove(Runnable r) {
	return _scheduledThreadPool.remove(r);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Runnable r, int initialDelay, int delay) {
	return _scheduledThreadPool.scheduleAtFixedRate(r, initialDelay, delay, TimeUnit.MILLISECONDS);
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

	protected static final ThreadPoolManager _instance = new ThreadPoolManager();
    }

    public static ThreadPoolManager getInstance() {
	return SingletonHolder._instance;
    }

    private class PriorityThreadFactory implements ThreadFactory {

	private int _prio;
	private String _name;
	private AtomicInteger _threadNumber = new AtomicInteger(1);
	private ThreadGroup _group;

	public PriorityThreadFactory(String name, int prio) {
	    _prio = prio;
	    _name = name;
	    _group = new ThreadGroup(_name);
	}

	public Thread newThread(Runnable r) {
	    Thread t = new Thread(_group, r);
	    t.setName(_name + "-" + _threadNumber.getAndIncrement());
	    t.setPriority(_prio);
	    return t;
	}
    }
}
