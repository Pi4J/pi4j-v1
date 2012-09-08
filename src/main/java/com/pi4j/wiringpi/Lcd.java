/*
 * **********************************************************************
 * This file is part of the pi4j project: http://www.pi4j.com/
 * 
 * pi4j is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * pi4j is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with pi4j. If not,
 * see <http://www.gnu.org/licenses/>.
 * **********************************************************************
 */
package com.pi4j.wiringpi;

/**
 * <h1>WiringPi LCD Library</h1>
 * 
 * <p>
 * Part of wiringPi is a library to allow access to parallel interface LCD displays (Those that use
 * the popular Hitachi HD44780U or compatible controllers)
 * </p>
 * 
 * <p>
 * The library is simple to use in your own programs, however wiring the displays up may be
 * challenging, so do take care. It is possible to wire up more than one display! In 8-bit mode, the
 * first display needs 10 GPIO pins and each additional display needs just one more pin, so with a
 * maximum of 17 GPIO pins, that’s 8 displays. If you move to using a 4-bit interface (trivial in
 * the code), then it’s 4 more displays – 12 LCDs! However I suspect the rest of the wiring might be
 * somewhat challenging… Wiring is described at the end of the this page.
 * </p>
 * 
 * <p>
 * The LCD display can be either a 5V display or a 3,3v display, however if we are using a 5V
 * display then we must make absolutely sure the display can never write data back to the Raspberry
 * Pi, otherwise it will present 5V on the Pi’s GPIO pins which will not be good. At best you’ll
 * destroy the pin drivers, at worst you’ll destroy your Pi.
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
 * <blockquote> This library depends on the wiringPi native system library.</br> (developed by
 * Gordon Henderson @ <a href="https://projects.drogon.net/">https://projects.drogon.net/</a>)
 * </blockquote>
 * </p>
 * 
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @see <a
 *      href="https://projects.drogon.net/raspberry-pi/wiringpi/lcd-library/">https://projects.drogon.net/raspberry-pi/wiringpi/lcd-library/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class Lcd
{
    // Load the platform library
    static
    {
        System.loadLibrary("pi4j");
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
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/lcd-library/">https://projects.drogon.net/raspberry-pi/wiringpi/lcd-library/</a>
     * 
     * @param rows
     * @param cols
     * @param bits
     * @param rs
     * @param strb
     * @param d0
     * @param d1
     * @param d2
     * @param d3
     * @param d4
     * @param d5
     * @param d6
     * @param d7
     * @return
     */
    public static native int lcdInit(int rows, int cols, int bits, int rs, int strb, int d0,
            int d1, int d2, int d3, int d4, int d5, int d6, int d7);

    /**
     * <h1>void lcdHome (int handle);</h1>
     * 
     * <p>
     * Set the cursor to the home position.
     * </p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/lcd-library/">https://projects.drogon.net/raspberry-pi/wiringpi/lcd-library/</a>
     * @param handle
     */
    public static native void lcdHome(int handle);

    /**
     * <h1>void lcdClear (int handle);</h1>
     * 
     * <p>
     * Clears the LCD screen.
     * </p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/lcd-library/">https://projects.drogon.net/raspberry-pi/wiringpi/lcd-library/</a>
     * @param handle
     */
    public static native void lcdClear(int handle);

    /**
     * <h1>lcdPosition (int handle, int x, int y)</h1>
     * 
     * <p>
     * Set the position of the cursor for subsequent text entry.
     * </p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/lcd-library/">https://projects.drogon.net/raspberry-pi/wiringpi/lcd-library/</a>
     * @param handle
     * @param x
     * @param y
     */
    public static native void lcdPosition(int handle, int x, int y);

    /**
     * <h1>lcdPutchar(int handle, byte data)</h1>
     * 
     * <p>
     * Write a single character of data to the LCD display.
     * </p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/lcd-library/">https://projects.drogon.net/raspberry-pi/wiringpi/lcd-library/</a>
     * @param handle
     * @param data
     */
    public static native void lcdPutchar(int handle, byte data);

    /**
     * <h1>lcdPuts(int handle, String data)</h1>
     * 
     * <p>
     * Write string of data to the LCD display.
     * </p>
     * 
     * <p>
     * (ATTENTION: the 'data' argument can only be a maximum of 512 characters.)
     * </p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/lcd-library/">https://projects.drogon.net/raspberry-pi/wiringpi/lcd-library/</a>
     * @param handle
     * @param data
     */
    public static native void lcdPuts(int handle, String data);

    /**
     * <h1>lcdPuts(int handle, String data, String... args)</h1>
     * 
     * <p>
     * Write formatted string of data to the LCD display.
     * </p>
     * 
     * <p>
     * (ATTENTION: the 'data' argument can only be a maximum of 512 characters.)
     * </p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/lcd-library/">https://projects.drogon.net/raspberry-pi/wiringpi/lcd-library/</a>
     * @param handle
     * @param data
     * @param args
     */
    public static void lcdPuts(int handle, String data, String... args)
    {
        lcdPuts(handle, String.format(data, (Object[]) args));
    }
}
