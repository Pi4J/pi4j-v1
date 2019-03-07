package com.pi4j.io.serial.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SerialImpl.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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


import com.pi4j.io.serial.*;
import com.pi4j.io.serial.tasks.SerialDataEventDispatchTaskImpl;
import com.pi4j.jni.SerialInterrupt;
import com.pi4j.jni.SerialInterruptEvent;
import com.pi4j.jni.SerialInterruptListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

/**
 * <p> This implementation class implements the 'Serial' interface using the WiringPi Serial library.</p>
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
 * @see com.pi4j.io.serial.Serial
 * @see com.pi4j.io.serial.SerialDataEvent
 * @see com.pi4j.io.serial.SerialDataEventListener
 * @see com.pi4j.io.serial.SerialFactory
 *
 * @see <a href="https://www.pi4j.com/">https://www.pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class SerialImpl extends AbstractSerialDataReaderWriter implements Serial {

    protected int fileDescriptor = -1;
    protected final CopyOnWriteArrayList<SerialDataEventListener> listeners;
    protected final ExecutorService executor;
    protected final SerialByteBuffer receiveBuffer;
    protected boolean bufferingDataReceived = true;

    /**
     * default constructor
     */
    public SerialImpl(){
        listeners = new CopyOnWriteArrayList<>();
        executor = SerialFactory.getExecutorServiceFactory().newSingleThreadExecutorService();
        receiveBuffer = new SerialByteBuffer();

        // register shutdown callback hook class
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
    }

    /**
     * This class is used to perform any configured shutdown actions
     * for the serial impl
     *
     * @author Robert Savage
     *
     */
    private class ShutdownHook extends Thread {
        public void run() {

            // close serial port
            if(isOpen()){
                try {
                    close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // remove serial port listener
            SerialInterrupt.removeListener(fileDescriptor);

            // perform shutdown of any monitoring threads
            SerialFactory.shutdown();
        }
    }

    /**
     * <p>
     * This opens and initializes the serial port/device and sets the communication parameters.
     * It sets the port into raw mode (character at a time and no translations).
     * </p>
     *
     * <p>
     * (ATTENTION: the 'device' argument can only be a maximum of 128 characters.)
     * </p>
     *
     * @see #DEFAULT_COM_PORT
     *
     * @param device
     *          The device address of the serial port to access. You can use constant
     *          'DEFAULT_COM_PORT' if you wish to access the default serial port provided via the
     *          GPIO header.
     * @param baud
     *          The baud rate to use with the serial port. (Custom baud rate are not supported)
     * @param dataBits
     *          The data bits to use for serial communication. (5,6,7,8)
     * @param parity
     *          The parity setting to use for serial communication. (None, Event, Odd, Mark, Space)
     * @param stopBits
     *          The stop bits to use for serial communication. (1,2)
     * @param flowControl
     *          The flow control option to use for serial communication. (none, hardware, software)
     *
     * @throws IOException thrown on any error.
     */
    @Override
    public void open(String device, int baud, int dataBits, int parity, int stopBits, int flowControl)
            throws IOException{

        // open serial port
        fileDescriptor = com.pi4j.jni.Serial.open(device, baud, dataBits, parity, stopBits, flowControl);

        // read in initial buffered data (if any) into the receive buffer
        int available = com.pi4j.jni.Serial.available(fileDescriptor);

        if(available > 0) {
            byte[] initial_data = com.pi4j.jni.Serial.read(fileDescriptor, available);
            if (initial_data.length > 0) {
                try {
                    // write data to the receive buffer
                    receiveBuffer.write(initial_data);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // create a serial data listener event for data receive events from the serial device
        SerialInterrupt.addListener(fileDescriptor, new SerialInterruptListener() {
            @Override
            public void onDataReceive(SerialInterruptEvent event) {

                // ignore any event triggers that are missing data
                if(event.getLength() <= 0) return;

                try {
                    SerialDataEvent sde = null;

                    if(isBufferingDataReceived()) {
                        // stuff event data payload into the receive buffer
                        receiveBuffer.write(event.getData());

                        //System.out.println("BUFFER SIZE : " + receiveBuffer.capacity());
                        //System.out.println("BUFFER LEFT : " + receiveBuffer.remaining());
                        //System.out.println("BUFFER AVAIL: " + receiveBuffer.available());

                        // create the serial data event; since we are buffering data
                        // it will be located in the receive buffer
                        sde = new SerialDataEvent(SerialImpl.this);
                    }
                    else{
                        // create the serial data event; since we are NOT buffering data
                        // we will pass the specific data payload directly into the event
                        sde = new SerialDataEvent(SerialImpl.this, event.getData());
                    }

                    // add a new serial data event notification to the thread pool for *immediate* execution
                    // we notify the event listeners on a separate thread to prevent blocking the native monitoring thread
                    executor.execute(new SerialDataEventDispatchTaskImpl(sde, listeners));
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // ensure file descriptor is valid
        if (fileDescriptor == -1) {
            throw new IOException("Cannot open serial port");
        }
    }

    /**
     * <p>
     * This opens and initializes the serial port/device and sets the communication parameters.
     * It sets the port into raw mode (character at a time and no translations).
     *
     * This method will use the following default serial configuration parameters:
     *  - DATA BITS    = 8
     *  - PARITY       = NONE
     *  - STOP BITS    = 1
     *  - FLOW CONTROL = NONE
     *
     * </p>
     *
     * <p>
     * (ATTENTION: the 'device' argument can only be a maximum of 128 characters.)
     * </p>
     *
     * @see #DEFAULT_COM_PORT
     *
     * @param device
     *          The device address of the serial port to access. You can use constant
     *          'DEFAULT_COM_PORT' if you wish to access the default serial port provided via the
     *          GPIO header.
     * @param baud
     *          The baud rate to use with the serial port.
     *
     * @throws IOException thrown on any error.
     */
    @Override
    public void open(String device, int baud) throws IOException{
        // open the serial port with config settings of "8N1" and no flow control
        open(device,
             baud,
             com.pi4j.jni.Serial.DATA_BITS_8,
             com.pi4j.jni.Serial.PARITY_NONE,
             com.pi4j.jni.Serial.STOP_BITS_1,
             com.pi4j.jni.Serial.FLOW_CONTROL_NONE);
    }

    /**
     * <p>
     * This opens and initializes the serial port/device and sets the communication parameters.
     * It sets the port into raw mode (character at a time and no translations).
     * </p>
     *
     * <p>
     * (ATTENTION: the 'device' argument can only be a maximum of 128 characters.)
     * </p>
     *
     * @see #DEFAULT_COM_PORT
     *
     * @param device
     *          The device address of the serial port to access. You can use constant
     *          'DEFAULT_COM_PORT' if you wish to access the default serial port provided via the
     *          GPIO header.
     * @param baud
     *          The baud rate to use with the serial port.
     * @param dataBits
     *          The data bits to use for serial communication. (5,6,7,8)
     * @param parity
     *          The parity setting to use for serial communication. (None, Event, Odd, Mark, Space)
     * @param stopBits
     *          The stop bits to use for serial communication. (1,2)
     * @param flowControl
     *          The flow control option to use for serial communication. (none, hardware, software)
     *
     * @throws IOException thrown on any error.
     */
    @Override
    public void open(String device, Baud baud, DataBits dataBits, Parity parity, StopBits stopBits,
                     FlowControl flowControl) throws IOException{
        // open the serial port with NO ECHO and NO (forced) BUFFER FLUSH
        open(device, baud.getValue(), dataBits.getValue(), parity.getIndex(),
                stopBits.getValue(), flowControl.getIndex());
    }

    /**
     * <p>
     * This opens and initializes the serial port/device and sets the communication parameters.
     * It sets the port into raw mode (character at a time and no translations).
     * </p>
     *
     * <p>
     * (ATTENTION: the 'device' argument can only be a maximum of 128 characters.)
     * </p>
     *
     * @see #DEFAULT_COM_PORT
     *
     * @param serialConfig
     *          A serial configuration object that contains the device, baud rate, data bits, parity,
     *          stop bits, and flow control settings.
     *
     * @throws  IOException thrown on any error.
     */
    @Override
    public void open(SerialConfig serialConfig) throws IOException{
        // open the serial port with config settings
        open(serialConfig.device(),
             serialConfig.baud().getValue(),
             serialConfig.dataBits().getValue(),
             serialConfig.parity().getIndex(),
             serialConfig.stopBits().getValue(),
             serialConfig.flowControl().getIndex());
    }

    /**
     * This method is called to determine if the serial port is already open.
     *
     * @see #open(String, int)
     * @return a value of 'true' is returned if the serial port is already open.
     */
    @Override
    public boolean isOpen() {
        return (fileDescriptor >= 0);
    }

    /**
     * This method is called to determine if the serial port is already closed.
     *
     * @see #open(String, int)
     * @return a value of 'true' is returned if the serial port is already in the closed state.
     */
    @Override
    public boolean isClosed(){
        return !(isOpen());
    }


    /**
     * This method is called to close a currently open open serial port.
     *
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    @Override
    public void close() throws IllegalStateException, IOException {

        // validate state
        if (isClosed())
    	    throw new IllegalStateException("Serial connection is not open; cannot 'close()'.");

        // remove serial port listener
        SerialInterrupt.removeListener(fileDescriptor);

    	// close serial port now
        com.pi4j.jni.Serial.close(fileDescriptor);

        // reset file descriptor
        fileDescriptor = -1;
	}


    /**
     * <p>
     *     Forces the transmission of any remaining data in the serial port transmit buffer.
     *     Please note that this does not force the transmission of data, it discards it!
     * </p>
     *
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    @Override
    public void flush() throws IllegalStateException, IOException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'flush()'.");

        // flush data to serial port immediately
        com.pi4j.jni.Serial.flush(fileDescriptor);
    }

    /**
     * <p>
     *     Discards any data in the serial receive (input) buffer.
     *     Please note that this does not force the transmission of data, it discards it!
     * </p>
     *
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    @Override
    public void discardInput() throws IllegalStateException, IOException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'discardInput()'.");

        // flush data to serial port immediately
        com.pi4j.jni.Serial.discardInput(fileDescriptor);
    }

    /**
     * <p>
     *     Discards any data in the serial transmit (output) buffer.
     *     Please note that this does not force the transmission of data, it discards it!
     * </p>
     *
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    @Override
    public void discardOutput() throws IllegalStateException, IOException{
        // validate state
        if (isClosed())
                throw new IllegalStateException("Serial connection is not open; cannot 'discardOutput()'.");

        // flush data to serial port immediately
        com.pi4j.jni.Serial.discardOutput(fileDescriptor);
    }

    /**
     * <p>
     *     Discards any data in  both the serial receive and transmit buffers.
     *     Please note that this does not force the transmission of data, it discards it!
     * </p>
     *
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    @Override
    public void discardAll() throws IllegalStateException, IOException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'discardAll()'.");

        // flush data to serial port immediately
        com.pi4j.jni.Serial.discardAll(fileDescriptor);
    }

    /**
     * <p>
     *     Send a BREAK signal to connected device.
     * </p>
     *
     * @param duration
     *          The length of time (milliseconds) to send the BREAK signal
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    @Override
    public void sendBreak(int duration) throws IllegalStateException, IOException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'sendBreak()'.");

        // send BREAK signal to serial port immediately
        com.pi4j.jni.Serial.sendBreak(fileDescriptor, duration);
    }

    /**
     * <p>
     *     Send a BREAK signal to connected device for at least 0.25 seconds, and not more than 0.5 seconds
     * </p>
     *
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    @Override
    public void sendBreak() throws IllegalStateException, IOException{
        sendBreak(0);
    }

    /**
     * <p>
     *     Send a constant BREAK signal to connected device. (Turn break on/off)
     *     When enabled this will send a steady stream of zero bits.
     *     When enabled, no (other) data transmitting is possible.
     * </p>
     *
     * @param enabled
     *          The enable or disable state to control the BREAK signal
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    @Override
    public void setBreak(boolean enabled) throws IllegalStateException, IOException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'setBreak()'.");

        // control the constant state of the BREAK signal
        com.pi4j.jni.Serial.setBreak(fileDescriptor, enabled);
    }

    /**
     * <p>
     *     Control the RTS (request-to-send) pin state.
     *     When enabled this will set the RTS pin to the HIGH state.
     * </p>
     *
     * @param enabled
     *          The enable or disable state to control the RTS pin state.
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    @Override
    public void setRTS(boolean enabled) throws IllegalStateException, IOException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'setRTS()'.");

        // control the constant state of the RTS signal
        com.pi4j.jni.Serial.setRTS(fileDescriptor, enabled);
    }

    /**
     * <p>
     *     Control the DTR (data-terminal-ready) pin state.
     *     When enabled this will set the DTR pin to the HIGH state.
     * </p>
     *
     * @param enabled
     *          The enable or disable state to control the RTS pin state.
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    @Override
    public void setDTR(boolean enabled) throws IllegalStateException, IOException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'setDTR()'.");

        // control the constant state of the DTR signal
        com.pi4j.jni.Serial.setDTR(fileDescriptor, enabled);
    }

    /**
     * <p>
     *     Get the RTS (request-to-send) pin state.
     * </p>
     *
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    public boolean getRTS() throws IllegalStateException, IOException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'getRTS()'.");

        // get pin state
        return com.pi4j.jni.Serial.getRTS(fileDescriptor);
    }

    /**
     * <p>
     *     Get the DTR (data-terminal-ready) pin state.
     * </p>
     *
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    public boolean getDTR() throws IllegalStateException, IOException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'getDTR()'.");

        // get pin state
        return com.pi4j.jni.Serial.getDTR(fileDescriptor);
    }

    /**
     * <p>
     *     Get the CTS (clean-to-send) pin state.
     * </p>
     *
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    public boolean getCTS() throws IllegalStateException, IOException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'getCTS()'.");

        // get pin state
        return com.pi4j.jni.Serial.getCTS(fileDescriptor);
    }

    /**
     * <p>
     *     Get the DSR (data-set-ready) pin state.
     * </p>
     *
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    public boolean getDSR() throws IllegalStateException, IOException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'getDSR()'.");

        // get pin state
        return com.pi4j.jni.Serial.getDSR(fileDescriptor);
    }

    /**
     * <p>
     *     Get the RI (ring-indicator) pin state.
     * </p>
     *
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    public boolean getRI() throws IllegalStateException, IOException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'getRI()'.");

        // get pin state
        return com.pi4j.jni.Serial.getRI(fileDescriptor);
    }

    /**
     * <p>
     *     Get the CD (carrier-detect) pin state.
     * </p>
     *
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    public boolean getCD() throws IllegalStateException, IOException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'getCD()'.");

        // get pin state
        return com.pi4j.jni.Serial.getCD(fileDescriptor);
    }

    // ----------------------------------------
    // READ OPERATIONS
    // ----------------------------------------

    /**
     * Gets the number of bytes available for reading, or -1 for any error condition.
     *
     * @return Returns the number of bytes available for reading, or -1 for any error
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    @Override
    public int available() throws IllegalStateException, IOException {
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'available()'.");

        // get the number of available bytes in the serial port's receive buffer
        //return com.pi4j.jni.Serial.available(fileDescriptor);
        return receiveBuffer.getInputStream().available();
    }

    /**
     * <p>Reads all available bytes from the serial port/device.</p>
     *
     * @return Returns a byte array with the data read from the serial port.
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    @Override
    public byte[] read() throws IllegalStateException, IOException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'read()'.");

        // read serial data from receive buffer
        byte[] buffer = new byte[available()];
        receiveBuffer.getInputStream().read(buffer);
        return buffer;
    }

    /**
     * <p>Reads a length of bytes from the port/serial device.</p>
     *
     * @param length
     *          The number of bytes to get from the serial port/device.
     *          This number must not be higher than the number of available bytes.
     *
     * @return Returns a byte array with the data read from the serial port.
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    @Override
    public byte[] read(int length) throws IllegalStateException, IOException{
        // validate state
        if (isClosed())
            throw new IllegalStateException("Serial connection is not open; cannot 'read()'.");

        // read serial data from receive buffer
        byte[] buffer = new byte[length];
        receiveBuffer.getInputStream().read(buffer, 0 , length);
        return buffer;
    }


    // ----------------------------------------
    // WRITE OPERATIONS
    // ----------------------------------------

    /**
     * <p>Sends an array of bytes to the serial port/device identified by the given file descriptor.</p>
     *
     * @param data
     *            A ByteBuffer of data to be transmitted.
     * @param offset
     *            The starting index (inclusive) in the array to send from.
     * @param length
     *            The number of bytes from the byte array to transmit to the serial port.
     * @throws IllegalStateException thrown if the serial port is not already open.
     * @throws IOException thrown on any error.
     */
    @Override
    public void write(byte[] data, int offset, int length) throws IllegalStateException, IOException{
        // validate state
        if (isClosed()) {
            throw new IllegalStateException("Serial connection is not open; cannot 'write()'.");
        }

        // write serial data to transmit buffer
        com.pi4j.jni.Serial.write(fileDescriptor, data, offset, length);
    }


    // ----------------------------------------
    // EVENT OPERATIONS
    // ----------------------------------------

    /**
     * <p>Add Serial Event Listener</p>
     *
     * <p> Java consumer code can call this method to register itself as a listener for serial data
     * events. </p>
     *
     * @see com.pi4j.io.serial.SerialDataEventListener
     * @see com.pi4j.io.serial.SerialDataEvent
     *
     * @param listener  A class instance that implements the SerialListener interface.
     */
    @Override
    public synchronized void addListener(SerialDataEventListener... listener) {
        // add the new listener to the list of listeners
        Collections.addAll(listeners, listener);
    }

    /**
     * <p>Remove Serial Event Listener</p>
     *
     * <p> Java consumer code can call this method to unregister itself as a listener for serial data
     * events. </p>
     *
     * @see com.pi4j.io.serial.SerialDataEventListener
     * @see com.pi4j.io.serial.SerialDataEvent
     *
     * @param listener A class instance that implements the SerialListener interface.
     */
    @Override
    public synchronized void removeListener(SerialDataEventListener... listener) {
        // remove the listener from the list of listeners
        for (SerialDataEventListener lsnr : listener) {
            listeners.remove(lsnr);
        }
    }

    /**
     * This method returns the serial device file descriptor
     * @return fileDescriptor file descriptor
     */
    @Override
    public int getFileDescriptor() {
        return fileDescriptor;
    }

    /**
     * This method returns the input data stream for the serial port's receive buffer
     * @return InputStream input stream
     */
    @Override
    public InputStream getInputStream() {
        return receiveBuffer.getInputStream();
    }

    /**
     * This method returns the output data stream for the serial port's transmit buffer
     * @return OutputStream output stream
     */
    @Override
    public OutputStream getOutputStream() {
        return new SerialOutputStream();
    }


    /**
     * This method returns the buffering state for data received from the serial device/port.
     * @return 'true' if buffering is enabled; else 'false'
     */
    @Override
    public boolean isBufferingDataReceived(){
        return bufferingDataReceived;
    }

    /**
     * <p>
     *     This method controls the buffering state for data received from the serial device/port.
     * </p>
     * <p>
     *   If the buffering state is enabled, then all data bytes received from the serial port will
     *   get copied into a data receive buffer.  You can use the 'getInputStream()' or and of the 'read()'
     *   methods to access this data.  The data will also be available via the 'SerialDataEvent' event.
     *   It is important to know that if you are using data buffering, the data will continue to grow
     *   in memory until your program consume it from the data reader/stream.
     * </p>
     * <p>
     *   If the buffering state is disabled, then all data bytes received from the serial port will NOT
     *   get copied into the data receive buffer, but will be included in the 'SerialDataEvent' event's
     *   data payload.  If you program does not care about or use data received from the serial port,
     *   then you should disable the data buffering state to prevent memory waste/leak.
     * </p>
     *
     * @param enabled sets the buffering behavior state
     */
    @Override
    public void setBufferingDataReceived(boolean enabled){
        bufferingDataReceived = enabled;
    }


    private class SerialOutputStream extends OutputStream {

        @Override
        public void write(byte b[]) throws IOException {
            SerialImpl.this.write(b);
        }

        @Override
        public void write(int b) throws IOException {
            SerialImpl.this.write((byte)b);
        }

        public void write(byte b[], int offset, int length) throws IOException {
            SerialImpl.this.write(b, offset, length);
        }

        @Override
        public void flush() throws IOException {
            SerialImpl.this.flush();
        }
    }

    @SuppressWarnings("unused")
	private class SerialInputStream extends InputStream {

        @Override
        public int read() throws IOException {
            return 0;
        }
    }
}
