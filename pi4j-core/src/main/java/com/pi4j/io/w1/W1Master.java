package com.pi4j.io.w1;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;
import java.util.logging.Logger;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  W1Master.java  
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
public class W1Master {

    private final Logger log = Logger.getLogger(W1Master.class.getName());

    private final List<W1DeviceType> deviceTypes = new ArrayList<>();

    private final Map<String, W1DeviceType> deviceTypeMap = new LinkedHashMap<>();

    private File masterDir = new File("/sys/bus/w1/devices");

    /**
     * Create an instance of the W1 master.
     * Typically there should only be one master.
     *
     * java.util.ServiceLoader is used to add device support for individual devices.
     */
    public W1Master() {
        init();
    }

    /**
     * Create a master with a different default dir e.g. for tests.
     * @param masterDir
     */
    public W1Master(final String masterDir) {
        this.masterDir = new File(masterDir);
        init();
    }

    private void init() {
        final ServiceLoader<W1DeviceType> w1DeviceTypes = ServiceLoader.load(W1DeviceType.class);
        final Iterator<W1DeviceType> w1DeviceTypeIterator = w1DeviceTypes.iterator();
        while (w1DeviceTypeIterator.hasNext()) {
            final W1DeviceType w1DeviceType = w1DeviceTypeIterator.next();
            deviceTypes.add(w1DeviceType);
            final String deviceFamily = Integer.toHexString(w1DeviceType.getDeviceFamilyCode()).toUpperCase();
            deviceTypeMap.put(deviceFamily, w1DeviceType);
        }
    }

    /**
     * Gets a list of the available device types.
     * @return
     */
    public Collection<W1DeviceType> getDeviceTypes() {
        return deviceTypes;
    }

    private List<File> getDeviceDirs() {
        final File[] slaveDevices = masterDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return !name.contains("w1_bus_master");
            }
        });
        return Arrays.asList(slaveDevices);
    }

    /**
     * Gets a list of all registered slave device ids.
     * @return list of slave ids, can be empty, never null.
     */
    public List<String> getDeviceIDs() {
        List<String> ids = new ArrayList<>();
        for (File deviceDir: getDeviceDirs()) {
            ids.add(deviceDir.getName());
        }
        return ids;
    }

    public List<W1Device> getDevices() {
        return getDevices((String)null);
    }

    public List<W1Device> getDevices(int deviceFamilyId) {
        return getDevices(Integer.toHexString(deviceFamilyId));
    }

    public <T extends W1Device> List<T> getDevices(String deviceFamilyId) {
        List<W1Device> devices = new ArrayList<>();
        for (File deviceDir: getDeviceDirs()) {
            String id = deviceDir.getName().substring(0, 2).toUpperCase();
            if (deviceFamilyId == null || deviceFamilyId.toUpperCase().equals(id)) {
                final W1DeviceType w1DeviceType = deviceTypeMap.get(id);
                if (w1DeviceType != null) {
                    final W1Device w1Device = w1DeviceType.create(deviceDir);
                    devices.add(w1Device);
                } else {
                    log.info("no device type for [" + id + "] found - ignoring");
                }
            }
        }
        return (List<T>) devices;
    }

    public <T extends W1Device> List<T> getDevices(Class<T> type) {
        for (W1DeviceType deviceType : deviceTypes) {
            if (deviceType.getDeviceClass().equals(type)) {
                return (List<T>) getDevices(deviceType.getDeviceFamilyCode());
            }
        }
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("W1 Master: ").append(masterDir).append("\n");
        builder.append("Device Types: \n");
        for (W1DeviceType deviceType : deviceTypeMap.values()) {
            builder.append(" - ").append(Integer.toHexString(deviceType.getDeviceFamilyCode()));
            builder.append(" = ").append(deviceType.getDeviceClass());
            builder.append("\n");
        }
        builder.append("Devices:\n");
        for (W1Device device : getDevices()) {
            builder.append(" - ").append(device.getId()).append(": ").append(device.getName());
            builder.append(" = ").append(device.getClass().getName()).append("\n");
        }
        return builder.toString();
    }
}
