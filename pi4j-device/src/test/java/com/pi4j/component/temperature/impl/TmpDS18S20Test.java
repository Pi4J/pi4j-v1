package com.pi4j.component.temperature.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  TmpDS18S20Test.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2020 Pi4J
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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.io.w1.W1Device;
import com.pi4j.io.w1.W1Master;

/**
 * @author Peter Schuebl
 */
public class TmpDS18S20Test {
    private W1Master master;

    @Before
    public void setupMaster() throws URISyntaxException {
        master = new W1Master(new File(TmpDS18S20Test.class.getResource("/w1/sys/bus/w1/devices").toURI()).toString());
    }

    @Test
    public void testDevices() {
        //System.out.println(master.toString());
        final List<W1Device> devices = master.getDevices(TmpDS18S20DeviceType.FAMILY_CODE);
        assertEquals(2, devices.size());
        for (W1Device device : devices) {
            //System.out.println(((TemperatureSensor) device).getTemperature());
            assertEquals(-1.3d, (((TemperatureSensor) device).getTemperature()), 0.01d);
        }
    }

    public void testName() throws URISyntaxException {
        final String id = "10-000801d54852";
        TmpDS18S20DeviceType.TmpDS18S20 device = createDevice(id);
        device.setName("My Sensor");

        assertEquals("My Sensor", device.getName());
        assertEquals(id, device.getId());
    }

    private TmpDS18S20DeviceType.TmpDS18S20 createDevice(String id) throws URISyntaxException {
        final TmpDS18S20DeviceType deviceType = new TmpDS18S20DeviceType();
        final URI uri = TmpDS18S20Test.class.getResource("/w1/sys/bus/w1/devices/" + id).toURI();
        final File deviceDir = new File(uri);
        return deviceType.create(deviceDir);
    }

    @SuppressWarnings("unlikely-arg-type")
	@Test
    public void testEquals() throws Exception {
        final W1Device w1Devicea1 = createDevice("10-000801d54852");
        final W1Device w1Devicea2 = createDevice("10-000801d54852");

        assertTrue(w1Devicea1.equals(w1Devicea2));

        final W1Device w1Deviceb = createDevice("10-000801d54853");
        assertFalse(w1Devicea1.equals(w1Deviceb));
        assertFalse(w1Devicea1.equals(null));

        assertFalse(w1Devicea1.equals("123"));
    }

    @Test
    public void testHashCode() throws Exception {
        final W1Device w1Devicea1 = createDevice("10-000801d54852");
        final W1Device w1Devicea2 = createDevice("10-000801d54852");
        assertEquals(w1Devicea1.hashCode(), w1Devicea2.hashCode());

        final W1Device w1Deviceb = createDevice("10-000801d54853");
        assertNotEquals(w1Devicea1.hashCode(), w1Deviceb.hashCode());
    }
}
