package com.pi4j.system;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SystemInfoProvider.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
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

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * SystemInfo provider interface.  Used to support multiple platforms where each may need to
 * have a platform specific implementation of the system info.
 */
public interface SystemInfoProvider {
    String getProcessor()  throws IOException, InterruptedException, UnsupportedOperationException;
    String getModelName() throws IOException, InterruptedException, UnsupportedOperationException;
    String getBogoMIPS() throws IOException, InterruptedException, UnsupportedOperationException;
    String[] getCpuFeatures() throws IOException, InterruptedException, UnsupportedOperationException;
    String getCpuImplementer() throws IOException, InterruptedException, UnsupportedOperationException;
    String getCpuArchitecture() throws IOException, InterruptedException, UnsupportedOperationException;
    String getCpuVariant() throws IOException, InterruptedException, UnsupportedOperationException;
    String getCpuPart() throws IOException, InterruptedException, UnsupportedOperationException;
    String getCpuRevision() throws IOException, InterruptedException, UnsupportedOperationException;
    String getHardware() throws IOException, InterruptedException, UnsupportedOperationException;
    String getRevision() throws IOException, InterruptedException, UnsupportedOperationException;
    String getSerial() throws IOException, InterruptedException, UnsupportedOperationException;
    String getOsName() throws UnsupportedOperationException;
    String getOsVersion() throws UnsupportedOperationException;
    String getOsArch() throws UnsupportedOperationException;
    String getOsFirmwareBuild() throws IOException, InterruptedException, UnsupportedOperationException;
    String getOsFirmwareDate() throws IOException, InterruptedException, ParseException, UnsupportedOperationException;
    String getJavaVendor() throws UnsupportedOperationException;
    String getJavaVendorUrl() throws UnsupportedOperationException;
    String getJavaVersion() throws UnsupportedOperationException;
    String getJavaVirtualMachine()throws UnsupportedOperationException;
    String getJavaRuntime() throws UnsupportedOperationException;
    boolean isHardFloatAbi() throws UnsupportedOperationException;
    long getMemoryTotal() throws IOException, InterruptedException, UnsupportedOperationException;
    long getMemoryUsed() throws IOException, InterruptedException, UnsupportedOperationException;
    long getMemoryFree() throws IOException, InterruptedException, UnsupportedOperationException;
    long getMemoryShared() throws IOException, InterruptedException, UnsupportedOperationException;
    long getMemoryBuffers() throws IOException, InterruptedException, UnsupportedOperationException;
    long getMemoryCached() throws IOException, InterruptedException, UnsupportedOperationException;
    SystemInfo.BoardType getBoardType() throws IOException, InterruptedException, UnsupportedOperationException;
    float getCpuTemperature() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException;
    float getCpuVoltage() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException;
    float getMemoryVoltageSDRam_C() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException;
    float getMemoryVoltageSDRam_I() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException;
    float getMemoryVoltageSDRam_P() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException;
    boolean getCodecH264Enabled() throws IOException, InterruptedException, UnsupportedOperationException;
    boolean getCodecMPG2Enabled() throws IOException, InterruptedException, UnsupportedOperationException;
    boolean getCodecWVC1Enabled() throws IOException, InterruptedException, UnsupportedOperationException;
    long getClockFrequencyArm() throws IOException, InterruptedException, UnsupportedOperationException;
    long getClockFrequencyCore() throws IOException, InterruptedException, UnsupportedOperationException;
    long getClockFrequencyH264() throws IOException, InterruptedException, UnsupportedOperationException;
    long getClockFrequencyISP() throws IOException, InterruptedException, UnsupportedOperationException;
    long getClockFrequencyV3D() throws IOException, InterruptedException, UnsupportedOperationException;
    long getClockFrequencyUART() throws IOException, InterruptedException, UnsupportedOperationException;
    long getClockFrequencyPWM() throws IOException, InterruptedException, UnsupportedOperationException;
    long getClockFrequencyEMMC() throws IOException, InterruptedException, UnsupportedOperationException;
    long getClockFrequencyPixel() throws IOException, InterruptedException, UnsupportedOperationException;
    long getClockFrequencyVEC() throws IOException, InterruptedException, UnsupportedOperationException;
    long getClockFrequencyHDMI() throws IOException, InterruptedException, UnsupportedOperationException;
    long getClockFrequencyDPI() throws IOException, InterruptedException, UnsupportedOperationException;
}
