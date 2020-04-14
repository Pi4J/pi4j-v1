// START SNIPPET: system-info-snippet


/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  SystemInfoExample.java
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

import com.pi4j.platform.PlatformManager;
import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;

import java.io.IOException;
import java.text.ParseException;

/**
 * This example code demonstrates how to access a few of the system information properties and
 * network information from the Raspberry Pi.
 *
 * @author Robert Savage
 */
public class SystemInfoExample {

    public static void main(String[] args) throws InterruptedException, IOException, ParseException {

        // display a few of the available system information properties
        System.out.println("----------------------------------------------------");
        System.out.println("PLATFORM INFO");
        System.out.println("----------------------------------------------------");
        try{System.out.println("Platform Name     :  " + PlatformManager.getPlatform().getLabel());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("Platform ID       :  " + PlatformManager.getPlatform().getId());}
        catch(UnsupportedOperationException ex){}
        System.out.println("----------------------------------------------------");
        System.out.println("HARDWARE INFO");
        System.out.println("----------------------------------------------------");
        try{System.out.println("Serial Number     :  " + SystemInfo.getSerial());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("CPU Revision      :  " + SystemInfo.getCpuRevision());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("CPU Architecture  :  " + SystemInfo.getCpuArchitecture());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("CPU Part          :  " + SystemInfo.getCpuPart());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("CPU Temperature   :  " + SystemInfo.getCpuTemperature());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("CPU Core Voltage  :  " + SystemInfo.getCpuVoltage());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("CPU Model Name    :  " + SystemInfo.getModelName());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("Processor         :  " + SystemInfo.getProcessor());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("Hardware          :  " + SystemInfo.getHardware());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("Hardware Revision :  " + SystemInfo.getRevision());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("Is Hard Float ABI :  " + SystemInfo.isHardFloatAbi());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("Board Type        :  " + SystemInfo.getBoardType().name());}
        catch(UnsupportedOperationException ex){}

        System.out.println("----------------------------------------------------");
        System.out.println("MEMORY INFO");
        System.out.println("----------------------------------------------------");
        try{System.out.println("Total Memory      :  " + SystemInfo.getMemoryTotal());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("Used Memory       :  " + SystemInfo.getMemoryUsed());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("Free Memory       :  " + SystemInfo.getMemoryFree());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("Shared Memory     :  " + SystemInfo.getMemoryShared());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("Memory Buffers    :  " + SystemInfo.getMemoryBuffers());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("Cached Memory     :  " + SystemInfo.getMemoryCached());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("SDRAM_C Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_C());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("SDRAM_I Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_I());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("SDRAM_P Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_P());}
        catch(UnsupportedOperationException ex){}

        System.out.println("----------------------------------------------------");
        System.out.println("OPERATING SYSTEM INFO");
        System.out.println("----------------------------------------------------");
        try{System.out.println("OS Name           :  " + SystemInfo.getOsName());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("OS Version        :  " + SystemInfo.getOsVersion());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("OS Architecture   :  " + SystemInfo.getOsArch());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("OS Firmware Build :  " + SystemInfo.getOsFirmwareBuild());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("OS Firmware Date  :  " + SystemInfo.getOsFirmwareDate());}
        catch(UnsupportedOperationException ex){}

        System.out.println("----------------------------------------------------");
        System.out.println("JAVA ENVIRONMENT INFO");
        System.out.println("----------------------------------------------------");
        System.out.println("Java Vendor       :  " + SystemInfo.getJavaVendor());
        System.out.println("Java Vendor URL   :  " + SystemInfo.getJavaVendorUrl());
        System.out.println("Java Version      :  " + SystemInfo.getJavaVersion());
        System.out.println("Java VM           :  " + SystemInfo.getJavaVirtualMachine());
        System.out.println("Java Runtime      :  " + SystemInfo.getJavaRuntime());

        System.out.println("----------------------------------------------------");
        System.out.println("NETWORK INFO");
        System.out.println("----------------------------------------------------");

        // display some of the network information
        System.out.println("Hostname          :  " + NetworkInfo.getHostname());
        for (String ipAddress : NetworkInfo.getIPAddresses())
            System.out.println("IP Addresses      :  " + ipAddress);
        for (String fqdn : NetworkInfo.getFQDNs())
            System.out.println("FQDN              :  " + fqdn);
        for (String nameserver : NetworkInfo.getNameservers())
            System.out.println("Nameserver        :  " + nameserver);

        System.out.println("----------------------------------------------------");
        System.out.println("CODEC INFO");
        System.out.println("----------------------------------------------------");
        try{System.out.println("H264 Codec Enabled:  " + SystemInfo.getCodecH264Enabled());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("MPG2 Codec Enabled:  " + SystemInfo.getCodecMPG2Enabled());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("WVC1 Codec Enabled:  " + SystemInfo.getCodecWVC1Enabled());}
        catch(UnsupportedOperationException ex){}

        System.out.println("----------------------------------------------------");
        System.out.println("CLOCK INFO");
        System.out.println("----------------------------------------------------");
        try{System.out.println("ARM Frequency     :  " + SystemInfo.getClockFrequencyArm());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("CORE Frequency    :  " + SystemInfo.getClockFrequencyCore());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("H264 Frequency    :  " + SystemInfo.getClockFrequencyH264());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("ISP Frequency     :  " + SystemInfo.getClockFrequencyISP());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("V3D Frequency     :  " + SystemInfo.getClockFrequencyV3D());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("UART Frequency    :  " + SystemInfo.getClockFrequencyUART());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("PWM Frequency     :  " + SystemInfo.getClockFrequencyPWM());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("EMMC Frequency    :  " + SystemInfo.getClockFrequencyEMMC());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("Pixel Frequency   :  " + SystemInfo.getClockFrequencyPixel());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("VEC Frequency     :  " + SystemInfo.getClockFrequencyVEC());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("HDMI Frequency    :  " + SystemInfo.getClockFrequencyHDMI());}
        catch(UnsupportedOperationException ex){}
        try{System.out.println("DPI Frequency     :  " + SystemInfo.getClockFrequencyDPI());}
        catch(UnsupportedOperationException ex){}

        System.out.println();
        System.out.println();
        System.out.println("Exiting SystemInfoExample");
    }
}
// END SNIPPET: system-info-snippet
