package com.pi4j.io.w1;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  W1DummyDeviceType.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2021 Pi4J
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

/**
 * @author Peter Schuebl
 */
public class W1DummyDeviceType implements W1DeviceType {

    public static final int FAMILY_ID = 0xFE;

    @Override
    public int getDeviceFamilyCode() {
        return FAMILY_ID;
    }

    @Override
    public Class<? extends W1Device> getDeviceClass() {
        return W1DummyDevice.class;
    }

    @Override
    public W1DummyDevice create(final File deviceDir) {
        return new W1DummyDevice(deviceDir);
    }

    static class W1DummyDevice extends W1BaseDevice {

        @Override
        public int getFamilyId() {
            return FAMILY_ID;
        }

        public W1DummyDevice(final File deviceDir) {
            super(deviceDir);
        }

    }

}
