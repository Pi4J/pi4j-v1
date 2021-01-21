package com.pi4j.wiringpi;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  Lcd.java
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


import com.pi4j.util.NativeLibraryLoader;

/**
 * <p>
 * Part of wiringPi is a library to allow access to parallel interface LCD displays (Those that use
 * the popular Hitachi HD44780U or compatible controllers)
 * </p>
 *
 * <p>
 * The library is simple to use in your own programs, however wiring the displays up may be
 * challenging, so do take care. It is possible to wire up more than one display! In 8-bit mode, the
 * first display needs 10 GPIO pins and each additional display needs just one more pin, so with a
 * maximum of 17 GPIO pins, that's 8 displays. If you move to using a 4-bit interface (trivial in
 * the code), then it's 4 more displays and 12 LCDs! However I suspect the rest of the wiring might be
 * somewhat challenging. Wiring is described at the end of the this page.
 * </p>
 *
 * <p>
 * The LCD display can be either a 5V display or a 3,3v display, however if we are using a 5V
 * display then we must make absolutely sure the display can never write data back to the Raspberry
 * Pi, otherwise it will present 5V on the Pi's GPIO pins which will not be good. At best you'll
 * destroy the pin drivers, at worst you'll destroy your Pi.
 * </p>
 *
 * <p>
 * So make sure you always connect the R/W pin on the display to ground to force the display to be
 * read-only to the host.
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
 * @see <a
 *      href="http://wiringpi.com/dev-lib/lcd-library/">http://wiringpi.com/dev-lib/lcd-library/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class Lcd {

    // private constructor
    private Lcd() {
        // forbid object construction
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j");
    }

    /**
     * <p>
     * First, you need to initialize wiringPi in the way you want to. The LCD library will call
     * pinMode functions, but these are ignored if you have already set the modes using the gpio
     * program and want to use the wiringPiSetupSys() mechanism.
     * </p>
     *
     * <pre>
     * int lcdInit(int rows, int cols, int bits, int rs, int strb, int d0, int d1, int d2, int d3, int d4,
     *         int d5, int d6, int d7);
     * </pre>
     *
     * <p>
     * This is the main initialization function and must be called before you use any other LCD
     * functions.
     * </p>
     *
     * <p>
     * Rows and cols are the rows and columns on the display (e.g. 2, 16 or 4,20). Bits is the
     * number of bits wide on the interface (4 or 8). The rs and strb represent the pin numbers of
     * the displays RS pin and Strobe (E) pin. The parameters d0 through d7 are the pin numbers of
     * the 8 data pins connected from the Pi to the display. Only the first 4 are used if you are
     * running the display in 4-bit mode.
     * </p>
     *
     * <p>
     * The pin numbers will be either wiringPi pin numbers of GPIO pin numbers depending on which
     * wiringPiSetup function you used.
     * </p>
     *
     * <p>
     * The return value is the handle to be used for all subsequent calls to the lcd library when
     * dealing with that LCD, or -1 to indicate a fault. (Usually incorrect parameters)
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/dev-lib/lcd-library/">http://wiringpi.com/dev-lib/lcd-library</a>
     *
     * @param rows number of rows
     * @param cols number of columns
     * @param bits number of bits wide on the interface (4 or 8)
     * @param rs pin number of the RS pin
     * @param strb pin number of the strobe (E) pin
     * @param d0 pin number for driving bit 1
     * @param d1 pin number for driving bit 2
     * @param d2 pin number for driving bit 3
     * @param d3 pin number for driving bit 4
     * @param d4 pin number for driving bit 5 (only used in 8-bit mode)
     * @param d5 pin number for driving bit 6 (only used in 8-bit mode)
     * @param d6 pin number for driving bit 7 (only used in 8-bit mode)
     * @param d7 pin number for driving bit 8 (only used in 8-bit mode)
     * @return The return value is the ‘handle’ to be used for all subsequent calls to the lcd library when dealing with that LCD, or -1 to indicate a fault. (Usually incorrect parameters)
     */
    public static native int lcdInit(int rows, int cols, int bits, int rs, int strb, int d0,
            int d1, int d2, int d3, int d4, int d5, int d6, int d7);

    /**
     * <p>
     * Set the cursor to the home position.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/dev-lib/lcd-library/">http://wiringpi.com/dev-lib/lcd-library/</a>
     * @param handle file handle
     */
    public static native void lcdHome(int handle);

    /**
     * <p>
     * Clears the LCD screen.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/dev-lib/lcd-library/">http://wiringpi.com/dev-lib/lcd-library/</a>
     * @param handle file handle
     */
    public static native void lcdClear(int handle);

    /**
     * <p>
     * Turns the LCD display ON (1) / OFF (0)
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/dev-lib/lcd-library/">http://wiringpi.com/dev-lib/lcd-library/</a>
     * @param handle file handle
     */
    public static native void lcdDisplay(int handle, int state);

    /**
     * <p>
     * Turns the LCD cursor ON (1) / OFF (0)
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/dev-lib/lcd-library/">http://wiringpi.com/dev-lib/lcd-library/</a>
     * @param handle file handle
     */
    public static native void lcdCursor(int handle, int state);


    /**
     * <p>
     * Turns the LCD cursor blinking behavior ON (1) / OFF (0)
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/dev-lib/lcd-library/">http://wiringpi.com/dev-lib/lcd-library/</a>
     * @param handle file handle
     */
    public static native void lcdCursorBlink(int handle, int state);


    /**
     * <p>
     * Set the position of the cursor for subsequent text entry.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/dev-lib/lcd-library/">http://wiringpi.com/dev-lib/lcd-library/</a>
     * @param handle file handle
     * @param x column position staring at 0 (left)
     * @param y row position starting at 0 (top)
     */
    public static native void lcdPosition(int handle, int x, int y);

    /**
     * <p>
     * This allows you to re-define one of the 8 user-definable chanracters in the display. The data array is 8 bytes
     * which represent the character from the top-line to the bottom line. Note that the characters are actually 5×8,
     * so only the lower 5 bits are used. The index is from 0 to 7 and you can subsequently print the character defined
     * using the lcdPutchar() call.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/dev-lib/lcd-library/">http://wiringpi.com/dev-lib/lcd-library/</a>
     * @param handle file handle
     * @param index index value from 0 to 7
     * @param data 8 bytes which represent the character from the top-line to the bottom line
     */
    public static native void lcdCharDef(int handle, int index, byte data[]);

    /**
     * <p>
     * Write a single character of data to the LCD display.
     * </p>
     *
     * @see <a
     *      href="http://wiringpi.com/dev-lib/lcd-library/">http://wiringpi.com/dev-lib/lcd-library/</a>
     * @param handle file handle
     * @param data character data to write
     */
    public static native void lcdPutchar(int handle, byte data);

    /**
     * <p>Write string of data to the LCD display.</p>
     *
     * <p>(ATTENTION: the 'data' argument can only be a maximum of 512 characters.)</p>
     *
     * @see <a
     *      href="http://wiringpi.com/dev-lib/lcd-library/">http://wiringpi.com/dev-lib/lcd-library/</a>
     * @param handle file handle
     * @param data string data to write
     */
    public static native void lcdPuts(int handle, String data);

    /**
     * <p>Write formatted string of data to the LCD display.</p>
     *
     * <p>(ATTENTION: the 'data' argument can only be a maximum of 512 characters.)</p>
     *
     * @see <a
     *      href="http://wiringpi.com/dev-lib/lcd-library/">http://wiringpi.com/dev-lib/lcd-library/</a>
     * @param handle file handle
     * @param data format string to write
     * @param args string arguments to use in formatted string
     */
    public static void lcdPuts(int handle, String data, String... args) {
        lcdPuts(handle, String.format(data, (Object[]) args));
    }
}
