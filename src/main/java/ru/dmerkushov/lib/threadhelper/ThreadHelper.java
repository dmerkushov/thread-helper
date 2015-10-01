/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.lib.threadhelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import ru.dmerkushov.lib.threadhelper.collections.ConcurrentLinkedSet;

/**
 *
 * @author dmerkushov
 */
public class ThreadHelper {

	ConcurrentLinkedSet<THRunnable> thRunnables;

	static ThreadHelper instance;

	public static synchronized ThreadHelper getInstance () {
		if (instance == null) {
			instance = new ThreadHelper ();
		}
		return instance;
	}

	private ThreadHelper () {
		thRunnables = new ConcurrentLinkedSet<THRunnable> ();
	}

	public synchronized void addRunnable (THRunnable thRunnable) {
		thRunnables.add (thRunnable);
	}

	public synchronized void removeRunnable (THRunnable thRunnable) {
		thRunnables.remove (thRunnable);
	}

	public synchronized void finish () {
		for (THRunnable thRunnable : thRunnables) {
			thRunnable.finish ();
		}
		for (THRunnable thRunnable : thRunnables) {
			while (thRunnable.isRunning ()) {
			}
		}
	}

	public AbstractTHRunnable getRunnableInstance (Class<? extends AbstractTHRunnable> clazz) throws ThreadHelperException {
		AbstractTHRunnable newInstance;

		Method getInstanceMethod = null;
		try {
			getInstanceMethod = clazz.getMethod ("getInstance");
		} catch (NoSuchMethodException ex) {

		} catch (SecurityException ex) {
			throw new ThreadHelperException (ex);
		}

		if (getInstanceMethod != null) {
			if (!Modifier.isStatic (getInstanceMethod.getModifiers ())) {
				throw new ThreadHelperException ("Method getInstance () is not static");
			}

			try {
				newInstance = (AbstractTHRunnable) getInstanceMethod.invoke (null, null);
			} catch (IllegalAccessException ex) {
				throw new ThreadHelperException (ex);
			} catch (IllegalArgumentException ex) {
				throw new ThreadHelperException (ex);
			} catch (InvocationTargetException ex) {
				throw new ThreadHelperException (ex);
			}

		} else {

			try {
				newInstance = clazz.getConstructor ().newInstance ();
			} catch (NoSuchMethodException ex) {
				throw new ThreadHelperException ("There are neither getInstance() static method nor public constructor with no parameters in class " + clazz.getCanonicalName (), ex);
			} catch (SecurityException ex) {
				throw new ThreadHelperException (ex);
			} catch (InstantiationException ex) {
				throw new ThreadHelperException (ex);
			} catch (IllegalAccessException ex) {
				throw new ThreadHelperException (ex);
			} catch (IllegalArgumentException ex) {
				throw new ThreadHelperException (ex);
			} catch (InvocationTargetException ex) {
				throw new ThreadHelperException (ex);
			}

		}

		ThreadHelper.getInstance ().addRunnable (newInstance);
		return newInstance;
	}
}
