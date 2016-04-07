/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.lib.threadhelper;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 *
 * @author dmerkushov
 */
public abstract class AbstractTHRunnable implements THRunnable {

	/**
	 * The thread itself
	 */
	Thread thread;
	/**
	 * Thread name
	 */
	String threadName;
	/**
	 * True only if this thread is running right now
	 */
	private Boolean running = false;
	/**
	 * True only if this thread had worked and finished
	 */
	private Boolean finished = false;
	/**
	 * Read lock for the status information
	 */
	private ReadLock statusR;
	/**
	 * Write lock for the status information
	 */
	private WriteLock statusW;

	public AbstractTHRunnable (String threadName) {
		if (threadName == null) {
			this.threadName = this.getClass ().getSimpleName ();
		} else {
			this.threadName = threadName;
		}

		ReentrantReadWriteLock statusRrwl = new ReentrantReadWriteLock ();
		statusR = statusRrwl.readLock ();
		statusW = statusRrwl.writeLock ();
	}

	public AbstractTHRunnable () {
		this (null);
	}

	@Override
	public final void start () throws ThreadHelperException {

		System.err.println ("ThreadHelper library: " + this.getThreadName () + " - start() called");

		statusR.lock ();
		try {
			if (isRunning ()) {
				throw new ThreadHelperException ("Already running");
			}
			if (isFinished ()) {
				throw new ThreadHelperException ("Already finished");
			}
		} finally {
			statusR.unlock ();
		}

		thread = new Thread (this);
		thread.setDaemon (true);
		thread.setName (getThreadName ());
		thread.start ();
	}

	@Override
	public final void run () {
		statusW.lock ();
		try {
			running = true;
		} finally {
			statusW.unlock ();
		}

		doSomething ();

		statusW.lock ();
		try {
			running = false;
			finished = true;
		} finally {
			statusW.unlock ();
		}
	}

	@Override
	public final boolean isRunning () {
		Boolean localRunning;
		statusR.lock ();
		try {
			localRunning = running || (thread != null && thread.isAlive ());
		} finally {
			statusR.unlock ();
		}

		return localRunning;
	}

	@Override
	public final boolean isFinished () {
		Boolean localFinished;
		statusR.lock ();
		try {
			localFinished = finished;
		} finally {
			statusR.unlock ();
		}
		return localFinished;
	}

	/**
	 * Tests whether this instance's thread has been interrupted. The
	 * <i>interrupted status</i> of the thread is unaffected by this method.
	 *
	 * <p>
	 * A thread interruption ignored because a thread was not alive at the time
	 * of the interrupt will be reflected by this method returning false.
	 *
	 * @return  <code>true</code> if this thread has been interrupted;
	 * <code>false</code> otherwise.
	 */
	public final boolean isInterrupted () {
		Boolean localInterrupted;
		statusR.lock ();
		try {
			localInterrupted = thread.isInterrupted ();
		} finally {
			statusR.unlock ();
		}
		return localInterrupted;
	}

	/**
	 * Get the thread name for this instance
	 *
	 * @return
	 */
	@Override
	public String getThreadName () {
		return threadName;
	}

	@Override
	public String toString () {
		boolean localRunning;
		boolean localFinished;
		statusR.lock ();
		try {
			localRunning = running;
			localFinished = finished;
		} finally {
			statusR.unlock ();
		}

		StringBuilder sb = new StringBuilder ();
		sb.append (getClass ().getName ());
		sb.append (" -> ").append (AbstractTHRunnable.class.getName ()).append (" {threadName=\"").append (threadName)
				.append ("\", hashCode=").append (Integer.toHexString (hashCode ()))
				.append (", running=").append (localRunning)
				.append (", finished=").append (localFinished).append ("}");
		return sb.toString ();
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
