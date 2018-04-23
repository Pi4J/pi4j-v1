package com.pi4j.io.w1;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.CopyOnWriteArrayList;
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
 */

/**
 * @author Peter Schuebl
 */
public class W1Master {

    private final Logger log = Logger.getLogger(W1Master.class.getName());

    private final List<W1DeviceType> deviceTypes = new ArrayList<>();

    private final Map<String, W1DeviceType> deviceTypeMap = new LinkedHashMap<>();

    private File masterDir = new File("/sys/bus/w1/devices");

    private final List<W1Device> devices = new CopyOnWriteArrayList<>();

    /**
     * Create an instance of the W1 master. Typically there should only be one master.
     * <p/>
     * java.util.ServiceLoader is used to add device support for individual devices.
     */
    public W1Master() {
        init(null);
    }

    /**
     * Create an instance of the W1 master. Typically there should only be one master.
     * <p/>
     * java.util.ServiceLoader is used to add device support for individual devices.
     *
     * @param classloader This ClassLoader will be used for the ServiceLoader to determine supported device types.
     */
    public W1Master(final ClassLoader classloader) {
        init(classloader);
    }

    /**
     * Create a master with a different default dir e.g. for tests.
     *
     * @param masterDir
     */
    public W1Master(final String masterDir) {
        this.masterDir = new File(masterDir);
        init(null);
    }

    /**
     * Create a master with a different default dir e.g. for tests.
     *
     * @param masterDir
     * @param classloader This ClassLoader will be used for the ServiceLoader to determine supported device types.
     */
    public W1Master(final String masterDir, final ClassLoader classloader) {
        this.masterDir = new File(masterDir);
        init(classloader);
    }

    private void init(final ClassLoader classloader) {
        final ServiceLoader<W1DeviceType> w1DeviceTypes = classloader == null ? ServiceLoader.load(W1DeviceType.class) : ServiceLoader.load(W1DeviceType.class, classloader);
        final Iterator<W1DeviceType> w1DeviceTypeIterator = w1DeviceTypes.iterator();
        while (w1DeviceTypeIterator.hasNext()) {
            final W1DeviceType w1DeviceType = w1DeviceTypeIterator.next();
            deviceTypes.add(w1DeviceType);
            final String deviceFamily = Integer.toHexString(w1DeviceType.getDeviceFamilyCode()).toUpperCase();
            deviceTypeMap.put(deviceFamily, w1DeviceType);
        }
        devices.addAll(readDevices());
    }

    public void checkDeviceChanges() {
        final List<W1Device> refreshedDevices = new ArrayList<>();
        final List<W1Device> removedDevices = new ArrayList<>();

        refreshedDevices.addAll(readDevices());

        for (final W1Device device : devices) {
            if (!refreshedDevices.contains(device)) {
                removedDevices.add(device);
            }
        }
        refreshedDevices.removeAll(devices);

        final int newCount = refreshedDevices.size();
        final int removedCount = removedDevices.size();
        if (newCount > 0) {
            log.fine("found " + newCount + " new device(s): " + refreshedDevices);
        }

        if (removedCount > 0) {
            log.fine("removed " + removedCount + " device(s): " + removedDevices);
        }

        devices.addAll(refreshedDevices);
        devices.removeAll(removedDevices);
    }

    /**
     * Gets a list of the available device types.
     *
     * @return
     */
    public Collection<W1DeviceType> getDeviceTypes() {
        return deviceTypes;
    }

    public Map<String, W1DeviceType> getDeviceTypeMap() {
        return deviceTypeMap;
    }

    private List<File> getDeviceDirs() {
        final File[] slaveDevices = masterDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(final File dir, final String name) {
                return !name.contains("w1_bus_master");
            }
        });
        if (slaveDevices != null) {
            return Arrays.asList(slaveDevices);
        }
        return Collections.emptyList();
    }

    /**
     * Gets a list of all registered slave device ids.
     *
     * @return list of slave ids, can be empty, never null.
     */
    public List<String> getDeviceIDs() {
        final List<String> ids = new ArrayList<>();
        for (final File deviceDir : getDeviceDirs()) {
            ids.add(deviceDir.getName());
        }
        /*
         * //for (final W1Device device: devices) { ids.add(device.getId());
         */
        return ids;
    }

    /**
     * Get the list of available devices.
     *
     * @return returns an unmodifiable list of W1Devices.
     */
    public List<W1Device> getDevices() {
        return Collections.unmodifiableList(devices);
    }

    @SuppressWarnings("unchecked")
    public <T extends W1Device> List<T> getDevices(final int deviceFamilyId) {
        final List<W1Device> filteredDevices = new ArrayList<>();
        for (final W1Device device : devices) {
            if (deviceFamilyId == device.getFamilyId()) {
                filteredDevices.add(device);
            }
        }
        return (List<T>) filteredDevices;
    }

    @SuppressWarnings("unchecked")
    <T extends W1Device> List<T> readDevices() {
        final List<W1Device> devices = new ArrayList<>();
        for (final File deviceDir : getDeviceDirs()) {
            final String id = deviceDir.getName().substring(0, 2).toUpperCase();
            final W1DeviceType w1DeviceType = deviceTypeMap.get(id);
            if (w1DeviceType != null) {
                final W1Device w1Device = w1DeviceType.create(deviceDir);
                devices.add(w1Device);
            } else {
                log.info("no device type for [" + id + "] found - ignoring");
            }
        }
        return (List<T>) devices;
    }

    /*
    public <T extends W1Device> List<T> getDevices(final String deviceFamilyId) {
        List<W1Device> devices = new ArrayList<>();
        for (W1Device device : readDevices()) {

            if (deviceFamilyId == null || deviceFamilyId.toUpperCase().equals(device.getId())) {
                devices.add(device);
            }
        }
        return (List<T>) devices;
    }
     */

    /**
     * Get a list of devices that implement a certain interface.
     *
     * @param type
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getDevices(final Class<T> type) {
        final List<W1Device> allDevices = getDevices();
        final List<T> filteredDevices = new ArrayList<>();
        for (final W1Device device : allDevices) {
            if (type.isAssignableFrom(device.getClass())) {
                filteredDevices.add((T) device);
            }
        }
        return filteredDevices;
    }

    @SuppressWarnings("unchecked")
    public <T extends W1Device> List<T> getW1Devices(final Class<T> type) {
        for (final W1DeviceType deviceType : deviceTypes) {
            if (deviceType.getDeviceClass().equals(type)) {
                return (List<T>) getDevices(deviceType.getDeviceFamilyCode());
            }
        }
        return Collections.emptyList();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("W1 Master: ").append(masterDir).append("\n");
        builder.append("Device Types: \n");
        for (final W1DeviceType deviceType : deviceTypeMap.values()) {
            builder.append(" - ").append(Integer.toHexString(deviceType.getDeviceFamilyCode()));
            builder.append(" = ").append(deviceType.getDeviceClass());
            builder.append("\n");
        }
        builder.append("Devices:\n");
        for (final W1Device device : getDevices()) {
            builder.append(" - ").append(device.getId()).append(": ").append(device.getName());
            builder.append(" = ").append(device.getClass().getName()).append("\n");
        }
        return builder.toString();
    }
}
