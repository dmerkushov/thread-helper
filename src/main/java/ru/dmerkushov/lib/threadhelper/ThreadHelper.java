/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.dmerkushov.lib.threadhelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author dmerkushov
 */
public class ThreadHelper {

	LinkedHashMap<String, Set<THRunnable>> thRunnables;

	public static final String DEFAULT_GROUP_NAME = "default";

	static ThreadHelper instance;

	public static synchronized ThreadHelper getInstance () {
		if (instance == null) {
			instance = new ThreadHelper ();
		}
		return instance;
	}

	private ThreadHelper () {
		thRunnables = new LinkedHashMap<String, Set<THRunnable>> ();
		thRunnables.put (DEFAULT_GROUP_NAME, new HashSet<THRunnable> ());
	}

	public synchronized void addRunnable (THRunnable thRunnable) {
		addRunnable (DEFAULT_GROUP_NAME, thRunnable);
	}

	public synchronized void addRunnable (String groupName, THRunnable thRunnable) {
		if (groupName == null) {
			throw new IllegalArgumentException ("groupName is null");
		}
		if (thRunnable == null) {
			throw new IllegalArgumentException ("thRunnable is null");
		}

		if (!thRunnables.containsKey (groupName)) {
			thRunnables.put (groupName, new HashSet<THRunnable> ());
		}

		thRunnables.get (groupName).add (thRunnable);
	}

	public synchronized AbstractTHRunnable addRunnable (Class<? extends AbstractTHRunnable> clazz) throws ThreadHelperException {
		return addRunnable (DEFAULT_GROUP_NAME, clazz);
	}

	public synchronized AbstractTHRunnable addRunnable (String groupName, Class<? extends AbstractTHRunnable> clazz) throws ThreadHelperException {
		if (groupName == null) {
			throw new IllegalArgumentException ("groupName is null");
		}
		if (clazz == null) {
			throw new IllegalArgumentException ("clazz is null");
		}

		AbstractTHRunnable thRunnable = getRunnableInstance (clazz);
		addRunnable (groupName, thRunnable);
		return thRunnable;
	}

	public synchronized void removeRunnable (THRunnable thRunnable) throws ThreadHelperException {
		removeRunnable (DEFAULT_GROUP_NAME, thRunnable);
	}

	public synchronized void removeRunnable (String groupName, THRunnable thRunnable) throws ThreadHelperException {
		if (groupName == null) {
			throw new IllegalArgumentException ("groupName is null");
		}
		if (thRunnable == null) {
			throw new IllegalArgumentException ("thRunnable is null");
		}

		if (!thRunnables.containsKey (groupName)) {
			throw new ThreadHelperException ("No such group: " + groupName);
		}

		thRunnables.get (groupName).remove (thRunnable);
	}

	public synchronized void finish () throws ThreadHelperException {
		finish (0);
	}

	public synchronized void finish (final long timeout) throws ThreadHelperException {
		finish (DEFAULT_GROUP_NAME, timeout);
	}

	public synchronized void finish (final String groupName, final long timeout) throws ThreadHelperException {
		if (groupName == null) {
			throw new IllegalArgumentException ("groupName is null");
		}

		final Timer finishTimer = new Timer ();

		Runnable finishRunnable = new Runnable () {

			@Override
			public void run () {
				THRunnable[] thRunnablesArray = thRunnables.get (groupName).toArray (new THRunnable[0]);

				for (final THRunnable thRunnable : thRunnablesArray) {
					Thread finishT = new Thread (new Runnable () {

						@Override
						public void run () {
							try {
								thRunnable.finish ();
							} catch (ThreadHelperException ex) {
								ex.printStackTrace (System.err);
							}
						}
					});
					String threadName = "";
					try {
						threadName = thRunnable.getThreadName ();
					} catch (ThreadHelperException ex) {
						System.err.println ("ThreadHelper library: WARNING: Could not get THRunnable's name. Will use the default: UNNAMED_THRUNNABLE");
						ex.printStackTrace (System.err);
						threadName = "UNNAMED_THRUNNABLE";
					}
					finishT.setName ("THREADHELPER_" + threadName + "_FINISHER");
					finishT.start ();

					System.err.println ("ThreadHelper library: " + threadName + " - finish() called with timeout: " + timeout + " millis on group: " + groupName);

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
									System.err.println ("ThreadHelper library: WARNING: THRunnable not finished: " + thRunnable.toString () + ", thread name (may be null in case of error): " + threadName + ", timeout expired: " + timeout + " millis, group: " + groupName);
								}
							}

						};

						finishTimer.schedule (timerTask, timeout);
					}

				}

				if (timeout > 0l) {
					try {
						Thread.sleep (timeout * 2);
					} catch (InterruptedException ex) {
					}

					finishTimer.cancel ();
				}
			}
		};

		Thread finishThread = new Thread (finishRunnable);
		finishThread.setName ("THREADHELPER_GLOBAL_FINISHER");
		finishThread.start ();
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
