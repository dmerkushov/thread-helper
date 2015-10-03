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
public interface THRunnable extends Runnable {

	/**
	 * Get the name for this runnable
	 *
	 * @return
	 * @throws ThreadHelperException
	 */
	public String getThreadName () throws ThreadHelperException;

	/**
	 * Is this runnable running right now?
	 *
	 * @return
	 * @throws ThreadHelperException
	 */
	public boolean isRunning () throws ThreadHelperException;

	/**
	 * Start this runnable in a new thread
	 * @throws ThreadHelperException
	 */
	public void start () throws ThreadHelperException;

	/**
	 * Finish this runnable in a polite manner
	 * @throws ThreadHelperException
	 */
	public void finish () throws ThreadHelperException;

	/**
	 * Is this runnable already finished?
	 *
	 * @return
	 * @throws ThreadHelperException
	 */
	public boolean isFinished () throws ThreadHelperException;

}
