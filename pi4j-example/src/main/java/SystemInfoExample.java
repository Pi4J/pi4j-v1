// START SNIPPET: system-info-snippet


/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Examples
 * FILENAME      :  SystemInfoExample.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2013 Pi4J
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

import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;

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
        System.out.println("HARDWARE INFO");
        System.out.println("----------------------------------------------------");
        System.out.println("Serial Number     :  " + SystemInfo.getSerial());
        System.out.println("CPU Revision      :  " + SystemInfo.getCpuRevision());
        System.out.println("CPU Architecture  :  " + SystemInfo.getCpuArchitecture());
        System.out.println("CPU Part          :  " + SystemInfo.getCpuPart());
        System.out.println("CPU Temperature   :  " + SystemInfo.getCpuTemperature());
        System.out.println("CPU Core Voltage  :  " + SystemInfo.getCpuVoltage());
        System.out.println("MIPS              :  " + SystemInfo.getBogoMIPS());
        System.out.println("Processor         :  " + SystemInfo.getProcessor());
        System.out.println("Hardware Revision :  " + SystemInfo.getRevision());
        System.out.println("Is Hard Float ABI :  " + SystemInfo.isHardFloatAbi());
        System.out.println("Board Type        :  " + SystemInfo.getBoardType().name());
        
        System.out.println("----------------------------------------------------");
        System.out.println("MEMORY INFO");
        System.out.println("----------------------------------------------------");
        System.out.println("Total Memory      :  " + SystemInfo.getMemoryTotal());
        System.out.println("Used Memory       :  " + SystemInfo.getMemoryUsed());
        System.out.println("Free Memory       :  " + SystemInfo.getMemoryFree());
        System.out.println("Shared Memory     :  " + SystemInfo.getMemoryShared());
        System.out.println("Memory Buffers    :  " + SystemInfo.getMemoryBuffers());
        System.out.println("Cached Memory     :  " + SystemInfo.getMemoryCached());
        System.out.println("SDRAM_C Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_C());
        System.out.println("SDRAM_I Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_I());
        System.out.println("SDRAM_P Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_P());

        System.out.println("----------------------------------------------------");
        System.out.println("OPERATING SYSTEM INFO");
        System.out.println("----------------------------------------------------");
        System.out.println("OS Name           :  " + SystemInfo.getOsName());
        System.out.println("OS Version        :  " + SystemInfo.getOsVersion());
        System.out.println("OS Architecture   :  " + SystemInfo.getOsArch());
        System.out.println("OS Firmware Build :  " + SystemInfo.getOsFirmwareBuild());
        System.out.println("OS Firmware Date  :  " + SystemInfo.getOsFirmwareDate());
        
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
        System.out.println("H264 Codec Enabled:  " + SystemInfo.getCodecH264Enabled());
        System.out.println("MPG2 Codec Enabled:  " + SystemInfo.getCodecMPG2Enabled());
        System.out.println("WVC1 Codec Enabled:  " + SystemInfo.getCodecWVC1Enabled());

        System.out.println("----------------------------------------------------");
        System.out.println("CLOCK INFO");
        System.out.println("----------------------------------------------------");
        System.out.println("ARM Frequency     :  " + SystemInfo.getClockFrequencyArm());
        System.out.println("CORE Frequency    :  " + SystemInfo.getClockFrequencyCore());
        System.out.println("H264 Frequency    :  " + SystemInfo.getClockFrequencyH264());
        System.out.println("ISP Frequency     :  " + SystemInfo.getClockFrequencyISP());
        System.out.println("V3D Frequency     :  " + SystemInfo.getClockFrequencyV3D());
        System.out.println("UART Frequency    :  " + SystemInfo.getClockFrequencyUART());
        System.out.println("PWM Frequency     :  " + SystemInfo.getClockFrequencyPWM());
        System.out.println("EMMC Frequency    :  " + SystemInfo.getClockFrequencyEMMC());
        System.out.println("Pixel Frequency   :  " + SystemInfo.getClockFrequencyPixel());
        System.out.println("VEC Frequency     :  " + SystemInfo.getClockFrequencyVEC());
        System.out.println("HDMI Frequency    :  " + SystemInfo.getClockFrequencyHDMI());
        System.out.println("DPI Frequency     :  " + SystemInfo.getClockFrequencyDPI());
        
            
        System.out.println();
        System.out.println();
    }
}
// END SNIPPET: system-info-snippet
