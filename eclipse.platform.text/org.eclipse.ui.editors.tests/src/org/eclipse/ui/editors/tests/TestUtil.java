/*******************************************************************************
 * Copyright (c) 2016 Google, Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Stefan Xenos (Google) - Initial implementation
 *******************************************************************************/
package org.eclipse.ui.editors.tests;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;

import org.eclipse.swt.widgets.Display;

import org.eclipse.core.runtime.jobs.Job;

public class TestUtil {
	/**
	 * Call this in the tearDown method of every test to clean up state that can
	 * otherwise leak through SWT between tests.
	 */
	public static void cleanUp() {
		// Ensure that the Thread.interrupted() flag didn't leak.
		Assert.assertFalse("The main thread should not be interrupted at the end of a test", Thread.interrupted());
		// Wait for any outstanding jobs to finish. Protect against deadlock by
		// terminating the wait after a timeout.
		boolean timedOut = waitForJobs(0, TimeUnit.MINUTES.toMillis(3));
		Assert.assertFalse("Some Job did not terminate at the end of the test", timedOut);
		// Wait for any pending *syncExec calls to finish
		runEventLoop();
		// Ensure that the Thread.interrupted() flag didn't leak.
		Assert.assertFalse("The main thread should not be interrupted at the end of a test", Thread.interrupted());
	}

	/**
	 * Process all queued UI events. If called from background thread, does
	 * nothing.
	 */
	public static void runEventLoop() {
		Display display = Display.getCurrent();
		if (display != null && !display.isDisposed()) {
			while (display.readAndDispatch()) {
				// Keep pumping events until the queue is empty
			}
		}
	}

	/**
	 * Utility for waiting until the execution of jobs of any family has
	 * finished or timeout is reached. If no jobs are running, the method waits
	 * given minimum wait time. While this method is waiting for jobs, UI events
	 * are processed.
	 *
	 * @param minTimeMs
	 *            minimum wait time in milliseconds
	 * @param maxTimeMs
	 *            maximum wait time in milliseconds
	 * @return true if the method timed out, false if all the jobs terminated
	 *         before the timeout
	 */
	public static boolean waitForJobs(long minTimeMs, long maxTimeMs) {
		if (maxTimeMs < minTimeMs) {
			throw new IllegalArgumentException("Max time is smaller as min time!");
		}
		final long start = System.currentTimeMillis();
		while (System.currentTimeMillis() - start < minTimeMs) {
			runEventLoop();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// Uninterruptable
			}
		}
		while (!Job.getJobManager().isIdle()) {
			if (System.currentTimeMillis() - start >= maxTimeMs) {
				return true;
			}
			runEventLoop();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// Uninterruptable
			}
		}
		return false;
	}
}
