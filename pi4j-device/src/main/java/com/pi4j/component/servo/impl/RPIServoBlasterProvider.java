package com.pi4j.component.servo.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  RPIServoBlasterProvider.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2017 Pi4J
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pi4j.component.servo.ServoDriver;
import com.pi4j.component.servo.ServoProvider;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;


/**
 * Implementation of https://github.com/richardghirst/PiBits/tree/master/ServoBlaster
 *
 *
 * @author Daniel Sendula
 */
public class RPIServoBlasterProvider implements ServoProvider {

    // Default servo mapping of ServoBlaster's servod:
    //
    //    0 on P1-7           GPIO-4
    //    1 on P1-11          GPIO-17
    //    2 on P1-12          GPIO-18
    //    3 on P1-13          GPIO-27
    //    4 on P1-15          GPIO-22
    //    5 on P1-16          GPIO-23
    //    6 on P1-18          GPIO-24
    //    7 on P1-22          GPIO-25

    public static final String PIN_P1_3  = "P1-3";
    public static final String PIN_P1_5  = "P1-5";
    public static final String PIN_P1_7  = "P1-7";
    public static final String PIN_P1_11 = "P1-11";
    public static final String PIN_P1_12 = "P1-12";
    public static final String PIN_P1_13 = "P1-13";
    public static final String PIN_P1_15 = "P1-15";
    public static final String PIN_P1_16 = "P1-16";
    public static final String PIN_P1_18 = "P1-18";
    public static final String PIN_P1_19 = "P1-19";
    public static final String PIN_P1_21 = "P1-21";
    public static final String PIN_P1_22 = "P1-22";
    public static final String PIN_P1_23 = "P1-23";
    public static final String PIN_P1_24 = "P1-24";
    public static final String PIN_P1_25 = "P1-25";
    public static final String PIN_P1_26 = "P1-26";

    public static final String PIN_P5_3 = "P5-3";
    public static final String PIN_P5_4 = "P5-4";
    public static final String PIN_P5_5 = "P5-5";
    public static final String PIN_P5_6 = "P5-6";

    public static Map<Pin, String> PIN_MAP;
    public static Map<String, Pin> REVERSE_PIN_MAP;

    static {
        PIN_MAP = new HashMap<Pin, String>();
        REVERSE_PIN_MAP = new HashMap<String, Pin>();
        definePin(RaspiPin.GPIO_08, PIN_P1_3);
        definePin(RaspiPin.GPIO_09, PIN_P1_5);
        definePin(RaspiPin.GPIO_07, PIN_P1_7);
        definePin(RaspiPin.GPIO_00, PIN_P1_11);
        definePin(RaspiPin.GPIO_01, PIN_P1_12);
        definePin(RaspiPin.GPIO_02, PIN_P1_13);
        definePin(RaspiPin.GPIO_03, PIN_P1_15);
        definePin(RaspiPin.GPIO_04, PIN_P1_16);
        definePin(RaspiPin.GPIO_05, PIN_P1_18);
        definePin(RaspiPin.GPIO_12, PIN_P1_19);
        definePin(RaspiPin.GPIO_13, PIN_P1_21);
        definePin(RaspiPin.GPIO_06, PIN_P1_22);
        definePin(RaspiPin.GPIO_14, PIN_P1_23);
        definePin(RaspiPin.GPIO_10, PIN_P1_24);
        definePin(RaspiPin.GPIO_11, PIN_P1_26);

        definePin(RaspiPin.GPIO_17, PIN_P5_3);
        definePin(RaspiPin.GPIO_18, PIN_P5_4);
        definePin(RaspiPin.GPIO_19, PIN_P5_5);
        definePin(RaspiPin.GPIO_20, PIN_P5_6);
    }

    static void definePin(Pin pin, String s) {
        PIN_MAP.put(pin, s);
        REVERSE_PIN_MAP.put(s, pin);
    }

    public static final String SERVO_BLASTER_DEV = "/dev/servoblaster";
    public static final String SERVO_BLASTER_DEV_CFG = "/dev/servoblaster-cfg";

    protected File servoBlasterDev;
    protected File servoBlasterDevCfg;

    protected Writer writer;

    protected Map<Pin, RPIServoBlasterServoDriver> allocatedDrivers = new HashMap<Pin, RPIServoBlasterServoDriver>();

    /**
     * Constructor. It checks if /dev/servoblaster file exists.
     *
     * @throws IOException thrown in case file /dev/servoblaster does not exist.
     */
    public RPIServoBlasterProvider() throws IOException {
        servoBlasterDev = new File(SERVO_BLASTER_DEV);
        if (!servoBlasterDev.exists()) {
            throw new FileNotFoundException("File " + SERVO_BLASTER_DEV + " is not present." +
                    " Please check https://github.com/richardghirst/PiBits/tree/master/ServoBlaster for details.");
        }
        servoBlasterDevCfg = new File(SERVO_BLASTER_DEV_CFG);
        if (!servoBlasterDevCfg.exists()) {
            throw new FileNotFoundException("File " + SERVO_BLASTER_DEV_CFG + " is not present." +
                    " Please check https://github.com/richardghirst/PiBits/tree/master/ServoBlaster for details.");
        }

    }

    public List<Pin> getDefinedServoPins() throws IOException {
        List<Pin> servoPins = new ArrayList<Pin>();
        FileReader in = new FileReader(servoBlasterDevCfg);
        try {
            @SuppressWarnings("unused")
            String p1pins = null;
            @SuppressWarnings("unused")
            String p5pins = null;
            boolean mappingStarted = false;

            @SuppressWarnings("resource")
            BufferedReader reader = new BufferedReader(in);

            String line = reader.readLine();
            while (line != null) {
                if (mappingStarted) {
                    line = line.trim();
                    int i = line.indexOf(" on ");
                    if (i > 0) {
                        try {
                            int index = Integer.parseInt(line.substring(0, i));
                            String pin = line.substring(i + 4).trim();
                            i = pin.indexOf(' ');
                            pin = pin.substring(0, i);

                            Pin gpio = REVERSE_PIN_MAP.get(pin);
                            if (gpio != null) {
                                if (index == servoPins.size()) {
                                    servoPins.add(gpio);
                                } else if (index > servoPins.size()) {
                                    while (servoPins.size() < index) {
                                        servoPins.add(null);
                                    }
                                    servoPins.add(gpio);
                                } else {
                                    servoPins.set(index, gpio);
                                }
                            } else {
                                System.err.println("Unrecognised pin " + pin);
                            }

                        } catch (NumberFormatException ignore) { }
                    }
                } else {
                    if (line.startsWith("p1pins=")) {
                        p1pins = line.substring(7);
                    }
                    if (line.startsWith("p5pins=")) {
                        p5pins = line.substring(7);
                    }
                    if (line.trim().equals("Servo mapping:")) {
                        mappingStarted = true;
                    }
                }
                line = reader.readLine();
            }
        } finally {
            try {
                in.close();
            } catch (IOException ignore) {
            }
        }
        return servoPins;
    }

    /**
     * Returns new instance of {@link RPIServoBlasterServoDriver}.
     *
     * @param servoPin servo pin.
     * @return instance of {@link RPIServoBlasterServoDriver}.
     */
    public synchronized ServoDriver getServoDriver(Pin servoPin) throws IOException {
        List<Pin> servoPins = getDefinedServoPins();
        int index = servoPins.indexOf(servoPin);
        if (index < 0) {
            throw new IllegalArgumentException("Servo driver cannot drive pin " + servoPin);
        }

        RPIServoBlasterServoDriver driver = allocatedDrivers.get(servoPin);
        if (driver == null) {
            driver = new RPIServoBlasterServoDriver(servoPin, index, PIN_MAP.get(servoPin), this);
            ensureWriterIsCreated();
        }

        return driver;
    }

    protected synchronized void ensureWriterIsCreated() throws IOException {
        if (writer == null) {
            // Not really singleton, but it will work...
            writer = new FileWriter(servoBlasterDev);
        }
    }

    protected synchronized void updateServo(String pinName, int value) {
        StringBuilder b = new StringBuilder();
        b.append(pinName).append('=').append(Integer.toString(value)).append('\n');
        try {
            writer.write(b.toString());
            writer.flush();
        } catch (IOException e) {
            try {
                writer.close();
            } catch (IOException ignore) { }
        }
        try {
            ensureWriterIsCreated();
            writer.write(b.toString());
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to /dev/servoblaster device", e);
        }
    }
}
