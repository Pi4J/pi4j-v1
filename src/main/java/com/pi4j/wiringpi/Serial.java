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

import com.pi4j.util.NativeLibraryLoader;

/**
 * <h1>WiringPi Serial Port Communication</h1>
 * 
 * <p>
 * WiringPi includes a simplified serial port handling library. It can use the on-board serial port,
 * or any USB serial device with no special distinctions between them. You just specify the device
 * name in the initial open function.
 * </p>
 * 
 * <p>
 * Note: The file descriptor (fd) returned is a standard Linux filehandle. You can use the standard
 * read(), write(), etc. system calls on this filehandle as required. E.g. you may wish to write a
 * larger block of binary data where the serialPutchar() or serialPuts() function may not be the
 * most appropriate function to use, in which case, you can use write() to send the data.
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
 *      href="https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/">https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class Serial
{
    /**
     * <p>The default hardware COM port provided via the Raspberry Pi GPIO header.</p>
     * 
     * @see #serialOpen(String,int)
     */
    public static final String DEFAULT_COM_PORT = "/dev/ttyAMA0";

    static
    {
        // Load the platform library
        NativeLibraryLoader.load("pi4j", "libpi4j.so");
    }

    /**
     * <h1>int serialOpen (char *device, int baud);</h1>
     * 
     * <p>
     * This opens and initializes the serial device and sets the baud rate. It sets the port into
     * raw mode (character at a time and no translations), and sets the read timeout to 10 seconds.
     * The return value is the file descriptor or -1 for any error, in which case errno will be set
     * as appropriate.
     * </p>
     * 
     * <p>
     * (ATTENTION: the 'device' argument can only be a maximum of 128 characters.)
     * </p>
     * 
     * @see #DEFAULT_COM_PORT
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/">https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/</a>
     * 
     * @param device <p>
     *            <The device address of the serial port to access. You can use constant
     *            'DEFAULT_COM_PORT' if you wish to access the default serial port provided via the
     *            GPIO header./p>
     * @param baud <p>
     *            The baud rate to use with the serial port.
     *            </p>
     * @return <p>
     *         The return value is the file descriptor or -1 for any error, in which case errno will
     *         be set as appropriate.
     *         </p>
     */
    public static native int serialOpen(String device, int baud);

    /**
     * <h1>void serialClose (int fd);</h1>
     * 
     * <p>
     * Closes the device identified by the file descriptor given.
     * </p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/">https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/</a>
     * @param fd <p>
     *            The file descriptor of the serial port.
     *            </p>
     */
    public static native void serialClose(int fd);

    /**
     * <h1>void serialFlush (int fd);</h1>
     * 
     * <p>
     * This discards all data received, or waiting to be send down the given device.
     * </p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/">https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/</a>
     * @param fd <p>
     *            The file descriptor of the serial port.
     *            </p>
     */
    public static native void serialFlush(int fd);

    /**
     * <h1>void serialPutchar (int fd, unsigned char c);</h1>
     * 
     * <p>
     * Sends the single byte to the serial device identified by the given file descriptor.
     * </p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/">https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/</a>
     * @param fd <p>
     *            The file descriptor of the serial port.
     *            </p>
     * @param data <p>
     *            The character to transmit to the serial port.
     *            </p>
     */
    public static native void serialPutchar(int fd, char data);

    /**
     * <h1>void serialPuts (int fd, char *s);</h1>
     * 
     * <p>
     * Sends the nul-terminated string to the serial device identified by the given file descriptor.
     * </p>
     * 
     * <p>
     * (ATTENTION: the 'data' argument can only be a maximum of 1024 characters.)
     * </p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/">https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/</a>
     * @param fd <p>
     *            The file descriptor of the serial port.
     *            </p>
     * @param data <p>
     *            The data string to transmit to the serial port.
     *            </p>
     */
    public static native void serialPuts(int fd, String data);

    /**
     * <h1>void serialPuts (int fd, String data, String...arguments);</h1>
     * 
     * <p>
     * Sends the nul-terminated formatted string to the serial device identified by the given file
     * descriptor.
     * </p>
     * 
     * <p>
     * (ATTENTION: the 'data' argument can only be a maximum of 1024 characters.)
     * </p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/">https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/</a>
     * @param fd <p>
     *            The file descriptor of the serial port.
     *            </p>
     * @param data <p>
     *            The formatted data string to transmit to the serial port.
     *            </p>
     * @param args <p>
     *            Arguments to the format string.
     *            </p>
     */
    public static void serialPuts(int fd, String data, String... args)
    {
        serialPuts(fd, String.format(data, (Object[]) args));
    }

    /**
     * <h1>int serialDataAvail (int fd);</h1>
     * 
     * <p>
     * Returns the number of characters available for reading, or -1 for any error condition, in
     * which case errno will be set appropriately.
     * </p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/">https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/</a>
     * @param fd <p>
     *            The file descriptor of the serial port.
     *            </p>
     * @return <p>
     *         Returns the number of characters available for reading, or -1 for any error
     *         condition, in which case errno will be set appropriately.
     *         </p>
     */
    public static native int serialDataAvail(int fd);

    /**
     * <h1>int serialGetchar (int fd);</h1>
     * 
     * <p>
     * Returns the next character available on the serial device. This call will block for up to 10
     * seconds if no data is available (when it will return -1)
     * </p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/">https://projects.drogon.net/raspberry-pi/wiringpi/serial-library/</a>
     * @param fd <p>
     *            The file descriptor of the serial port.
     *            </p>
     * @return <p>
     *         Returns the next character available on the serial device. This call will block for
     *         up to 10 seconds if no data is available (when it will return -1)
     *         </p>
     */
    public static native int serialGetchar(int fd);
}
