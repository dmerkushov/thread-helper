/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.lib.threadhelper;

/**
 * A Runnable acceptable for ThreadHelper. All the methods, except {@link THRunnable#start()
 * }, should be thread-safe.
 *
 * @author dmerkushov
 * @see Runnable
 */
public interface THRunnable extends Runnable {

	/**
	 * Get the name for this thread. This method should be thread-safe and
	 * always return.
	 *
	 * @return
	 * @throws ThreadHelperException
	 */
	public String getThreadName () throws ThreadHelperException;

	/**
	 * Returns true if and only if this thread is running. This method should be
	 * thread-safe.
	 *
	 * @return
	 * @throws ThreadHelperException
	 */
	public boolean isRunning () throws ThreadHelperException;

	/**
	 * Start this runnable. This method should only be called once.
	 *
	 * @throws ThreadHelperException
	 */
	public void start () throws ThreadHelperException;

	/**
	 * Finish this runnable in a polite manner. This method should be
	 * thread-safe.
	 *
	 * @throws ThreadHelperException
	 */
	public void finish () throws ThreadHelperException;

	/**
	 * Returns true if and only if this thread has run and finished in a polite
	 * manner. This method should be thread-safe.
	 *
	 * @return
	 * @throws ThreadHelperException
	 */
	public boolean isFinished () throws ThreadHelperException;

}
