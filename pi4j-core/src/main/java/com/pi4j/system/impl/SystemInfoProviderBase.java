package com.pi4j.system.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SystemInfoProviderBase.java
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

import com.pi4j.system.SystemInfo;
import com.pi4j.system.SystemInfoProvider;

import java.io.IOException;
import java.text.ParseException;

/**
 * Abstract base implementation of the SystemInfoProvider interface.
 * This base impl is a NO-OP impl, simply throwing a 'UnsupportedOperationException'
 * for each method.
 */
public abstract class SystemInfoProviderBase implements SystemInfoProvider {

    @Override
    public String getProcessor() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getModelName() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getBogoMIPS() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getCpuFeatures() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCpuImplementer() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCpuArchitecture() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCpuVariant() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCpuPart() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCpuRevision() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getHardware() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRevision() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSerial() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getOsName() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getOsVersion() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getOsArch() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getOsFirmwareBuild() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getOsFirmwareDate() throws IOException, InterruptedException, ParseException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getJavaVendor() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getJavaVendorUrl() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getJavaVersion() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getJavaVirtualMachine() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getJavaRuntime() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isHardFloatAbi() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getMemoryTotal() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getMemoryUsed() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getMemoryFree() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getMemoryShared() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getMemoryBuffers() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getMemoryCached() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public SystemInfo.BoardType getBoardType() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getCpuTemperature() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getCpuVoltage() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getMemoryVoltageSDRam_C() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getMemoryVoltageSDRam_I() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getMemoryVoltageSDRam_P() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getCodecH264Enabled() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getCodecMPG2Enabled() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getCodecWVC1Enabled() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getClockFrequencyArm() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getClockFrequencyCore() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getClockFrequencyH264() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getClockFrequencyISP() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getClockFrequencyV3D() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getClockFrequencyUART() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getClockFrequencyPWM() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getClockFrequencyEMMC() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getClockFrequencyPixel() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getClockFrequencyVEC() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getClockFrequencyHDMI() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getClockFrequencyDPI() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
