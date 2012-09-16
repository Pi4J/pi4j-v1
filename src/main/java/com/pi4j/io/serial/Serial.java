package com.pi4j.io.serial;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library
 * FILENAME      :  Serial.java  
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


/**
 * <h1>Serial Interface</h1>
 * 
 * <p>
 * This interface provides a set of functions for 'Serial' communication.
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
 * @see #SerialFactory
 * @see #SerialDataEvent
 * @see #SerialDataListener
 * 
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public interface Serial
{
    /**
     * <p>
     * The default hardware COM port provided via the Raspberry Pi GPIO header.
     * </p>
     * 
     * @see #serialOpen(String,int)
     */
    public static final String DEFAULT_COM_PORT = "/dev/ttyAMA0";

    /**
     * <p>
     * This method is call to open a serial port for communication.
     * </p>
     * 
     * @see #DEFAULT_COM_PORT
     * 
     * @param device <p>
     *            <The device address of the serial port to access. You can use constant
     *            'DEFAULT_COM_PORT' if you wish to access the default serial port provided via the
     *            GPIO header./p>
     * @param baudRate <p>
     *            The baud rate to use with the serial port.
     *            </p>
     * @return <p>
     *         The return value is the file descriptor or -1 for any error.
     *         </p>
     */
    int open(String device, int baudRate);

    /**
     * <p>
     * This method is called to close a currently open open serial port.
     * </p>
     */
    void close();

    /**
     * <p>
     * This method is called to determine if the serial port is already open.
     * </p>
     * 
     * @see #open(String, int)
     * @return <p>
     *         a value of 'true' is returned if the serial port is already open.
     *         </p>
     */
    boolean isOpen();

    /**
     * <p>
     * This method is called to immediately flush the serial data transmit buffer and force any
     * pending data to be sent to the serial port immediately.
     * </p>
     */
    void flush();

    /**
     * <p>
     * This method will read the next character available from the serial port receive buffer.
     * </p>
     * <p>
     * <b>NOTE: If a serial data listener has been implemented and registered with this class, then
     * this method should not be called directly. A background thread will be running to collect
     * received data from the serial port receive buffer and the received data will be available on
     * via the event.</b>
     * </p>
     * 
     * @return <p>
     *         next available character in the serial data buffer
     *         </p>
     */
    char read();

    /**
     * <p>
     * This method is called to submit a single character of data to the serial port transmit
     * buffer.
     * </p>
     * 
     * @param data <p>
     *            A single character to be transmitted.
     *            </p>
     */
    void write(char data);

    /**
     * <p>
     * This method is called to submit a character array of data to the serial port transmit buffer.
     * </p>
     * 
     * @param data <p>
     *            A character array of data to be transmitted.
     *            </p>
     */
    void write(char data[]);

    /**
     * <p>
     * This method is called to submit a single byte of data to the serial port transmit buffer.
     * </p>
     * 
     * @param data <p>
     *            A single byte to be transmitted.
     *            </p>
     */
    void write(byte data);

    /**
     * <p>
     * This method is called to submit a byte array of data to the serial port transmit buffer.
     * </p>
     * 
     * @param data <p>
     *            A byte array of data to be transmitted.
     *            </p>
     */
    void write(byte data[]);

    /**
     * <p>
     * This method is called to submit a string of data to the serial port transmit buffer.
     * </p>
     * 
     * @param data <p>
     *            A string of data to be transmitted.
     *            </p>
     */
    void write(String data);

    /**
     * <p>
     * This method is called to submit a string of data with trailing CR + LF characters to the
     * serial port transmit buffer.
     * </p>
     * 
     * @param data <p>
     *            A string of data to be transmitted.
     *            </p>
     */
    void writeln(String data);

    /**
     * <p>
     * This method is called to submit a string of formatted data to the serial port transmit
     * buffer.
     * </p>
     * 
     * @param data <p>
     *            A string of formatted data to be transmitted.
     *            </p>
     * @param args <p>
     *            A series of arguments that can be included for the format string variable
     *            replacements.
     *            </p>
     */
    void write(String data, String... args);

    /**
     * <p>
     * This method is called to submit a string of formatted data with trailing CR + LF characters
     * to the serial port transmit buffer.
     * </p>
     * 
     * @param data <p>
     *            A string of formatted data to be transmitted.
     *            </p>
     * @param args <p>
     *            A series of arguments that can be included for the format string variable
     *            replacements.
     *            </p>
     */
    void writeln(String data, String... args);

    /**
     * <p>
     * This method is called to determine if and how many bytes are available on the serial received
     * data buffer.
     * </p>
     * 
     * @return <p>
     *         The number of available bytes pending in the serial received buffer is returned.
     *         </p>
     */
    int availableBytes();

    /**
     * <h1>Add Serial Event Listener</h1>
     * 
     * <p>
     * Java consumer code can call this method to register itself as a listener for serial data
     * events.
     * </p>
     * 
     * @see #SerialListener
     * @see #SerialDataEvent
     * 
     * @param listener <p>
     *            A class instance that implements the SerialListener interface.
     *            </p>
     */
    void addListener(SerialDataListener listener);

    /**
     * <h1>Remove Serial Event Listener</h1>
     * 
     * <p>
     * Java consumer code can call this method to unregister itself as a listener for serial data
     * events.
     * </p>
     * 
     * @see #SerialListener
     * @see #SerialDataEvent
     * 
     * @param listener <p>
     *            A class instance that implements the SerialListener interface.
     *            </p>
     */
    void removeListener(SerialDataListener listener);
}
