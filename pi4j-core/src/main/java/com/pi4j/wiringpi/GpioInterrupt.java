package com.pi4j.wiringpi;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  GpioInterrupt.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2021 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.util.NativeLibraryLoader;

/**
 * <p>
 * This class provides static methods to configure the native Pi4J library to listen to GPIO
 * interrupts and invoke callbacks into this class. Additionally, this class provides a listener
 * registration allowing Java consumers to subscribe to GPIO pin state changes.
 * </p>
 *
 * <p>
 * Before using the Pi4J library, you need to ensure that the Java VM in configured with access to
 * the following system libraries:
 * <ul>
 * <li>pi4j</li>
 * <li>wiringPi</li>
 * </ul>
 * <blockquote> This library depends on the wiringPi native system library. (developed by
 * Gordon Henderson @ <a href="http://wiringpi.com/">http://wiringpi.com/</a>)
 * </blockquote>
 * </p>
 *
 * @see <a href="https://pi4j.com/">https://pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class GpioInterrupt {

    private static final Object mutex;
    private static final LinkedBlockingQueue<GpioEvent> events;
	private static final List<GpioInterruptListener> listeners;

	private static boolean run;
	private static ExecutorService eventExecutor;
	private static Future<?> eventTask;

	// private constructor
    private GpioInterrupt()  {
        // forbid object construction
    }

    static {
		mutex = new Object();
		events = new LinkedBlockingQueue<>();
		listeners = Collections.synchronizedList(new ArrayList<>());

        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so", "pi4j");
    }

    /**
     * <p>
     * This method is used to instruct the native code to setup a monitoring thread to monitor
     * interrupts that represent changes to the selected GPIO pin.
     * </p>
     *
     * <p>
     * <b>The GPIO pin must first be exported before it can be monitored.</b>
     * </p>
     *
     * @param pin GPIO pin number (not header pin number; not wiringPi pin number)
     * @return A return value of a negative number represents an error. A return value of '0'
     *         represents success and that the GPIO pin is already being monitored. A return value
     *         of '1' represents success and that a new monitoring thread was created to handle the
     *         requested GPIO pin number.
     */
    public static native int enablePinStateChangeCallback(int pin);

    /**
     * <p>
     * This method is used to instruct the native code to stop the monitoring thread monitoring
     * interrupts on the selected GPIO pin.
     * </p>
     *
     * @param pin GPIO pin number (not header pin number; not wiringPi pin number)

     * @return A return value of a negative number represents an error. A return value of '0'
     *         represents success and that no existing monitor was previously running. A return
     *         value of '1' represents success and that an existing monitoring thread was stopped
     *         for the requested GPIO pin number.
     */
    public static native int disablePinStateChangeCallback(int pin);

    /**
     * <p>
     * This method is provided as the callback handler for the Pi4J native library to invoke when a
     * GPIO interrupt is detected. This method should not be called from any Java consumers. (Thus
     * is is marked as a private method.)
     * </p>
     *
     * @param pin GPIO pin number (not header pin number; not wiringPi pin number)
     * @param state New GPIO pin state.
     */
    private static void pinStateChangeCallback(int pin, boolean state) {
		synchronized (mutex) {
			events.add(new GpioEvent(pin, state));
		}
    }

    /**
     * <p>
     * Java consumer code can call this method to register itself as a listener for pin state
     * changes.
     * </p>
     *
     * @see com.pi4j.wiringpi.GpioInterruptListener
     * @see com.pi4j.wiringpi.GpioInterruptEvent
     *
     * @param listener A class instance that implements the GpioInterruptListener interface.
     */
    public static void addListener(GpioInterruptListener listener) {
    	synchronized (mutex) {
			if (!listeners.contains(listener)) {
				listeners.add(listener);

				if (!run)
					enableEventExecutor();
			}
		}
    }

	/**
     * <p>
     * Java consumer code can all this method to unregister itself as a listener for pin state
     * changes.
     * </p>
     *
     * @see com.pi4j.wiringpi.GpioInterruptListener
     * @see com.pi4j.wiringpi.GpioInterruptEvent
     *
     * @param listener A class instance that implements the GpioInterruptListener interface.
     */
    public static void removeListener(GpioInterruptListener listener) {
		synchronized (mutex) {
			listeners.remove(listener);

			if (run && listeners.isEmpty())
				disableEventExecutor();
		}
    }

	/**
     * <p>
     * Returns true if the listener is already registered for event callbacks.
     * </p>
     *
     * @see com.pi4j.wiringpi.GpioInterruptListener
     * @see com.pi4j.wiringpi.GpioInterruptEvent
     *
     * @param listener A class instance that implements the GpioInterruptListener interface.
     */
    public static boolean hasListener(GpioInterruptListener listener) {
		synchronized (mutex) {
			return listeners.contains(listener);
		}
    }

	private static synchronized void enableEventExecutor() {
		if (!run) {
			run = true;
			if (eventExecutor == null)
				eventExecutor = GpioFactory.getExecutorServiceFactory().getEventExecutorService();
			eventTask = eventExecutor.submit(GpioInterrupt::handleEvents);
		}
	}

	private static void disableEventExecutor() {
		if (run) {
			run = false;
			if (eventTask != null)
				eventTask.cancel(true);
		}
	}

	public static void shutdown() {
		disableEventExecutor();
	}

	private static void handleEvents() {
		while (run) {
			try {
				GpioEvent event = events.take();

				List<GpioInterruptListener> listenersClone;
				synchronized (mutex) {
					listenersClone = new ArrayList<>(listeners);
				}

				for (GpioInterruptListener listener : listenersClone) {
					GpioInterruptEvent interruptEvent = new GpioInterruptEvent(listener, event.pin, event.state);
					listener.pinStateChange(interruptEvent);
				}
			} catch (InterruptedException e) {
				if (!run) {
					return;
				}
			}
		}
	}

    public static class GpioEvent {
        private final int pin;
        private final boolean state;

        public GpioEvent(int pin, boolean state) {
            this.pin = pin;
            this.state = state;
        }
    }
}
