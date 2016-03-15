package com.pi4j.component.temperature.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  TmpDS18B20Test.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
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


import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.io.w1.W1Device;
import com.pi4j.io.w1.W1Master;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Peter Schuebl
 */
public class TmpDS18B20Test {
    private W1Master master;

    @Before
    public void setupMaster() throws URISyntaxException {
        master = new W1Master(new File(TmpDS18B20Test.class.getResource("/w1/sys/bus/w1/devices").toURI()).toString());
    }

    @Test
    public void testDevices() {
        //System.out.println(master.toString());
        final List<W1Device> devices = master.getDevices(TmpDS18B20DeviceType.FAMILY_CODE);
        assertEquals(2, devices.size());
        for (W1Device device : devices) {
            //System.out.println(((TemperatureSensor) device).getTemperature());
            assertTrue((((TemperatureSensor) device).getTemperature()) > 20.0);
        }
    }

    public void testName() throws URISyntaxException {
        final String id = "28-00000698ebb1";
        TmpDS18B20DeviceType.TmpDS18B20 device = createDevice(id);
        device.setName("My Sensor");

        assertEquals("My Sensor", device.getName());
        assertEquals(id, device.getId());

    }

    private TmpDS18B20DeviceType.TmpDS18B20 createDevice(String id) throws URISyntaxException {
        final TmpDS18B20DeviceType deviceType = new TmpDS18B20DeviceType();
        final URI uri = TmpDS18B20Test.class.getResource("/w1/sys/bus/w1/devices/" + id).toURI();
        final File deviceDir = new File(uri);
        return deviceType.create(deviceDir);
    }

    @Test
    public void testEquals() throws Exception {
        final W1Device w1Devicea1 = createDevice("28-00000698ebb1");
        final W1Device w1Devicea2 = createDevice("28-00000698ebb1");

        assertTrue(w1Devicea1.equals(w1Devicea2));

        final W1Device w1Deviceb = createDevice("28-00000698ebb2");
        assertFalse(w1Devicea1.equals(w1Deviceb));
        assertFalse(w1Devicea1.equals(null));

        assertFalse(w1Devicea1.equals("123"));
    }

    @Test
    public void testHashCode() throws Exception {
        final W1Device w1Devicea1 = createDevice("28-00000698ebb1");
        final W1Device w1Devicea2 = createDevice("28-00000698ebb1");
        assertEquals(w1Devicea1.hashCode(), w1Devicea2.hashCode());

        final W1Device w1Deviceb = createDevice("28-00000698ebb2");
        assertNotEquals(w1Devicea1.hashCode(), w1Deviceb.hashCode());
    }
}
