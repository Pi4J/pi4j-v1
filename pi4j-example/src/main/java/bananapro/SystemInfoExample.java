package bananapro;
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
 * Copyright (C) 2012 - 2018 Pi4J
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

import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;
import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;

import java.io.IOException;
import java.text.ParseException;

/**
 * This example code demonstrates how to access a few of the system information properties and
 * network information from the LeMaker BananaPro board.
 *
 * @author Robert Savage
 */
public class SystemInfoExample {

    public static void main(String[] args) throws InterruptedException, IOException, ParseException, PlatformAlreadyAssignedException {

        // ####################################################################
        //
        // since we are not using the default Raspberry Pi platform, we should
        // explicitly assign the platform as the BananaPro platform.
        //
        // ####################################################################
        PlatformManager.setPlatform(Platform.BANANAPRO);

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

        System.out.println();
    }
}
