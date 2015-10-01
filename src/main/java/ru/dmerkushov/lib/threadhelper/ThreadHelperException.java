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
public class ThreadHelperException extends Exception {

	/**
	 * Creates a new instance of <code>ThreadHelperException</code> without
	 * detail message.
	 */
	public ThreadHelperException () {
	}

	/**
	 * Constructs an instance of <code>ThreadHelperException</code> with the
	 * specified detail message.
	 *
	 * @param msg the detail message.
	 */
	public ThreadHelperException (String msg) {
		super (msg);
	}

	public ThreadHelperException (String message, Throwable cause) {
		super (message, cause);
	}

	public ThreadHelperException (Throwable cause) {
		super (cause);
	}

}
