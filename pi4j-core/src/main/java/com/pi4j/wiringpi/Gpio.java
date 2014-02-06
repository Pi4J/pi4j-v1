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
 * Copyright (C) 2012 - 2014 Pi4J
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


import com.pi4j.util.NativeLibraryLoader;

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
 * Gordon Henderson @ <a href="https://projects.drogon.net/">https://projects.drogon.net/</a>)
 * </blockquote>
 * </p>
 * 
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @see <a
 *      href="https://projects.drogon.net/raspberry-pi/wiringpi/">https://projects.drogon.net/raspberry-pi/wiringpi/</a>
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
    public static final int NUM_PINS = 20;

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

    static {
        // Load the platform library
        NativeLibraryLoader.load("pi4j", "libpi4j.so");
    }

    /**
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
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/functions/">https://projects.drogon.net/raspberry-pi/wiringpi/functions/</a>
     * @return If this function returns a value of '-1' then an error has occurred and the
     *         initialization of the GPIO has failed. A return value of '0' indicates a successful
     *         GPIO initialization.
     */
    public static native int wiringPiSetup();

    /**
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
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/functions/">https://projects.drogon.net/raspberry-pi/wiringpi/functions/</a>
     * @return If this function returns a value of '-1' then an error has occurred and the
     *         initialization of the GPIO has failed. A return value of '0' indicates a successful
     *         GPIO initialization.
     */
    public static native int wiringPiSetupSys();

    /**
     * <p>
     * This setup function is identical to wiringPiSetup(), however it allows the calling programs
     * to use the Broadcom GPIO pin numbers directly with no re-mapping.
     * </p>
     * 
     * <p> <b><i>This function needs to be called with root privileges.</i></b></p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/functions/">https://projects.drogon.net/raspberry-pi/wiringpi/functions/</a>
     * @return If this function returns a value of '-1' then an error has occurred and the
     *         initialization of the GPIO has failed. A return value of '0' indicates a successful
     *         GPIO initialization.
     */
    public static native int wiringPiSetupGpio();

    /**
     * <p>
     * This sets the mode of a pin to either INPUT, OUTPUT, or PWM_OUTPUT. Note that only wiringPi
     * pin 1 (GPIO-18) supports PWM output. The pin number is the number obtained from the pins
     * table.
     * </p>
     * 
     * <p> <b><i>This function has no effect when in Sys mode.</i></b></p>
     * 
     * @see #INPUT
     * @see #OUTPUT
     * @see #PWM_OUTPUT
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/functions/">https://projects.drogon.net/raspberry-pi/wiringpi/functions/</a>
     * @param pin The GPIO pin number. </br><i>(Depending on how wiringPi was initialized, this may
     *            be the wiringPi pin number or the Broadcom GPIO pin number.)</i>
     * @param mode  Pin mode/direction to apply to the selected pin.</br>The following constants are
     *            provided for use with this parameter:
     *            <ul>
     *            <li>INPUT</li>
     *            <li>OUTPUT</li>
     *            <li>PWM_OUTPUT</li>
     *            </ul>
     */
    public static native void pinMode(int pin, int mode);

    /**
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
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/functions/">https://projects.drogon.net/raspberry-pi/wiringpi/functions/</a>
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
     * <p>
     * Writes the value HIGH or LOW (1 or 0) to the given pin which must have been previously set as
     * an output.
     * </p>
     * 
     * @see #HIGH
     * @see #LOW
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/functions/">https://projects.drogon.net/raspberry-pi/wiringpi/functions/</a>
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
     * <p>
     * Writes the value HIGH or LOW ('true' or 'false') to the given pin which must have been
     * previously set as an output.
     * </p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/functions/">https://projects.drogon.net/raspberry-pi/wiringpi/functions/</a>
     * @param pin The GPIO pin number. </br><i>(Depending on how wiringPi was initialized, this may
     *            be the wiringPi pin number or the Broadcom GPIO pin number.)</i>
     * @param value The pin boolean state to write to the selected pin.
     */
    public static void digitalWrite(int pin, boolean value) {
        digitalWrite(pin, (value == true) ? 1 : 0);
    }

    /**
     * <p>
     * Writes the value to the PWM register for the given pin. The value must be between 0 and 1024.
     * (Again, note that only pin 1 supports PWM)
     * </p>
     * 
     * <p><b>This function has no effect when in Sys mode</b></p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/functions/">https://projects.drogon.net/raspberry-pi/wiringpi/functions/</a>
     * @param pin The GPIO pin number. </br><i>(Depending on how wiringPi was initialized, this may
     *            be the wiringPi pin number or the Broadcom GPIO pin number.)</i>
     * @param value The analog value to write to the selected pin. </br><i>(The value must be between
     *            0 and 1024.)</i>
     */
    public static native void pwmWrite(int pin, int value);

    /**
     * <p>
     * This function returns the value read at the given pin. It will be HIGH or LOW (1 or 0)
     * depending on the logic level at the pin.
     * </p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/functions/">https://projects.drogon.net/raspberry-pi/wiringpi/functions/</a>
     * @param pin The GPIO pin number. </br><i>(Depending on how wiringPi was initialized, this may
     *            be the wiringPi pin number or the Broadcom GPIO pin number.)</i>
     * @return If the selected GPIO pin is HIGH, then a value of '1' is returned; else of the pin is
     *         LOW, then a value of '0' is returned.
     */
    public static native int digitalRead(int pin);

    /**
     * <p>
     * This causes program execution to pause for at least howLong milliseconds. Due to the
     * multi-tasking nature of Linux it could be longer. Note that the maximum delay is an unsigned
     * 32-bit integer or approximately 49 days.
     * </p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/functions/">https://projects.drogon.net/raspberry-pi/wiringpi/functions/</a>
     * @param howLong The number of milliseconds to delay the main program thread.
     */
    public static native void delay(long howLong);

    /**
     * <p>
     * This returns a number representing the number if milliseconds since your program called one
     * of the wiringPiSetup functions. It returns an unsigned 32-bit number which wraps after 49
     * days.
     * </p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/functions/">https://projects.drogon.net/raspberry-pi/wiringpi/functions/</a>
     * @return The number if milliseconds since the program called one of the wiringPi setup
     *         functions.
     */
    public static native long millis();

    /**
     * <p>
     * This causes program execution to pause for at least howLong microseconds. Due to the
     * multi-tasking nature of Linux it could be longer. Note that the maximum delay is an unsigned
     * 32-bit integer microseconds or approximately 71 minutes.
     * </p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/functions/">https://projects.drogon.net/raspberry-pi/wiringpi/functions/</a>
     * @param howLong The number of microseconds to delay the main program thread.
     */
    public static native void delayMicroseconds(long howLong);

    /**
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
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/functions/">https://projects.drogon.net/raspberry-pi/wiringpi/functions/</a>
     * @param priority  The priority parameter should be from 0 (the Default) to 99 (the maximum)
     * @return The return value is 0 for success and -1 for error. If an error is returned, the
     *         program should then consult the errno global variable, as per the usual conventions.
     */
    public static native int piHiPri(int priority);

    /**
     * <p>[Interrupts]</p>
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
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/functions/">https://projects.drogon.net/raspberry-pi/wiringpi/functions/</a>
     * @param pin The GPIO pin number. </br><i>(Depending on how wiringPi was initialized, this may
     *            be the wiringPi pin number or the Broadcom GPIO pin number.)</i>
     * @param timeout The number of milliseconds to wait before timing out. </br>A value of '-1' will
     *            disable the timeout.
     * @return The return value is -1 if an error occurred (and errno will be set appropriately), 0
     *         if it timed out, or 1 on a successful interrupt event.
     */
    public static native int waitForInterrupt(int pin, int timeout);

    /**
     * <p>[Hardware]</p>
     * 
     * <p> This method provides the board revision as determined by the wiringPi library.  </p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/functions/">https://projects.drogon.net/raspberry-pi/wiringpi/functions/</a>
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
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/functions/">https://projects.drogon.net/raspberry-pi/wiringpi/functions/</a>
     * @return The return value represents the RaspberryPi GPIO (edge) pin number. 
     *         A -1 will be returned for an invalid pin number.
     */
    public static native int wpiPinToGpio(int wpiPin);
                                                     
    
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
    //     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/functions/">https://projects.drogon.net/raspberry-pi/wiringpi/functions/</a>
    //     */
    // public static native int piThreadCreate(void fn, int timeout);
    // public static native void piLock(int key);
    // public static native void piUnlock(int key);

    // private static class Hook extends Thread
    // {
    // File libfile;
    //
    // public Hook(File libfile)
    // {
    // this.libfile = libfile;
    // }
    // public void run()
    // {
    // if(libfile.exists())
    // libfile.deleteOnExit()
    // System.out.println( "Running Clean Up..." );
    // }
    // }
}
