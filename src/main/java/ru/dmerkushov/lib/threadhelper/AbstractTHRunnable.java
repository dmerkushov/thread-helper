/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.lib.threadhelper;

/**
 *
 * @author dmerkushov
 */
public abstract class AbstractTHRunnable implements THRunnable {

	Thread thread;
	String threadName;
	private Boolean running = false;
	private Boolean finished = false;
	private final Integer lock = 0;

	public AbstractTHRunnable (String threadName) {
		if (threadName == null) {
			this.threadName = this.getClass ().getSimpleName ();
		} else {
			this.threadName = threadName;
		}
	}

	public AbstractTHRunnable () {
		this (null);
	}

	@Override
	public final void start () throws ThreadHelperException {
		synchronized (lock) {
			if (isRunning ()) {
				throw new ThreadHelperException ("Already running");
			}
			if (isFinished ()) {
				throw new ThreadHelperException ("Already finished");
			}
		}
		thread = new Thread (this);
		thread.setName (getThreadName ());
		thread.start ();
	}

	@Override
	public final void run () {
		synchronized (lock) {
			running = true;
		}

		doSomething ();

		synchronized (lock) {
			running = false;
			finished = true;
		}
	}

	@Override
	public final boolean isRunning () {
		Boolean result;
		synchronized (lock) {
			result = running;
		}
		return result;
	}

	@Override
	public final boolean isFinished () {
		Boolean result;
		synchronized (lock) {
			result = finished;
		}
		return result;
	}

	/**
	 * Get the thread name for this instance
	 *
	 * @return
	 */
	@Override
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
