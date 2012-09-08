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
 * <h1>WiringPi SPI Library</h1>
 * 
 * <p>
 * WiringPi includes a software-driven PWM handler capable of outputting a PWM signal on any of the
 * Raspberry Pi’s GPIO pins.
 * </p>
 * 
 * <p>
 * There are some limitations… To maintain a low CPU usage, the minimum pulse width is 100uS. That
 * combined with the default suggested range of 100 gives a PWM frequency of 100Hz. You can lower
 * the range to get a higher frequency, at the expense of resolution, or increase to get more
 * resolution, but that will lower the frequency. If you change the pulse-width in the drive code,
 * then be aware that at delays of less than 100uS wiringPi does it in a software loop, which means
 * that CPU usage will rise dramatically, and controlling more than one pin will be almost
 * impossible.
 * </p>
 * 
 * <p>
 * Also note that while the routines run themselves at a higher and real-time priority, Linux can
 * still affect the accuracy of the generated signal.
 * </p>
 * 
 * <p>
 * However, within these limitations, control of a light/LED or a motor is very achievable.
 * </p>
 * 
 * <p>
 * <b>You must initialize wiringPi with one of wiringPiSetup() or wiringPiSetupGpio() functions
 * beforehand. wiringPiSetupSys() is not fast enough, so you must run your programs with sudo.</b>
 * </p>
 * 
 * <p>
 * Before using the Pi4J library, you need to ensure that the Java VM in configured with access to
 * the following system libraries:
 * <ul>
 * <li>pi4j</li>
 * <li>wiringPi</li>
 * <li>pthread</li>
 * </ul>
 * <blockquote> This library depends on the wiringPi native system library.</br> (developed by
 * Gordon Henderson @ <a href="https://projects.drogon.net/">https://projects.drogon.net/</a>)
 * </blockquote>
 * </p>
 * 
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class Spi
{
    // Load the platform library
    static
    {
        System.loadLibrary("pi4j");
    }

    /**
     * <h1>wiringPiSPIGetFd:</hq>
     * 
     * <p>
     * Return the file-descriptor for the given channel
     * </p>
     * 
     * @param channel <p>
     *            SPI channel
     *            </p>
     * @return <p>
     *         file-descriptor for the given channel
     *         </p>
     */
    public static native int wiringPiSPIGetFd(int channel);

    /**
     * <h1>wiringPiSPIDataRW:</h1>
     * 
     * <p>
     * Write and Read a block of data over the SPI bus. Note the data is being read into the
     * transmit buffer, so will overwrite it! This is also a full-duplex operation.
     * </p>
     * 
     * <p>
     * (ATTENTION: the 'data' argument can only be a maximum of 1024 characters.)
     * </p>
     * 
     * @param channel <p>
     *            SPI channel
     *            </p>
     * @param data
     * @param len
     * @return
     */
    public static native int wiringPiSPIDataRW(int channel, String data, int len);

    /**
     * <h1>wiringPiSPISetup:</h1>
     * 
     * <p>
     * Open the SPI device, and set it up, etc.</b>
     * 
     * @param channel <p>
     *            SPI channel
     *            </p>
     * @param speed <p>
     *            SPI speed
     *            </p>
     * @return
     */
    public static native int wiringPiSPISetup(int channel, int speed);
}
