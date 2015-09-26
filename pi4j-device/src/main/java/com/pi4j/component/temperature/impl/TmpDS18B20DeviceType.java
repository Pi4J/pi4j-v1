package com.pi4j.component.temperature.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  TmpDS18B20DeviceType.java  
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


import com.pi4j.io.w1.W1Device;
import com.pi4j.io.w1.W1DeviceType;

import java.io.File;

/**
 * @author Peter Schuebl
 */
public class TmpDS18B20DeviceType implements W1DeviceType {

    public static final int FAMILY_CODE = 0x28;

    @Override
    public int getDeviceFamilyCode() {
        return FAMILY_CODE;
    }

    @Override
    public Class<? extends W1Device> getDeviceClass() {
        return TmpDS18B20.class;
    }

    @Override
    public TmpDS18B20 create(final File deviceDir) {
        return new TmpDS18B20(deviceDir);
    }

    /*
    use w1Master.getDevices(TmpDS18B20.class) instead

    public static List<TmpDS18B20> getDevices(final W1Master master) {
        final List<TmpDS18B20> devices = new ArrayList<>();
        for (W1Device device : master.getDevices(FAMILY_CODE)) {
            devices.add((TmpDS18B20) device);
        }
        return devices;
    }
    */
}
