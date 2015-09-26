package com.pi4j.component.temperature.impl;

import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.component.temperature.TemperatureSensorBase;
import com.pi4j.io.w1.W1Device;
import com.pi4j.io.w1.W1Master;
import com.pi4j.temperature.TemperatureScale;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.util.IllegalFormatException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
* #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  TmpDS18B20.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
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
public class TmpDS18B20 extends TemperatureSensorBase implements TemperatureSensor, W1Device {

    private final Logger log = Logger.getLogger(TmpDS18B20.class.getName());

    private String name;

    private File deviceDir;

    private double lastGoodTemperature = Double.NaN;

    // (?s).*crc=[0-9a-f]+ ([A-Z]+).*t=([0-9]+)
    private final Pattern VALUE_PATTERN = Pattern.compile("(?s).*crc=[0-9a-f]+ (?<success>[A-Z]+).*t=(?<temp>[0-9]+)");

    @Override
    public String getName() {
        return name;
    }

    public TmpDS18B20(final File deviceDir) {
        try {
            name = new String(Files.readAllBytes(new File(deviceDir, "name").toPath())).trim();
        } catch (IOException e) {
            // FIXME logging
            name = deviceDir.getName();
        }
        this.deviceDir = deviceDir;
    }

    @Override
    public double getTemperature() {
        double temperature = lastGoodTemperature;
        try {
            String value = getValue();
            temperature = parseValue(value);
        } catch (Exception e) {
            log.warning("Error reading temperature - returning last known temperature - " + e.toString());
        }

        return temperature;
    }

    private double parseValue(final String value) throws Exception {
        double result = Double.NaN;
        /*
        53 01 4b 46 7f ff 0d 10 e9 : crc=e9 YES
        53 01 4b 46 7f ff 0d 10 e9 t=21187
         */
        final Matcher matcher = VALUE_PATTERN.matcher(value.trim());
        if (matcher.matches()) {
            if (matcher.group("success").equals("YES")) {
                int tempValue = Integer.valueOf(matcher.group("temp"));

                // rounding: DS18B20 is +/- 0.5 degree Celsius - no point in being too detailed
                BigDecimal bd = BigDecimal.valueOf(tempValue);
                bd = bd.movePointLeft(3);
                bd = bd.setScale(1, RoundingMode.HALF_UP);
                result = bd.doubleValue();
            } else {
                throw new Exception("temperature value is not valid: " + value);
            }
        } else {
            throw new Exception("value regex didn't match");
        }
        return result;
    }

    @Override
    public TemperatureScale getScale() {
        return TemperatureScale.CELSIUS;
    }

    @Override
    public String getId() {
        return deviceDir.getName();
    }


    @Override
    public String getValue() throws IOException {
        return new String(Files.readAllBytes(new File(deviceDir, "w1_slave").toPath()));
    }
}
