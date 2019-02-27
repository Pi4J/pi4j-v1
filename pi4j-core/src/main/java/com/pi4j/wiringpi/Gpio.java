package com.pi4j.wiringpi;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  Gpio.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */


import com.pi4j.util.NativeLibraryLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * <[>WiringPi GPIO Control</[>
 *
 * <p>
 * Some of the functions in the WiringPi library are designed to mimic those in the Arduino Wiring
 * system. There are relatively easy to use and should present no problems for anyone used to the
 * Arduino system, or C programming in-general.
 * </p>
 *
 * <p>
 * The main difference is that unlike the Arduino system, the main loop of the program is not
 * provided for you and you need to write it yourself. This is often desirable in a Linux system
 * anyway as it can give you access to command-line arguments and so on. See the examples page for
 * some simple examples and a Makefile to use.
 * </p>
 *
 * <p>
 * Before using the Pi4J library, you need to ensure that the Java VM in configured with access to
 * the following system libraries:
 * <ul>
 * <li>pi4j</li>
 * <li>wiringPi</li>
 * </ul>
 * <blockquote> This library depends on the wiringPi native system library.</br> (developed by
 * Gordon Henderson @ <a href="http://wiringpi.com/">http://wiringpi.com/</a>)
 * </blockquote>
 * </p>
 *
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @see <a
 *      href="http://wiringpi.com/reference/">http://wiringpi.com/reference/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class Gpio {

    // private constructor
    private Gpio()  {
        // forbid object construction
    }

    /**
     * The total number of GPIO pins available in the WiringPi library.
     * <i>(Note this is not the maximum pin count on the Pi GPIO header.)</i>
     */
    public static final int NUM_PINS = 46;

    /**
     * GPIO pin constant for INPUT direction for reading pin states
     *
     * @see #pinMode(int,int)
     */
    public static final int INPUT = 0;

    /**
     * GPIO pin constant for OUTPUT direction for writing digital pin states (0/1)
     *
     * @see #pinMode(int,int)
     */
    public static final int OUTPUT = 1;

    /**
     * GPIO pin constant for PWM_OUTPUT direction for writing analog pin states
     *
     * @see #pinMode(int,int)
     */
    public static final int PWM_OUTPUT = 2;

    /**
     * GPIO pin constant for GPIO_CLOCK pin mode
     *
     * @see #pinMode(int,int)
     */
    public static final int GPIO_CLOCK = 3;

    /**
     * GPIO pin state constant for LOW/OFF/0VDC
     *
     * @see #digitalWrite(int,int)
     */
    public static final int LOW = 0;

    /**
     * GPIO pin state constant for HIGH/ON/+3.3VDC
     *
     * @see #digitalWrite(int,int)
     */
    public static final int HIGH = 1;

    /**
     * GPIO constant to disable the pull-up or pull-down resistor mode on a GPIO pin.
     *
     * @see #waitForInterrupt(int,int)
     */
    public static final int PUD_OFF = 0;

    /**
     * GPIO constant to enable the pull-down resistor mode on a GPIO pin.
     *
     * @see #waitForInterrupt(int,int)
     */
    public static final int PUD_DOWN = 1;

    /**
     * GPIO constant to enable the pull-up resistor mode on a GPIO pin.
     *
     * @see #waitForInterrupt(int,int)
     */
    public static final int PUD_UP = 2;


    /**
     * GPIO constant to define PWM balanced mode.
     *
     * @see #pwmSetMode(int)
     */
    public static final int PWM_MODE_BAL = 1;


    /**
     * GPIO constant to define PWM mark:space mode.
     *
     * @see #pwmSetMode(int)
     */
    public static final int PWM_MODE_MS = 0;


    /**
     * GPIO constant to define pin ALT modes
     *
     * @see #pinModeAlt(int,int)
     */
    public static final int ALT0 = 4;
    public static final int ALT1 = 5;
    public static final int ALT2 = 6;
    public static final int ALT3 = 7;
    public static final int ALT4 = 3;
    public static final int ALT5 = 2;

    /**
     * GPIO constants to define interrupt levels
     *
     * @see #wiringPiISR(int,int,com.pi4j.wiringpi.GpioInterruptCallback)
     */
    public static final int INT_EDGE_SETUP = 0;
    public static final int INT_EDGE_FALLING = 1;
    public static final int INT_EDGE_RISING = 2;
    public static final int INT_EDGE_BOTH = 3;

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }

    /**
     * <p>Setup Functions</p>
     *
     * <p>
     * This initializes the wiringPi system and assumes that the calling program is going to be
     * using the wiringPi pin numbering scheme. This is a simplified numbering scheme which provides
     * a mapping from virtual pin numbers 0 through 16 to the real underlying Broadcom GPIO pin
     * numbers. See the pins page for a table which maps the wiringPi pin number to the Broadcom
     * GPIO pin number to the physical location on the edge connector.
     * </p>
     *
     * <p><b><i>This function needs to be called with root privileges.</i></b></p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/setup/">http://wiringpi.com/reference/setup/</a>
     * @return If this function returns a value of '-1' then an error has occurred and the
     *         initialization of the GPIO has failed. A return value of '0' indicates a successful
     *         GPIO initialization.
     */
    public static native int wiringPiSetup();

    /**
     * <p>Setup Functions</p>
     *
     * <p>
     * This initializes the wiringPi system but uses the /sys/class/gpio interface rather than
     * accessing the hardware directly. This can be called as a non-root user provided the GPIO pins
     * have been exported before-hand using the gpio program. Pin number in this mode is the native
     * Broadcom GPIO numbers.
     * </p>
     *
     * <p>
     * <ul>
     * Note:
     * </ul>
     * In this mode you can only use the pins which have been exported via the /sys/class/gpio
     * interface. You must export these pins before you call your program. You can do this in a
     * separate shell-script, or by using the system() function from inside your program.
     * </p>
     *
     * <p>
     * <b><i>Also note that some functions (noted below) have no effect when using this mode as
     * they're not currently possible to action unless called with root privileges.</i></b>
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/setup/">http://wiringpi.com/reference/setup/</a>
     * @return If this function returns a value of '-1' then an error has occurred and the
     *         initialization of the GPIO has failed. A return value of '0' indicates a successful
     *         GPIO initialization.
     */
    public static native int wiringPiSetupSys();

    /**
     * <p>Setup Functions</p>
     *
     * <p>
     * This setup function is identical to wiringPiSetup(), however it allows the calling programs
     * to use the Broadcom GPIO pin numbers directly with no re-mapping.
     * </p>
     *
     * <p> <b><i>This function needs to be called with root privileges.</i></b></p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/setup/">http://wiringpi.com/reference/setup/</a>
     * @return If this function returns a value of '-1' then an error has occurred and the
     *         initialization of the GPIO has failed. A return value of '0' indicates a successful
     *         GPIO initialization.
     */
    public static native int wiringPiSetupGpio();


    /**
     * <p>Setup Functions</p>
     *
     * <p>
     * This setup function is identical to wiringPiSetup(), however it allows the calling programs
     * to use the physical header pin numbers on the board GPIO header.
     * </p>
     *
     * <p> <b><i>This function needs to be called with root privileges.</i></b></p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/setup/">http://wiringpi.com/reference/setup/</a>
     * @return If this function returns a value of '-1' then an error has occurred and the
     *         initialization of the GPIO has failed. A return value of '0' indicates a successful
     *         GPIO initialization.
     */
    public static native int wiringPiSetupPhys();


    /**
     * <p>Core Functions</p>
     *
     * <p>
     * This sets the mode of a pin to either INPUT, OUTPUT, PWM_OUTPUT or GPIO_CLOCK. Note that only wiringPi pin 1
     * (BCM_GPIO 18) supports PWM output and only wiringPi pin 7 (BCM_GPIO 4) supports CLOCK output modes.
     * </p>
     *
     * <p> <b><i>This function has no effect when in Sys mode.</i></b></p>
     *
     * @see #INPUT
     * @see #OUTPUT
     * @see #PWM_OUTPUT
     * @see <a
     *      href="http://wiringpi.com/reference/core-functions/">http://wiringpi.com/reference/core-functions/</a>
     * @param pin The GPIO pin number. </br><i>(Depending on how wiringPi was initialized, this may
     *            be the wiringPi pin number, the Broadcom GPIO pin number, or the board header pin number.)</i>
     * @param mode  Pin mode/direction to apply to the selected pin.</br>The following constants are
     *            provided for use with this parameter:
     *            <ul>
     *            <li>INPUT</li>
     *            <li>OUTPUT</li>
     *            <li>PWM_OUTPUT</li>
     *            <li>GPIO_CLOCK</li>
     *            </ul>
     */
    public static native void pinMode(int pin, int mode);


    /**
     * <p>Core Functions</p>
     *
     * <p>
     * This method is an undocumented method in the WiringPi library that allows
     * you to configure any PIN to any MODE.
     * </p>
     *
     * @param pin pin number
     * @param mode  Pin mode/direction to apply to the selected pin.</br>The following constants are
     *            provided for use with this parameter:
     *            <ul>
     *            <li>INPUT</li>
     *            <li>OUTPUT</li>
     *            <li>ALT0</li>
     *            <li>ALT1</li>
     *            <li>ALT2</li>
     *            <li>ALT3</li>
     *            <li>ALT4</li>
     *            <li>ALT5</li>
     *            </ul>
     */
    public static native void pinModeAlt(int pin, int mode);



    /**
     * <p>Core Functions</p>
     *
     * This sets the pull-up or pull-down resistor mode on the given pin, which should be set as an
     * input. Unlike the Arduino, the BCM2835 has both pull-up an down internal resistors. The
     * parameter pud should be; PUD_OFF, (no pull up/down), PUD_DOWN (pull to ground) or PUD_UP
     * (pull to 3.3v)
     *
     * This function has no effect when in Sys mode (see above) If you need to activate a
     * pull-up/pull-down, then you can do it with the gpio program in a script before you start your
     * program.
     *
     * @see #PUD_OFF
     * @see #PUD_DOWN
     * @see #PUD_UP
     * @see <a
     *      href="http://wiringpi.com/reference/core-functions/">http://wiringpi.com/reference/core-functions/</a>
     * @param pin The GPIO pin number. </br><i>(Depending on how wiringPi was initialized, this may
     *            be the wiringPi pin number or the Broadcom GPIO pin number.)</i>
     * @param pud Pull Up/Down internal pin resistance.</br>The following constants are provided for
     *            use with this parameter:
     *            <ul>
     *            <li>PUD_OFF</li>
     *            <li>PUD_DOWN</li>
     *            <li>PUD_UP</li>
     *            </ul>
     */
    public static native void pullUpDnControl(int pin, int pud);


    /**
     * <p>Core Functions</p>
     *
     * <p>
     * Writes the value HIGH or LOW (1 or 0) to the given pin which must have been previously set as
     * an output.  WiringPi treats any non-zero number as HIGH, however 0 is the only representation of LOW.
     * </p>
     *
     * @see #HIGH
     * @see #LOW
     * @see <a
     *      href="http://wiringpi.com/reference/core-functions/">http://wiringpi.com/reference/core-functions/</a>
     * @param pin The GPIO pin number. </br><i>(Depending on how wiringPi was initialized, this may
     *            be the wiringPi pin number or the Broadcom GPIO pin number.)</i>
     * @param value The pin state to write to the selected pin.</br>The following constants are
     *            provided for use with this parameter:
     *            <ul>
     *            <li>HIGH</li>
     *            <li>LOW</li>
     *            </ul>
     */
    public static native void digitalWrite(int pin, int value);


    /**
     * <p>Core Functions</p>
     *
     * <p>
     * Writes the value HIGH or LOW ('true' or 'false') to the given pin which must have been
     * previously set as an output.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/core-functions/">http://wiringpi.com/reference/core-functions/</a>
     * @param pin The GPIO pin number. </br><i>(Depending on how wiringPi was initialized, this may
     *            be the wiringPi pin number or the Broadcom GPIO pin number.)</i>
     * @param value The pin boolean state to write to the selected pin.
     */
    public static void digitalWrite(int pin, boolean value) {
        digitalWrite(pin, (value) ? 1 : 0);
    }


    /**
     * <p>Core Functions</p>
     *
     * <p>
     * Writes the value to the PWM register for the given pin. The value must be between 0 and 1024.
     * (Again, note that only pin 1 supports PWM: )
     * </p>
     *
     * <p><b>This function has no effect when in Sys mode</b></p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/core-functions/">http://wiringpi.com/reference/core-functions/</a>
     * @param pin The GPIO pin number. </br><i>(Depending on how wiringPi was initialized, this may
     *            be the wiringPi pin number or the Broadcom GPIO pin number.)</i>
     * @param value The analog value to write to the selected pin. </br><i>(The value must be between
     *            0 and 1024.)</i>
     */
    public static native void pwmWrite(int pin, int value);


    /**
     * <p>Core Functions</p>
     *
     * <p>
     * This function returns the value read at the given pin. It will be HIGH or LOW (1 or 0)
     * depending on the logic level at the pin.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/core-functions/">http://wiringpi.com/reference/core-functions/</a>
     * @param pin The GPIO pin number. </br><i>(Depending on how wiringPi was initialized, this may
     *            be the wiringPi pin number or the Broadcom GPIO pin number.)</i>
     * @return If the selected GPIO pin is HIGH, then a value of '1' is returned; else of the pin is
     *         LOW, then a value of '0' is returned.
     */
    public static native int digitalRead(int pin);


    /**
     * <p>Core Functions</p>
     *
     * <p>
     * This returns the value read on the supplied analog input pin. You will need to register additional analog
     * modules to enable this function for devices such as the Gertboard, quick2Wire analog board, etc.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/core-functions/">http://wiringpi.com/reference/core-functions/</a>
     * @param pin The GPIO pin number. </br><i>(Depending on how wiringPi was initialized, this may
     *            be the wiringPi pin number or the Broadcom GPIO pin number.)</i>
     * @return Analog value of selected pin.
     */
    public static native int analogRead(int pin);


    /**
     * <p>Core Functions</p>
     *
     * <p>
     * This writes the given value to the supplied analog pin. You will need to register additional analog modules to
     * enable this function for devices such as the Gertboard.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/core-functions/">http://wiringpi.com/reference/core-functions/</a>
     * @param pin The GPIO pin number. </br><i>(Depending on how wiringPi was initialized, this may
     *            be the wiringPi pin number or the Broadcom GPIO pin number.)</i>
     * @param value The analog value to assign to the selected pin number.
     */
    public static native void analogWrite(int pin, int value);


    /**
     * <p>Timing Functions</p>
     *
     * <p>
     * This causes program execution to pause for at least howLong milliseconds. Due to the
     * multi-tasking nature of Linux it could be longer. Note that the maximum delay is an unsigned
     * 32-bit integer or approximately 49 days.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/timing/">http://wiringpi.com/reference/timing/</a>
     * @param howLong The number of milliseconds to delay the main program thread.
     */
    public static native void delay(long howLong);


    /**
     * <p>Timing Functions</p>
     *
     * <p>
     * This returns a number representing the number if milliseconds since your program called one
     * of the wiringPiSetup functions. It returns an unsigned 32-bit number which wraps after 49
     * days.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/timing/">http://wiringpi.com/reference/timing/</a>
     * @return The number if milliseconds since the program called one of the wiringPi setup
     *         functions.
     */
    public static native long millis();


    /**
     * <p>Timing Functions</p>
     *
     * <p>
     * This returns a number representing the number of microseconds since your program called one of the
     * wiringPiSetup functions. It returns an unsigned 32-bit number which wraps after approximately 71 minutes.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/timing/">http://wiringpi.com/reference/timing/</a>
     * @return The number if microseconds since the program called one of the wiringPi setup
     *         functions.
     */
    public static native long micros();


    /**
     * <p>Timing Functions</p>
     *
     * <p>
     * This causes program execution to pause for at least howLong microseconds. Due to the
     * multi-tasking nature of Linux it could be longer. Note that the maximum delay is an unsigned
     * 32-bit integer microseconds or approximately 71 minutes.
     *
     * Delays under 100 microseconds are timed using a hard-coded loop continually polling the system time,
     * Delays over 100 microseconds are done using the system nanosleep() function – You may need to consider
     * the implications of very short delays on the overall performance of the system, especially if using
     * threads.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/timing/">http://wiringpi.com/reference/timing/</a>
     * @param howLong The number of microseconds to delay the main program thread.
     */
    public static native void delayMicroseconds(long howLong);


    /**
     * <p>Priority, Interrupt and Thread Functions</p>
     *
     * <p>
     * This attempts to shift your program (or thread in a multi-threaded program) to a higher
     * priority and enables a real-time scheduling. The priority parameter should be from 0 (the
     * Default) to 99 (the maximum). This won't make your program go any faster, but it will give it
     * a bigger slice of time when other programs are running. The priority parameter works relative
     * to others and so you can make one program priority 1 and another priority 2 and it will have
     * the same effect as setting one to 10 and the other to 90 (as long as no other programs are
     * running with elevated priorities)
     * </p>
     *
     * <p>
     * The return value is 0 for success and -1 for error. If an error is returned, the program
     * should then consult the errno global variable, as per the usual conventions.
     * </p>
     *
     * <p>
     * Note: Only programs running as root can change their priority. If called from a non-root
     * program then nothing happens.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/priority-interrupts-and-threads/">http://wiringpi.com/reference/priority-interrupts-and-threads/</a>
     * @param priority  The priority parameter should be from 0 (the Default) to 99 (the maximum)
     * @return The return value is 0 for success and -1 for error. If an error is returned, the
     *         program should then consult the errno global variable, as per the usual conventions.
     */
    public static native int piHiPri(int priority);


    /**
     * <p>Priority, Interrupt and Thread Functions</p>
     *
     * <p>
     * With a newer kernel patched with the GPIO interrupt handling code, you can now wait for an
     * interrupt in your program. This frees up the processor to do other tasks while you're waiting
     * for that interrupt. The GPIO can be set to interrupt on a rising, falling or both edges of
     * the incoming signal.
     * </p>
     * <p> <b> int waitForInterrupt (int pin, int timeOut) </b> </p>
     *
     * <p>
     * When called, it will wait for an interrupt event to happen on that pin and your program will
     * be stalled. The timeOut parameter is given in milliseconds, or can be -1 which means to wait
     * forever.
     * </p>
     *
     * <p>
     * Before you call waitForInterrupt, you must first initialize the GPIO pin and at present the
     * only way to do this is to use the gpio program, either in a script, or using the system()
     * call from inside your program.
     * </p>
     *
     * <p>
     * e.g. We want to wait for a falling-edge interrupt on GPIO pin 0, so to setup the hardware, we
     * need to run:
     *
     * <pre>
     * gpio edge 0 falling
     * </pre>
     *
     * </p>
     *
     * @deprecated Note: Jan 2013: The waitForInterrupt() function is deprecated – you should use the newer
     *             and easier to use wiringPiISR() function.
     *
     * @see <a
     *      href="http://wiringpi.com/reference/priority-interrupts-and-threads/">http://wiringpi.com/reference/priority-interrupts-and-threads/</a>
     * @param pin The GPIO pin number. </br><i>(Depending on how wiringPi was initialized, this may
     *            be the wiringPi pin number or the Broadcom GPIO pin number.)</i>
     * @param timeout The number of milliseconds to wait before timing out. </br>A value of '-1' will
     *            disable the timeout.
     * @return The return value is -1 if an error occurred (and errno will be set appropriately), 0
     *         if it timed out, or 1 on a successful interrupt event.
     */
    public static native int waitForInterrupt(int pin, int timeout);


    /**
     * <p>Priority, Interrupt and Thread Functions</p>
     *
     * <p>
     * This function registers a function to received interrupts on the specified pin. The edgeType parameter is either
     * INT_EDGE_FALLING, INT_EDGE_RISING, INT_EDGE_BOTH or INT_EDGE_SETUP. If it is INT_EDGE_SETUP then no
     * initialisation of the pin will happen – it’s assumed that you have already setup the pin elsewhere
     * (e.g. with the gpio program), but if you specify one of the other types, then the pin will be exported and
     * initialised as specified. This is accomplished via a suitable call to the gpio utility program, so it need to
     * be available
     * </p>
     *
     * <p>
     * The pin number is supplied in the current mode – native wiringPi, BCM_GPIO, physical or Sys modes.
     * </p>
     *
     * <p>
     * This function will work in any mode, and does not need root privileges to work.
     * </p>
     *
     * <p>
     * The function will be called when the interrupt triggers. When it is triggered, it’s cleared in the dispatcher
     * before calling your function, so if a subsequent interrupt fires before you finish your handler, then it won’t
     * be missed. (However it can only track one more interrupt, if more than one interrupt fires while one is being
     * handled then they will be ignored)
     * </p>
     *
     * <p>
     * This function is run at a high priority (if the program is run using sudo, or as root) and executes
     * concurrently with the main program. It has full access to all the global variables, open file handles
     * and so on.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/priority-interrupts-and-threads/">http://wiringpi.com/reference/priority-interrupts-and-threads/</a>
     * @param pin The GPIO pin number. </br><i>(Depending on how wiringPi was initialized, this may
     *            be the wiringPi pin number or the Broadcom GPIO pin number.)</i>
     * @param edgeType The type of pin edge event to watch for: INT_EDGE_FALLING, INT_EDGE_RISING, INT_EDGE_BOTH or INT_EDGE_SETUP.
     * @param callback The callback interface implemented by the consumer.  The 'callback' method of this interface
     *                 will be invoked when the wiringPiISR issues a callback signal.
     * @return The return value is -1 if an error occurred (and errno will be set appropriately), 0
     *         if it timed out, or 1 on a successful interrupt event.
     */
    public static int wiringPiISR(int pin, int edgeType, GpioInterruptCallback callback){
        // if there is not collection in the array at this pin location, then initialize an array list
        if(isrCallbacks[pin] == null){
            isrCallbacks[pin] = new ArrayList<>();
        }

        // add provided callback interface to ISR callbacks collection
        isrCallbacks[pin].add(callback);
        return _wiringPiISR(pin, edgeType);
    }

    // internal collection for use subscribed IRS callbacks
    private static List<GpioInterruptCallback> isrCallbacks[] = new List[NUM_PINS];

    // delegated native method for 'wiringPiISR'
    private static native int _wiringPiISR(int pin, int edgeType);

    /**
     * Clear all WiringPiISR callbacks for this GPIO pin.
     *
     * @param pin The GPIO pin number. </br><i>(Depending on how wiringPi was initialized, this may
     *            be the wiringPi pin number or the Broadcom GPIO pin number.)</i>
     */
    public static void wiringPiClearISR(int pin){
        // ensure there is a callbacks at this pin location
        if(isrCallbacks[pin] != null){
            // remove all ISR callbacks
            isrCallbacks[pin].clear();
        }
    }

    /**
     * <p>
     * This method is provided as the callback handler for the Pi4J native library to invoke when a
     * GPIO ISR is detected. This method should not be called from any Java consumers. (Thus
     * is is marked as a private method.)
     * </p>
     *
     * @param pin GPIO pin number
     */
    private static void isrCallback(int pin) {
        // dispatch callback to the subscribers for this pin
        List<GpioInterruptCallback> callbacks = isrCallbacks[pin];

        // ensure callbacks collection exists for this pins number
        if(callbacks != null && !callbacks.isEmpty()){
            // iterate over each callback delegate and invoke the callback if the pin and edge type match
            for(GpioInterruptCallback callback : callbacks){
                callback.callback(pin);
            }
        }
    }

    //    /**
    //     * --------------------------------------------------------------------------------------------
    //     * lets not use native code for threading in Java; that could get you into some trouble.
    //     * --------------------------------------------------------------------------------------------
    //     * <h1>Concurrent Processing (multi-threading)</h1>
    //     *
    //     * wiringPi has a simplified interface to the Linux implementation of Posix threads, as well as
    //     * a (simplified) mechanisms to access mutexs (Mutual exclusions)
    //     *
    //     * Using these functions you can create a new process (a function inside your main program)
    //     * which runs concurrently with your main program and using the mutex mechanisms, safely pass
    //     * variables between them.
    //     *
    //     * @see <a
    //     *      href="http://wiringpi.com/reference/priority-interrupts-and-threads/">http://wiringpi.com/reference/priority-interrupts-and-threads/</a>
    //     */
    // public static native int piThreadCreate(void fn, int timeout);
    // public static native void piLock(int key);
    // public static native void piUnlock(int key);


    /**
     * <p>[Hardware]</p>
     *
     * <p> This method provides the board revision as determined by the wiringPi library.  </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/raspberry-pi-specifics/">http://wiringpi.com/reference/raspberry-pi-specifics/</a>
     * @return The return value represents the major board revision version.
     *         A -1 will be returned if the board revision cannot be determined.
     */
    public static native int piBoardRev();


    /**
     * <p>[Hardware]</p>
     *
     * <p> This method provides the edge GPIO pin number for the requested wiringPi pin number.  </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/raspberry-pi-specifics/">http://wiringpi.com/reference/raspberry-pi-specifics/</a>
     * @return The return value represents the RaspberryPi GPIO (edge) pin number.
     *         A -1 will be returned for an invalid pin number.
     */
    public static native int wpiPinToGpio(int wpiPin);


    /**
     * <p>[Hardware]</p>
     *
     * <p> This returns the BCM_GPIO pin number of the supplied physical pin on the board header connector. </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/raspberry-pi-specifics/">http://wiringpi.com/reference/raspberry-pi-specifics/</a>
     * @return The return value represents the RaspberryPi GPIO (edge) pin number.
     *         A -1 will be returned for an invalid pin number.
     */
    public static native int physPinToGpio(int physPin);


    /**
     * <p> This writes the 8-bit byte supplied to the first 8 GPIO pins. It’s the fastest way to set all 8 bits at once to a particular value,
     *     although it still takes two write operations to the Pi’s GPIO hardware.  </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/raspberry-pi-specifics/">http://wiringpi.com/reference/raspberry-pi-specifics/</a>
     */
    public static native void digitalWriteByte(int value);


    /**
     * <p>[PWM]</p>
     *
     * <p> The PWM generator can run in 2 modes – balanced and mark:space. The mark:space mode is traditional, however
     *     the default mode in the Pi is balanced. You can switch modes by supplying the parameter: PWM_MODE_BAL or PWM_MODE_MS.</p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/raspberry-pi-specifics/">http://wiringpi.com/reference/raspberry-pi-specifics/</a>
     */
    public static native void pwmSetMode(int mode);


    /**
     * <p>[PWM]</p>
     *
     * <p> This sets the range register in the PWM generator. The default is 1024.</p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/raspberry-pi-specifics/">http://wiringpi.com/reference/raspberry-pi-specifics/</a>
     */
    public static native void pwmSetRange(int range);


    /**
     * <p>[PWM]</p>
     *
     * <p> This sets the divisor for the PWM clock.</p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/raspberry-pi-specifics/">http://wiringpi.com/reference/raspberry-pi-specifics/</a>
     */
    public static native void pwmSetClock(int divisor);


    /**
     * <p>[Hardware]</p>
     *
     * <p> This sets the strength of the pad drivers for a particular group of pins. There are 3 groups of pins and the drive strength is from 0 to 7. Do not use this unless you know what you are doing. </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/raspberry-pi-specifics/">http://wiringpi.com/reference/raspberry-pi-specifics/</a>
     */
    public static native void setPadDrive(int group, int value);


    /**
     * <p>[Hardware]</p>
     *
     * <p> This gets the ALT function level of the requested pin number </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/raspberry-pi-specifics/">http://wiringpi.com/reference/raspberry-pi-specifics/</a>
     */
    public static native int getAlt(int pin);


    /**
     * <p>[Hardware]</p>
     *
     * <p> This sets the frequency of a GPIO pin </p>
     *
     * @see <a
     *      href="http://wiringpi.com/reference/raspberry-pi-specifics/">http://wiringpi.com/reference/raspberry-pi-specifics/</a>
     */
    public static native void gpioClockSet(int pin, int frequency);
}
