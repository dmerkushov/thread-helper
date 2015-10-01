/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.lib.threadhelper;

import java.util.UUID;

/**
 *
 * @author dmerkushov
 */
public abstract class AbstractTHRunnable implements THRunnable {

	Thread thread;
	String threadName;
	private boolean running = false;

	public AbstractTHRunnable (String threadName) {
		if (threadName == null) {
			throw new NullPointerException ("threadName");
		}
		this.threadName = threadName;
	}

	public AbstractTHRunnable () {
		this (UUID.randomUUID ().toString ());
	}

	@Override
	public final void start () {
		thread = new Thread (this);
		thread.setName (threadName);
		thread.start ();
	}

	@Override
	public final void run () {
		running = true;
		doSomething ();
		running = false;
	}

	@Override
	public final boolean isRunning () {
		return running;
	}

	/**
	 * Get the thread name for this instance
	 *
	 * @return
	 */
	public final String getThreadName () {
		return threadName;
	}

	/**
	 * Get the thread for this instance
	 *
	 * @return
	 */
	public final Thread getThread () {
		return thread;
	}

	/**
	 * Implement the business logic here
	 */
	public abstract void doSomething ();

}
