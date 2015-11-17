package com.pi4j.system;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SystemInfo.java  
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


import com.pi4j.io.i2c.I2CFactoryProviderBanana;
import com.pi4j.io.i2c.I2CFactoryProviderRaspberry;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformManager;
import com.pi4j.util.ExecUtil;
import com.pi4j.util.StringUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemInfo {

    // private constructor
    private SystemInfo() {
        // forbid object construction
    }

    public enum BoardType {
        UNKNOWN,
        ModelA_Rev1,
        ModelA_Plus_Rev1,
        ModelB_Rev1,
        ModelB_Rev2,
        ModelB_Plus_Rev1,
        Compute_Module_Rev1,
        Model2B_Rev1,
        BananaPi,
        BananaPro,
        Odroid
    }

    public static String getProcessor()  throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getProcessor();
    }

    public static String getModelName() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getModelName();
    }

    public static String getBogoMIPS() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getBogoMIPS();
    }

    public static String[] getCpuFeatures() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getCpuFeatures();
    }

    public static String getCpuImplementer() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getCpuImplementer();
    }

    public static String getCpuArchitecture() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getCpuArchitecture();
    }

    public static String getCpuVariant() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getCpuVariant();
    }

    public static String getCpuPart() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getCpuPart();
    }

    public static String getCpuRevision() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getCpuRevision();
    }

    public static String getHardware() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getHardware();
    }

    public static String getRevision() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getRevision();
    }

    public static String getSerial() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getSerial();
    }

    public static String getOsName() throws UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getOsName();
    }

    public static String getOsVersion() throws UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getOsVersion();
    }

    public static String getOsArch() throws UnsupportedOperationException  {
        return SystemInfoFactory.getProvider().getOsArch();
    }

    public static String getOsFirmwareBuild() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getOsFirmwareBuild();
    }

    public static String getOsFirmwareDate() throws IOException, InterruptedException, ParseException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getOsFirmwareDate();
    }

    public static String getJavaVendor() throws UnsupportedOperationException  {
        return SystemInfoFactory.getProvider().getJavaVendor();
    }

    public static String getJavaVendorUrl() throws UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getJavaVendorUrl();
    }

    public static String getJavaVersion() throws UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getJavaVersion();
    }

    public static String getJavaVirtualMachine() throws UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getJavaVirtualMachine();
    }

    public static String getJavaRuntime() throws UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getJavaRuntime();
    }

    public static boolean isHardFloatAbi() throws UnsupportedOperationException {
        return SystemInfoFactory.getProvider().isHardFloatAbi();
    }

    public static long getMemoryTotal() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getMemoryTotal();
    }

    public static long getMemoryUsed() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getMemoryUsed();
    }

    public static long getMemoryFree() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getMemoryFree();
    }

    public static long getMemoryShared() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getMemoryShared();
    }

    public static long getMemoryBuffers() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getMemoryBuffers();
    }

    public static long getMemoryCached() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getMemoryCached();
    }

    public static BoardType getBoardType() throws IOException, InterruptedException, UnsupportedOperationException
    {
        return SystemInfoFactory.getProvider().getBoardType();
    }

    public static float getCpuTemperature() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getCpuTemperature();
    }

    public static float getCpuVoltage() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getCpuVoltage();
    }

    public static float getMemoryVoltageSDRam_C() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getMemoryVoltageSDRam_C();
    }

    public static float getMemoryVoltageSDRam_I() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getMemoryVoltageSDRam_I();
    }

    public static float getMemoryVoltageSDRam_P() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getMemoryVoltageSDRam_P();
    }

    public static boolean getCodecH264Enabled() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getCodecH264Enabled();
    }

    public static boolean getCodecMPG2Enabled() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getCodecMPG2Enabled();
    }

    public static boolean getCodecWVC1Enabled() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getCodecWVC1Enabled();
    }

    public static long getClockFrequencyArm() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getClockFrequencyArm();
    }

    public static long getClockFrequencyCore() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getClockFrequencyCore();
    }

    public static long getClockFrequencyH264() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getClockFrequencyH264();
    }

    public static long getClockFrequencyISP() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getClockFrequencyISP();
    }

    public static long getClockFrequencyV3D() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getClockFrequencyV3D();
    }

    public static long getClockFrequencyUART() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getClockFrequencyUART();
    }

    public static long getClockFrequencyPWM() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getClockFrequencyPWM();
    }

    public static long getClockFrequencyEMMC() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getClockFrequencyEMMC();
    }

    public static long getClockFrequencyPixel() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getClockFrequencyPixel();
    }

    public static long getClockFrequencyVEC() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getClockFrequencyVEC();
    }

    public static long getClockFrequencyHDMI() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getClockFrequencyHDMI();
    }

    public static long getClockFrequencyDPI() throws IOException, InterruptedException, UnsupportedOperationException {
        return SystemInfoFactory.getProvider().getClockFrequencyDPI();
    }
}
