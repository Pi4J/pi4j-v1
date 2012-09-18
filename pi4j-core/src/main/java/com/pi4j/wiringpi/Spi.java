package com.pi4j.wiringpi;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  Spi.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 Pi4J
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
 * <h1>WiringPi SPI Library</h1>
 * 
 * <p>
 * WiringPi includes a software-driven PWM handler capable of outputting a PWM signal on any of the
 * Raspberry Pi's GPIO pins.
 * </p>
 * 
 * <p>
 * There are some limitations. To maintain a low CPU usage, the minimum pulse width is 100uS. That
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
    static
    {
        // Load the platform library
        NativeLibraryLoader.load("pi4j", "libpi4j.so");
    }

    /**
     * <h1>wiringPiSPIGetFd:</h1>
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
     * @return <p>return -1 on error</p>
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
     * @return <p>return -1 on error</p>
     */
    public static native int wiringPiSPISetup(int channel, int speed);
}
