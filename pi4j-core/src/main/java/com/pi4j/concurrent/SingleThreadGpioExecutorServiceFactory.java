package com.pi4j.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This {@link ExecutorServiceFactory} extends the {@link DefaultExecutorServiceFactory} but changes the GPIO event
 * executor to be single threaded
 */
public class SingleThreadGpioExecutorServiceFactory extends DefaultExecutorServiceFactory {

	private static class GpioEventExecutorServiceHolder {
		static final ExecutorService heldExecutor = Executors
				.newSingleThreadExecutor(getThreadFactory("pi4j-gpio-event-executor-%d"));
	}

	private static ExecutorService getInternalGpioExecutorService() {
		return GpioEventExecutorServiceHolder.heldExecutor;
	}

	private static class GpioEventExecutorServiceWrapperHolder {
		static final ExecutorService heldWrapper = new ShutdownDisabledExecutorWrapper(
				getInternalGpioExecutorService());

	}

	private static ExecutorService getGpioEventExecutorServiceWrapper() {
		return SingleThreadGpioExecutorServiceFactory.GpioEventExecutorServiceWrapperHolder.heldWrapper;
	}

	@Override
	public ExecutorService getGpioEventExecutorService() {
		// we return the protected wrapper to prevent any consumers from
		// being able to shutdown the scheduled executor service
		return getGpioEventExecutorServiceWrapper();
	}
}
