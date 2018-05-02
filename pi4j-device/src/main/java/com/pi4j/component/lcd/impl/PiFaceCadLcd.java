/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pi4j.component.lcd.impl;

/*-
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  PiFaceCadLcd.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2018 Pi4J
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
 *
 */
import com.pi4j.component.lcd.LCD;
import com.pi4j.component.lcd.LCDBase;
import com.pi4j.gpio.extension.mcp.MCP23S17GpioProvider;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import java.io.IOException;
import java.util.ArrayList;
import jdk.nashorn.internal.runtime.ECMAException;

/**
 * The PiFace Control and Display LCD implementation
 *
 * The original C implementation can be found http://piface.github.io/libpifacecad/
 *
 * The used pins are
 * <PRE>
 * pins
 * 2  (5V)
 * 17 (3.3V)
 * 16 (GPIO 23)  => 4 (Infrared)
 * 19 (SPI MOSI) => 12
 * 21 (SPI MISO) => 13
 * 22 (GPIO 06)  => 6 (button interrurption)
 * 23 (SPI SCLK) => 14
 * 25 (Ground)
 * 26 (CE1 SPI CS Line) => 11
 * </PRE>
 *
 * The infrared is not yet implemented.
 *
 * The implementation will use a dispatch thread with the buttons events to
 * send to listener, the pooling in 500ms.
 *
 * Direct calling of the listener will results in some garbage on the screen
 * if the calls are to near, so the working polling approach was implemented.
 *
 * If there is another reliable way to fire events to listener, please submit
 * a pull request.
 * @author sbodmer
 */
public class PiFaceCadLcd extends LCDBase implements Runnable, LCD, GpioPinListenerDigital {

    /**
     * Copied from MCP23S17GpioProvider.java
     */
    protected static final byte REGISTER_IODIR_A = 0x00;
    protected static final byte REGISTER_IODIR_B = 0x01;
    protected static final byte REGISTER_GPINTEN_A = 0x04;
    protected static final byte REGISTER_GPINTEN_B = 0x05;
    protected static final byte REGISTER_DEFVAL_A = 0x06;
    protected static final byte REGISTER_DEFVAL_B = 0x07;
    protected static final byte REGISTER_INTCON_A = 0x08;
    protected static final byte REGISTER_INTCON_B = 0x09;
    protected static final byte REGISTER_IOCON_A = 0x0A;
    protected static final byte REGISTER_IOCON_B = 0x0B;
    protected static final byte REGISTER_GPPU_A = 0x0C;
    protected static final byte REGISTER_GPPU_B = 0x0D;
    protected static final byte REGISTER_INTF_A = 0x0E;
    protected static final byte REGISTER_INTF_B = 0x0F;
    protected static final byte REGISTER_INTCAP_A = 0x10;
    protected static final byte REGISTER_INTCAP_B = 0x11;
    protected static final byte REGISTER_GPIO_A = 0x12;
    protected static final byte REGISTER_GPIO_B = 0x13;

    protected static final byte IOCON_UNUSED = (byte) 0x01;
    protected static final byte IOCON_INTPOL = (byte) 0x02;
    protected static final byte IOCON_ODR = (byte) 0x04;
    protected static final byte IOCON_HAEN = (byte) 0x08;
    protected static final byte IOCON_DISSLW = (byte) 0x10;
    protected static final byte IOCON_SEQOP = (byte) 0x20;
    protected static final byte IOCON_MIRROR = (byte) 0x40;
    protected static final byte IOCON_BANK_MODE = (byte) 0x80;

    protected static final int DELAY_PULSE_NS = 1000; // 1us
    protected static final int DELAY_SETTLE_NS = 40000; // 40us
    protected static final long DELAY_CLEAR_MS = 3L; // 2.6ms
    protected static final long DELAY_SETUP_0_MS = 15L; // 15ms
    protected static final long DELAY_SETUP_1_MS = 5L; // 5ms
    protected static final long DELAY_SETUP_2_MS = 1L; // 1ms

    protected static final byte SWITCH_PORT = REGISTER_GPIO_A;
    protected static final byte LCD_PORT = REGISTER_GPIO_B;

    /**
     * SPI Address Register  0b[0 1 0 0 A2 A1 A0 x]
     */
    public static final byte ADDRESS_0 = 0b01000000; // 0x40 [0100 0000] [A0 = 0 | A1 = 0 | A2 = 0]
    public static final byte ADDRESS_1 = 0b01000010; // 0x42 [0100 0010] [A0 = 1 | A1 = 0 | A2 = 0]
    public static final byte ADDRESS_2 = 0b01000100; // 0x44 [0100 0100] [A0 = 0 | A1 = 1 | A2 = 0]
    public static final byte ADDRESS_3 = 0b01000110; // 0x46 [0100 0110] [A0 = 1 | A1 = 1 | A2 = 0]
    public static final byte ADDRESS_4 = 0b01001000; // 0x48 [0100 1000] [A0 = 0 | A1 = 0 | A2 = 1]
    public static final byte ADDRESS_5 = 0b01001010; // 0x4A [0100 1010] [A0 = 1 | A1 = 0 | A2 = 1]
    public static final byte ADDRESS_6 = 0b01001100; // 0x4C [0100 1100] [A0 = 0 | A1 = 1 | A2 = 1]
    public static final byte ADDRESS_7 = 0b01001110; // 0x4E [0100 1110] [A0 = 1 | A1 = 1 | A2 = 1]
    public static final byte DEFAULT_ADDRESS = ADDRESS_0;

    /**
     * MCP23s17 GPIOB to HD44780 pin map
     */
    protected static final int PIN_D4 = 0;
    protected static final int PIN_D5 = 1;
    protected static final int PIN_D6 = 2;
    protected static final int PIN_D7 = 3;
    protected static final int PIN_ENABLE = 4;
    protected static final int PIN_RW = 5;
    protected static final int PIN_RS = 6;
    protected static final int PIN_BACKLIGHT = 7;

    /**
     * Commands
     */
    protected static final int LCD_CLEARDISPLAY = 0x01;
    protected static final int LCD_RETURNHOME = 0x02;
    protected static final int LCD_ENTRYMODESET = 0x04;
    protected static final int LCD_DISPLAYCONTROL = 0x08;
    protected static final int LCD_CURSORSHIFT = 0x10;
    protected static final int LCD_FUNCTIONSET = 0x20;
    protected static final int LCD_SETCGRAMADDR = 0x40;
    protected static final int LCD_SETDDRAMADDR = 0x80;
    protected static final int LCD_NEWLINE = 0xC0;

    /**
     * Flags for function set
     */
    protected static final byte LCD_8BITMODE = 0x10;
    protected static final byte LCD_4BITMODE = 0x00;
    protected static final byte LCD_2LINE = 0x08;
    protected static final byte LCD_1LINE = 0x00;
    protected static final byte LCD_5X10DOTS = 0x04;
    protected static final byte LCD_5X8DOTS = 0x00;

    /**
     * Flags for display on/off control
     */
    protected static final byte LCD_DISPLAYON = 0x04;
    protected static final byte LCD_DISPLAYOFF = 0x00;
    protected static final byte LCD_CURSORON = 0x02;
    protected static final byte LCD_CURSOROFF = 0x00;
    protected static final byte LCD_BLINKON = 0x01;
    protected static final byte LCD_BLINKOFF = 0x00;

    /**
     * Flags for display entry mode
     */
    protected static final byte LCD_ENTRYRIGHT = 0x00;
    protected static final byte LCD_ENTRYLEFT = 0x02;
    protected static final byte LCD_ENTRYSHIFTINCREMENT = 0x01;
    protected static final byte LCD_ENTRYSHIFTDECREMENT = 0x00;

    protected static final int LCD_MAX_LINES = 2;
    protected static final int LCD_WIDTH = 16;
    protected static final int LCD_RAM_WIDTH = 80; // RAM is 80 wide, split over two lines
    protected static final int ROW_OFFSETS[] = {0, 0x40};

    protected static final byte WRITE_FLAG = 0b00000000;    // 0x00
    protected static final byte READ_FLAG = 0b00000001;    // 0x01

    /**
     * The interrupt pin on main board
     */
    protected GpioPinDigitalInput pin6 = null;

    protected int rows = 2;
    protected int columns = 16;
    protected int currentAddress = 0;
    protected int currentDisplayControl = 0;
    protected int currentFunctionSet = 0;
    protected int currentModeSet = 0;
    protected int oldPressedButtons = 0;

    protected SpiDevice spi = null;
    protected PiFaceCadButtonsListener listener = null;
    protected byte address = DEFAULT_ADDRESS;

    /**
     * The event to dispatch to button listeners
     * <PRE>
     * [0] = pressed
     * [1] = released
     * </PRE>
     */
    protected ArrayList<int[]> queue = new ArrayList<>();

    /**
     * The event dispatcher (sequential to avoid problems)
     */
    protected Thread dispatcher = null;

    /**
     * The default PiFace is at CS1
     *
     * @throws IOException
     */
    public PiFaceCadLcd() throws IOException {
        this(SpiChannel.CS1);
    }

    /**
     * Default LCD is at SpiChannel.CS1
     *
     * The PIN 22 (pi4j => 6) will be set as input to get the button
     * interruption
     *
     * @param channel
     * @throws IOException
     */
    public PiFaceCadLcd(SpiChannel channel) throws IOException {
        spi = SpiFactory.getInstance(channel);

        //--- The below init code was taken from MCP23S17GpioProvider
        // write IO configuration
        write(REGISTER_IOCON_A, (byte) (IOCON_SEQOP | IOCON_HAEN));  // enable hardware address
        write(REGISTER_IOCON_B, (byte) (IOCON_SEQOP | IOCON_HAEN));  // enable hardware address

        // set all default pins directions
        // (1 = Pin is configured as an input.)
        // (0 = Pin is configured as an output.)
        // Set GPIO Port A as inputs (switches)
        write(REGISTER_IODIR_A, (byte) 0xff);
        // Set GPIO Port B as outputs (connected to HD44780)
        write(REGISTER_IODIR_B, (byte) 0x00);

        // set all default pin states
        write(REGISTER_GPIO_A, (byte) 0x00);
        write(REGISTER_GPIO_B, (byte) 0x00);

        // set all default pin pull up resistors
        // (1 = Pull-up enabled.)
        // (0 = Pull-up disabled.)
        // Set GPIO Port A as inputs (switches)
        write(REGISTER_GPPU_A, (byte) 0xff);
        write(REGISTER_GPPU_B, (byte) 0x00);

        // set all default pin interrupts
        // (if pin direction is input (1), then enable interrupt for pin)
        // (1 = Enable GPIO input pin for interrupt-on-change event.)
        // (0 = Disable GPIO input pin for interrupt-on-change event.)
        // enable interrupts
        write(REGISTER_GPINTEN_A, (byte) 0xff);
        write(REGISTER_GPINTEN_B, (byte) 0x00);

        // set all default pin interrupt default values
        // (comparison value registers are not used in this implementation)
        write(REGISTER_DEFVAL_A, (byte) 0x00);
        write(REGISTER_DEFVAL_B, (byte) 0x00);

        // set all default pin interrupt comparison behaviors
        // (1 = Controls how the associated pin value is compared for interrupt-on-change.)
        // (0 = Pin value is compared against the previous pin value.)
        write(REGISTER_INTCON_A, (byte) 0x00);
        write(REGISTER_INTCON_B, (byte) 0x00);

        //--- Additional configuration of the MCP23S17 for initalizing the
        //--- LCD display
        try {
            //--- Init LCD
            Thread.sleep(DELAY_SETUP_0_MS);
            write(LCD_PORT, (byte) 0x3);
            lcdPulseEnable();

            Thread.sleep(DELAY_SETUP_1_MS);
            write(LCD_PORT, (byte) 0x3);
            lcdPulseEnable();

            Thread.sleep(DELAY_SETUP_2_MS);
            write(LCD_PORT, (byte) 0x3);
            lcdPulseEnable();

            write(LCD_PORT, (byte) 0x2);
            lcdPulseEnable();

            currentFunctionSet = LCD_4BITMODE | LCD_2LINE | LCD_5X8DOTS;
            lcdSendCommand((byte) (LCD_FUNCTIONSET | currentFunctionSet));

            //--- Clear
            lcdSendCommand((byte) LCD_CLEARDISPLAY);
            Thread.sleep(DELAY_CLEAR_MS);

            currentModeSet = (LCD_ENTRYLEFT | LCD_ENTRYSHIFTDECREMENT);
            lcdSendCommand((byte) (LCD_ENTRYMODESET | currentModeSet));

            //--- On display
            currentDisplayControl = (LCD_DISPLAYON | LCD_CURSOROFF | LCD_BLINKOFF);
            lcdSendCommand((byte) (LCD_DISPLAYCONTROL | currentDisplayControl));

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        GpioController gpio = GpioFactory.getInstance();

        pin6 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_06, "Interrupt");
        pin6.setShutdownOptions(Boolean.TRUE);
        // pin6.setDebounce(10);
        pin6.addListener(this);

        dispatcher = new Thread(this);
        dispatcher.start();
    }

    //**************************************************************************
    //*** LCD
    //**************************************************************************
    @Override
    public int getRowCount() {
        return rows;
    }

    @Override
    public int getColumnCount() {
        return columns;
    }

    @Override
    public void setCursorPosition(int row, int column) {
        try {
            int x = max(0, min(column, (LCD_RAM_WIDTH / 2) - 1));
            int y = max(0, min(row, LCD_MAX_LINES - 1));
            currentAddress = colrow2address(x, y);
            lcdSendCommand((byte) (LCD_SETDDRAMADDR | currentAddress));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void clear() {
        try {
            lcdSendCommand((byte) LCD_CLEARDISPLAY);
            Thread.sleep(DELAY_CLEAR_MS);
            currentAddress = 0;

        } catch (Exception ex) {
            //---
        }
    }

    @Override
    public void write(byte data) {
        try {
            lcdSendCommand((byte) (LCD_SETDDRAMADDR | currentAddress));
            if (data == '\n') {
                setCursorPosition(1, 0);

            } else {
                lcdSendData(data);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //**************************************************************************
    //*** API
    //**************************************************************************

    public void setButtonsListener(PiFaceCadButtonsListener listener) {
        this.listener = listener;

    }

    public int readSwitches() throws IOException {
        return read(SWITCH_PORT);
    }

    public int readSwitch(int switchNum) throws IOException {
        return (read(SWITCH_PORT) >> switchNum) & 0x1;
    }

    public void setBackLight(boolean state) throws IOException {
        writeBit(LCD_PORT, state, PIN_BACKLIGHT);

    }

    public void setBlink(boolean state) throws IOException, InterruptedException {
        if (state) {
            currentDisplayControl |= LCD_BLINKON;

        } else {
            currentDisplayControl &= 0xff ^ LCD_BLINKON;

        }
        lcdSendCommand((byte) (LCD_DISPLAYCONTROL | currentDisplayControl));
    }

    public void setCursor(boolean state) throws IOException, InterruptedException {
        if (state) {
            currentDisplayControl |= LCD_CURSORON;

        } else {
            currentDisplayControl &= 0xff ^ LCD_CURSORON;

        }
        lcdSendCommand((byte) (LCD_DISPLAYCONTROL | currentDisplayControl));
    }

    public void close() throws IOException {
        dispatcher.interrupt();

        pin6.removeAllListeners();

        // disable interrupts if enabled
        int intenb = read(REGISTER_GPINTEN_A);
        if (intenb != 0) {
            write(REGISTER_GPINTEN_A, (byte) 0x00);

        }

    }

    //**************************************************************************
    //*** GpioPinDigitial listener
    //**************************************************************************
    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        // display pin state on console
        try {
            // System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
            int pressed = 255 - readSwitches();
            if (listener != null) {
                if (event.getState().isLow()) {
                    queue.add(new int[] {pressed, oldPressedButtons & ~pressed});
                    oldPressedButtons = pressed;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //**************************************************************************
    //*** Protected
    //**************************************************************************
    protected synchronized void write(byte register, byte data) throws IOException {
        // create packet in data buffer
        byte packet[] = new byte[3];
        packet[0] = (byte) (address | WRITE_FLAG);   // address byte
        packet[1] = register;                     // register byte
        packet[2] = data;                         // data byte

        // send data packet
        spi.write(packet);
    }

    protected synchronized int read(byte register) throws IOException {
        // create packet in data buffer
        byte packet[] = new byte[3];
        packet[0] = (byte) (address | READ_FLAG);   // address byte
        packet[1] = register;                    // register byte
        packet[2] = 0b00000000;                  // data byte

        byte[] result = spi.write(packet);

        // (include the '& 0xFF' to ensure the bits in the unsigned byte are cast properly)
        return result[2] & 0xff;
    }

    //--------------------------------------------------------------------------
    //--- The below code was translated to java from the C library implementation
    //--- avaiable from PiFace site
    //--------------------------------------------------------------------------
    /* pulse the enable pin */
    protected void lcdPulseEnable() throws InterruptedException, IOException {
        lcdSetEnable(true);
        Thread.sleep(0,DELAY_PULSE_NS);
        lcdSetEnable(false);
        Thread.sleep(0,DELAY_PULSE_NS);
    }

    protected void lcdSetEnable(boolean state) throws IOException {
        writeBit(LCD_PORT, state, PIN_ENABLE);
    }

    protected void writeBit(byte register, boolean on, int bit_num) throws IOException {
        int regData = read(register);
        if (on) {
            regData |= 1 << bit_num; // set

        } else {
            regData &= 0xff ^ (1 << bit_num); // clear
        }
        write(register, (byte) regData);

    }

    protected void lcdSendCommand(byte command) throws IOException, InterruptedException {
        lcdSetRs(false);
        lcdSendByte(command);
        Thread.sleep(0,DELAY_SETTLE_NS);
    }

    protected void lcdSetRs(boolean state) throws IOException {
        writeBit(LCD_PORT, state, PIN_RS);
    }

    /**
     * Send a byte to LCD port
     *
     * @param b
     * @throws IOException
     * @throws InterruptedException
     */
    protected void lcdSendByte(byte b) throws IOException, InterruptedException {
        // get current lcd port state and clear the data bits
        int currentState = read(LCD_PORT);
        currentState &= 0xf0; // clear the data bits

        // send first nibble (0bXXXX0000)
        int newByte = currentState | ((b >> 4) & 0xf);  // set nibble
        write(LCD_PORT, (byte) newByte);
        lcdPulseEnable();

        // send second nibble (0b0000XXXX)
        newByte = currentState | (b & 0xf);  // set nibble
        write(LCD_PORT, (byte) newByte);
        lcdPulseEnable();
    }

    protected void lcdSendData(byte data) throws IOException, InterruptedException {
        lcdSetRs(true);
        lcdSendByte(data);
        currentAddress++;
        Thread.sleep(0,DELAY_SETTLE_NS);
    }

    protected static int max(int a, int b) {
        return a > b ? a : b;
    }

    protected static int min(int a, int b) {
        return a < b ? a : b;
    }

    protected int colrow2address(int col, int row) {
        return col + ROW_OFFSETS[row];
    }

    protected int address2col(int address) {
        return address % ROW_OFFSETS[1];
    }

    protected int address2row(int address) {
        return address > ROW_OFFSETS[1] ? 1 : 0;
    }

    //**************************************************************************
    //*** Public interface for buttons feedback
    //**************************************************************************
    public interface PiFaceCadButtonsListener {

        public static int BUTTON_1 = 1;
        public static int BUTTON_2 = 2;
        public static int BUTTON_3 = 4;
        public static int BUTTON_4 = 8;
        public static int BUTTON_5 = 16;
        public static int BUTTON_MIDDLE = 32;
        public static int BUTTON_LEFT = 64;
        public static int BUTTON_RIGHT = 128;

        /**
         * Specified buttons which were pressed or released
         *
         * Bitwise mask with the button constant to get which one are pressed
         * and released
         *
         * @param buttons
         */
        public void pifaceButtonsEvent(int pressed, int released);


    }


    /**
     * Dispatcher thread (needed to avoid to short delays between events)
     */
    public void run() {
        try {
            while (dispatcher.isInterrupted() == false) {
                if (!queue.isEmpty()) {
                    int ev[] = queue.remove(0);
                    if (listener != null) listener.pifaceButtonsEvent(ev[0],ev[1]);

                }
                dispatcher.sleep(500);

            }

        } catch (InterruptedException ex) {
            //---
        }
    }
}
