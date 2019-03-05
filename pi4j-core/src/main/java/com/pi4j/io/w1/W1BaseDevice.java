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
