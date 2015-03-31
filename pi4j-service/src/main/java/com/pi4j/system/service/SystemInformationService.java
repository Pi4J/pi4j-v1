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
 * Copyright (C) 2012 - 2015 Pi4J
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
