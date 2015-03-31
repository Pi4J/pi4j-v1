package com.pi4j.system.service.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: OSGi Service
 * FILENAME      :  SystemInformationServiceImpl.java  
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


import java.io.IOException;
import java.text.ParseException;

import com.pi4j.system.SystemInfo;
import com.pi4j.system.service.SystemInformationService;

@SuppressWarnings("PackageAccessibility")
public class SystemInformationServiceImpl implements SystemInformationService
{
    public SystemInformationServiceImpl()
    {
    }
    
    @Override
    public String getProcessor() throws IOException, InterruptedException
    {
        return SystemInfo.getProcessor();
    }

    @Deprecated
    @Override
    public String getBogoMIPS() throws IOException, InterruptedException
    {
        return SystemInfo.getBogoMIPS();
    }

    @Override
    public String[] getCpuFeatures() throws IOException, InterruptedException
    {
        return SystemInfo.getCpuFeatures();
    }

    @Override
    public String getCpuImplementer() throws IOException, InterruptedException
    {
        return SystemInfo.getCpuImplementer();
    }

    @Override
    public String getCpuArchitecture() throws IOException, InterruptedException
    {
        return SystemInfo.getCpuArchitecture();
    }

    @Override
    public String getCpuVariant() throws IOException, InterruptedException
    {
        return SystemInfo.getCpuVariant();
    }

    @Override
    public String getCpuPart() throws IOException, InterruptedException
    {
        return SystemInfo.getCpuPart();
    }

    @Override
    public String getCpuRevision() throws IOException, InterruptedException
    {
        return SystemInfo.getCpuRevision();
    }

    @Override
    public String getHardware() throws IOException, InterruptedException
    {
        return SystemInfo.getHardware();
    }

    @Override
    public String getRevision() throws IOException, InterruptedException
    {
        return SystemInfo.getRevision();
    }

    @Override
    public String getSerial() throws IOException, InterruptedException
    {
        return SystemInfo.getSerial();
    }

    @Override
    public String getModelName() throws IOException, InterruptedException {
        return SystemInfo.getModelName();
    }

    @Override
    public String getOsName() throws IOException, InterruptedException {
        return SystemInfo.getOsName();
    }

    @Override
    public String getOsVersion() throws IOException, InterruptedException {
        return SystemInfo.getOsVersion();
    }

    @Override
    public String getOsArch() throws IOException, InterruptedException {
        return SystemInfo.getOsArch();
    }

    @Override
    public String getOsFirmwareBuild() throws IOException, InterruptedException {
        return SystemInfo.getOsFirmwareBuild();
    }

    @Override
    public String getOsFirmwareDate() throws IOException, InterruptedException, ParseException {
        return SystemInfo.getOsFirmwareDate();
    }

    @Override
    public String getJavaVendor() {
        return SystemInfo.getJavaVendor();
    }

    @Override
    public String getJavaVendorUrl() {
        return SystemInfo.getJavaVendorUrl();
    }

    @Override
    public String getJavaVersion() {
        return SystemInfo.getJavaVersion();
    }

    @Override
    public String getJavaVirtualMachine() {
        return SystemInfo.getJavaVirtualMachine();
    }

    @Override
    public String getJavaRuntime() {
        return SystemInfo.getJavaRuntime();
    }

    @Override
    public boolean isHardFloatAbi() {
        return SystemInfo.isHardFloatAbi();
    }

    @Override
    public long getMemoryTotal() throws IOException, InterruptedException {
        return SystemInfo.getMemoryTotal();
    }

    @Override
    public long getMemoryUsed() throws IOException, InterruptedException {
        return SystemInfo.getMemoryUsed();
    }

    @Override
    public long getMemoryFree() throws IOException, InterruptedException {
        return SystemInfo.getMemoryFree();
    }

    @Override
    public long getMemoryShared() throws IOException, InterruptedException {
        return SystemInfo.getMemoryShared();
    }

    @Override
    public long getMemoryBuffers() throws IOException, InterruptedException {
        return SystemInfo.getMemoryBuffers();
    }

    @Override
    public long getMemoryCached() throws IOException, InterruptedException {
        return SystemInfo.getMemoryCached();
    }

    @Override
    public SystemInfo.BoardType getBoardType() throws IOException, InterruptedException {
        return SystemInfo.getBoardType();
    }

    @Override
    public float getCpuTemperature() throws IOException, InterruptedException, NumberFormatException {
        return SystemInfo.getCpuTemperature();
    }

    @Override
    public float getCpuVoltage() throws IOException, InterruptedException, NumberFormatException {
        return SystemInfo.getCpuVoltage();
    }

    @Override
    public float getMemoryVoltageSDRam_C() throws IOException, InterruptedException, NumberFormatException {
        return SystemInfo.getMemoryVoltageSDRam_C();
    }

    @Override
    public float getMemoryVoltageSDRam_I() throws IOException, InterruptedException, NumberFormatException {
        return SystemInfo.getMemoryVoltageSDRam_I();
    }

    @Override
    public float getMemoryVoltageSDRam_P() throws IOException, InterruptedException, NumberFormatException {
        return SystemInfo.getMemoryVoltageSDRam_P();
    }

    @Override
    public boolean getCodecH264Enabled() throws IOException, InterruptedException {
        return SystemInfo.getCodecH264Enabled();
    }

    @Override
    public boolean getCodecMPG2Enabled() throws IOException, InterruptedException {
        return SystemInfo.getCodecMPG2Enabled();
    }

    @Override
    public boolean getCodecWVC1Enabled() throws IOException, InterruptedException {
        return SystemInfo.getCodecWVC1Enabled();
    }

    @Override
    public long getClockFrequencyArm() throws IOException, InterruptedException {
        return SystemInfo.getClockFrequencyArm();
    }

    @Override
    public long getClockFrequencyCore() throws IOException, InterruptedException {
        return SystemInfo.getClockFrequencyCore();
    }

    @Override
    public long getClockFrequencyH264() throws IOException, InterruptedException {
        return SystemInfo.getClockFrequencyH264();
    }

    @Override
    public long getClockFrequencyISP() throws IOException, InterruptedException {
        return SystemInfo.getClockFrequencyISP();
    }

    @Override
    public long getClockFrequencyV3D() throws IOException, InterruptedException {
        return SystemInfo.getClockFrequencyV3D();
    }

    @Override
    public long getClockFrequencyUART() throws IOException, InterruptedException {
        return SystemInfo.getClockFrequencyUART();
    }

    @Override
    public long getClockFrequencyPWM() throws IOException, InterruptedException {
        return SystemInfo.getClockFrequencyPWM();
    }

    @Override
    public long getClockFrequencyEMMC() throws IOException, InterruptedException {
        return SystemInfo.getClockFrequencyEMMC();
    }

    @Override
    public long getClockFrequencyPixel() throws IOException, InterruptedException {
        return SystemInfo.getClockFrequencyPixel();
    }

    @Override
    public long getClockFrequencyVEC() throws IOException, InterruptedException {
        return SystemInfo.getClockFrequencyVEC();
    }

    @Override
    public long getClockFrequencyHDMI() throws IOException, InterruptedException {
        return SystemInfo.getClockFrequencyHDMI();
    }

    @Override
    public long getClockFrequencyDPI() throws IOException, InterruptedException {
        return SystemInfo.getClockFrequencyDPI();
    }
}
