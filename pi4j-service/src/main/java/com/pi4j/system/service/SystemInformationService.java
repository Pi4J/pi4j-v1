package com.pi4j.system.service;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: OSGi Service
 * FILENAME      :  SystemInformationService.java  
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

import com.pi4j.system.SystemInfo;
import java.io.IOException;
import java.text.ParseException;

public interface SystemInformationService 
{
    String getProcessor() throws IOException, InterruptedException;
    @Deprecated String getBogoMIPS() throws IOException, InterruptedException;
    String[] getCpuFeatures() throws IOException, InterruptedException;
    String getCpuImplementer() throws IOException, InterruptedException;
    String getCpuArchitecture() throws IOException, InterruptedException;
    String getCpuVariant() throws IOException, InterruptedException;
    String getCpuPart() throws IOException, InterruptedException;
    String getCpuRevision() throws IOException, InterruptedException;
    String getHardware() throws IOException, InterruptedException;
    String getRevision() throws IOException, InterruptedException;
    String getSerial() throws IOException, InterruptedException;
    String getModelName() throws IOException, InterruptedException;
    String getOsName() throws IOException, InterruptedException;
    String getOsVersion() throws IOException, InterruptedException;
    String getOsArch() throws IOException, InterruptedException;
    String getOsFirmwareBuild() throws IOException, InterruptedException;
    String getOsFirmwareDate() throws IOException, InterruptedException, ParseException;
    String getJavaVendor();
    String getJavaVendorUrl();
    String getJavaVersion();
    String getJavaVirtualMachine();
    String getJavaRuntime();
    boolean isHardFloatAbi();
    long getMemoryTotal() throws IOException, InterruptedException;
    long getMemoryUsed() throws IOException, InterruptedException;
    long getMemoryFree() throws IOException, InterruptedException;
    long getMemoryShared() throws IOException, InterruptedException;
    long getMemoryBuffers() throws IOException, InterruptedException;
    long getMemoryCached() throws IOException, InterruptedException;
    SystemInfo.BoardType getBoardType() throws IOException, InterruptedException;
    float getCpuTemperature() throws IOException, InterruptedException, NumberFormatException;
    float getCpuVoltage() throws IOException, InterruptedException, NumberFormatException;
    float getMemoryVoltageSDRam_C() throws IOException, InterruptedException, NumberFormatException;
    float getMemoryVoltageSDRam_I() throws IOException, InterruptedException, NumberFormatException;
    float getMemoryVoltageSDRam_P() throws IOException, InterruptedException, NumberFormatException;
    boolean getCodecH264Enabled() throws IOException, InterruptedException;
    boolean getCodecMPG2Enabled() throws IOException, InterruptedException;
    boolean getCodecWVC1Enabled() throws IOException, InterruptedException;
    long getClockFrequencyArm() throws IOException, InterruptedException;
    long getClockFrequencyCore() throws IOException, InterruptedException;
    long getClockFrequencyH264() throws IOException, InterruptedException;
    long getClockFrequencyISP() throws IOException, InterruptedException;
    long getClockFrequencyV3D() throws IOException, InterruptedException;
    long getClockFrequencyUART() throws IOException, InterruptedException;
    long getClockFrequencyPWM() throws IOException, InterruptedException;
    long getClockFrequencyEMMC() throws IOException, InterruptedException;
    long getClockFrequencyPixel() throws IOException, InterruptedException;
    long getClockFrequencyVEC() throws IOException, InterruptedException;
    long getClockFrequencyHDMI() throws IOException, InterruptedException;
    long getClockFrequencyDPI() throws IOException, InterruptedException;
}
