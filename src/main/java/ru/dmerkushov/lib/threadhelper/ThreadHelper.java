/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.lib.threadhelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Timer;
import java.util.TimerTask;
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

	public synchronized AbstractTHRunnable addRunnable (Class<? extends AbstractTHRunnable> clazz) throws ThreadHelperException {
		AbstractTHRunnable thRunnable = getRunnableInstance (clazz);
		addRunnable (thRunnable);
		return thRunnable;
	}

	public synchronized void removeRunnable (THRunnable thRunnable) {
		thRunnables.remove (thRunnable);
	}

	public synchronized void finish (final long timeout) throws ThreadHelperException {
		Runnable finishRunnable = new Runnable () {

			@Override
			public void run () {
				THRunnable[] thRunnablesArray = thRunnables.toArray (new THRunnable[0]);

				final Timer timer = new Timer ();

				for (final THRunnable thRunnable : thRunnablesArray) {
					try {
						thRunnable.finish ();
						System.err.println ("ThreadHelper library: " + thRunnable.getThreadName () + " - finish() called with timeout: " + timeout + " millis");
					} catch (ThreadHelperException ex) {
						ex.printStackTrace (System.err);
					}

					if (timeout > 0l) {
						TimerTask timerTask = new TimerTask () {

							@Override
							public void run () {
								boolean running;
								try {
									running = thRunnable.isRunning ();
								} catch (ThreadHelperException ex) {
									ex.printStackTrace (System.err);
									return;
								}
								if (running) {
									String threadName = null;
									try {
										threadName = thRunnable.getThreadName ();
									} catch (ThreadHelperException ex) {
										ex.printStackTrace (System.err);
									}
									System.err.println ("ThreadHelper library: WARNING: THRunnable not finished: " + thRunnable.toString () + ", thread name (may be null in case of error): " + threadName + ", timeout expired: " + timeout + " millis");
								}
							}

						};

						timer.schedule (timerTask, timeout);
					}

				}
			}
		};

		Thread finishThread = new Thread (finishRunnable);
		finishThread.setName ("THREADHELPER_FINISHER");
		finishThread.start ();
	}

	public synchronized void finish () throws ThreadHelperException {
		finish (0);
	}

	private AbstractTHRunnable getRunnableInstance (Class<? extends AbstractTHRunnable> clazz) throws ThreadHelperException {
		AbstractTHRunnable newInstance;

		Method getInstanceMethod = null;
		try {
			getInstanceMethod = clazz.getMethod ("getInstance");
		} catch (NoSuchMethodException ex) {
			// Do nothing, as we may call a constructor later
		} catch (SecurityException ex) {
			throw new ThreadHelperException (ex);
		}

		if (getInstanceMethod != null && getInstanceMethod.getParameterTypes ().length > 0) {
			getInstanceMethod = null;
		}

		if (getInstanceMethod != null) {
			if (!Modifier.isStatic (getInstanceMethod.getModifiers ())) {
				throw new ThreadHelperException ("Method getInstance () of class " + clazz.getCanonicalName () + " is not static");
			}

			try {
				newInstance = (AbstractTHRunnable) getInstanceMethod.invoke (null, new Object[0]);
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
