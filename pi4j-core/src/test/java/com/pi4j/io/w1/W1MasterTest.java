package com.pi4j.io.w1;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  W1MasterTest.java  
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


import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Peter Schuebl
 */
public class W1MasterTest {

    private W1Master master;

    @Before
    public void setupMaster() throws URISyntaxException {
        master = new W1Master(new File(W1MasterTest.class.getResource("/w1/sys/bus/w1/devices").toURI()).toString());
    }

    @Test
    public void shouldAtLeastContainADummyDevice() {
        final Collection<W1DeviceType> deviceTypes = master.getDeviceTypes();
        assertTrue("there should be at least one device", deviceTypes.size() >= 1);
    }

    @Test
    public void thereShouldBeThreeDevices() {
        final List<String> deviceIDs = master.getDeviceIDs();
        assertEquals(3, deviceIDs.size());
        assertTrue(deviceIDs.contains("28-00000698ebb1"));
        assertTrue(deviceIDs.contains("28-00000698ebb2"));
    }

    @Test
    public void thereShouldBeADummyDevice() {
        final List<W1Device> devices = master.getDevices();
        assertEquals(1, devices.size());
        final W1Device dummyDevice = devices.get(0);
        assertTrue(dummyDevice instanceof W1DummyDevice);
        assertEquals("FE-00000698ebb3 Dummy Device", dummyDevice.getName());
    }

    @Test
    public void thereShouldBeNoDevicesForFamily28() {
        assertEquals(0, master.getDevices("28").size());
    }

    @Test
    public void thereShouldBeOneDummyDevicesForFamilyFE() {
        assertEquals(1, master.getDevices("FE").size());
    }
}
