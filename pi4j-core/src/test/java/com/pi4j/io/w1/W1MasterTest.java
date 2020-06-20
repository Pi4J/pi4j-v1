package com.pi4j.io.w1;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  W1MasterTest.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2020 Pi4J
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


import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.*;

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
        assertTrue(dummyDevice instanceof W1DummyDeviceType.W1DummyDevice);
        assertEquals("FE-00000698ebb3 Dummy Device", dummyDevice.getName());
    }

    @Test
    public void testCheckDeviceChanges() throws URISyntaxException {
        W1MasterDummy master = new W1MasterDummy();

        assertEquals(0, master.getDevices().size());
        final DummyDevice d1 = new DummyDevice("1");
        final DummyDevice d2 = new DummyDevice("2");
        master.setReadDevices(Arrays.<W1Device>asList(d1, d2));
        assertEquals(0, master.getDevices().size());

        master.checkDeviceChanges();
        assertEquals(2, master.getDevices().size());

        final DummyDevice d3 = new DummyDevice("3");
        final DummyDevice d4 = new DummyDevice("4");
        master.setReadDevices(Arrays.<W1Device>asList(d1, d3, d4));
        master.checkDeviceChanges();
        List<W1Device> devices = master.getDevices();
        assertEquals(3, devices.size());
        assertTrue(devices.contains(d1));
        assertTrue(devices.contains(d3));
        assertTrue(devices.contains(d4));

        master.checkDeviceChanges();
        assertEquals(3, master.getDevices().size());

        master.setReadDevices(Collections.<W1Device>emptyList());
        master.checkDeviceChanges();
        assertEquals(0, master.getDevices().size());



    }

    @Test
    public void thereShouldBeNoDevicesForFamily28() {
        assertEquals(0, master.getDevices(0x28).size());
    }

    @Test
    public void thereShouldBeOneDummyDevicesForFamilyFE() {
        assertEquals(1, master.getDevices(W1DummyDeviceType.FAMILY_ID).size());
    }

    static class DummyDevice extends W1BaseDevice {

        public DummyDevice(String id) {
            super(new File("FD-" + id));
        }

        @Override
        public int getFamilyId() {
            return 0xFD;
        }

        @Override
        public String toString() {
            return getId();
        }
    }

    static class W1MasterDummy extends W1Master {


        List<W1Device> readDevices = new ArrayList<>();

        public W1MasterDummy() {
            super("");
        }

        @SuppressWarnings("unchecked")
		@Override
        <T extends W1Device> List<T> readDevices() {
            if (readDevices == null) { // during construction
                readDevices = new ArrayList<>();
            }
            return (List<T>) readDevices;
        }

        public void setReadDevices(List<W1Device> readDevices) {
            this.readDevices = new ArrayList<>();
            this.readDevices.addAll(readDevices);
        }
    }
}
