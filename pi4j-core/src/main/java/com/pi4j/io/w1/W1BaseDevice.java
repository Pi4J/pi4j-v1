package com.pi4j.io.w1;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  W1BaseDevice.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author Peter Schuebl
 */
public abstract class W1BaseDevice implements W1Device {

    private final String id;

    private String name;

    private File deviceDir;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    public W1BaseDevice(final File deviceDir) {
        String deviceName;
        try {
            deviceName = new String(Files.readAllBytes(new File(deviceDir, "name").toPath()));
        } catch (IOException e) {
            // FIXME logging
            deviceName = deviceDir.getName();
        }
        name = deviceName;
        id = deviceName;
        this.deviceDir = deviceDir;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof W1Device && id.equals(((W1Device) obj).getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String getValue() throws IOException {
        return new String(Files.readAllBytes(new File(deviceDir, "w1_slave").toPath()));
    }
}
