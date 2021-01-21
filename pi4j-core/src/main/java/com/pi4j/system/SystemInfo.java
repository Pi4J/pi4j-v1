package com.pi4j.system;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  SystemInfo.java
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


import java.io.IOException;
import java.text.ParseException;

public class SystemInfo {

    // private constructor
    private SystemInfo() {
        // forbid object construction
    }

    public enum BoardType {
        UNKNOWN,
        //------------------------
        RaspberryPi_A,
        RaspberryPi_B_Rev1,
        RaspberryPi_B_Rev2,
        RaspberryPi_A_Plus,
        RaspberryPi_B_Plus,
        RaspberryPi_ComputeModule,
        RaspberryPi_2B,
        RaspberryPi_3B,
        RaspberryPi_3B_Plus,
        RaspberryPi_Zero,
        RaspberryPi_ComputeModule3,
        RaspberryPi_ZeroW,
        RaspberryPi_Alpha,
        RaspberryPi_Unknown,
        //------------------------
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
