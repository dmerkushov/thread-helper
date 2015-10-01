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
	 * Is this runnable running right now?
	 *
	 * @return
	 */
	public boolean isRunning ();

	/**
	 * Start this runnable in a new thread
	 */
	public void start ();

	/**
	 * Finish this runnable in a polite manner
	 */
	public void finish ();

}
