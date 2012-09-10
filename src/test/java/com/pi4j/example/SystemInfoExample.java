// START SNIPPET: system-info-snippet
package com.pi4j.example;

import java.io.IOException;

import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;

/**
 * This example code demonstrates how to access soem of the system information and network
 * information form the Raspberry Pi.
 * 
 * @author Robert Savage
 */
public class SystemInfoExample
{
    public static void main(String[] args) throws InterruptedException, IOException
    {
        // display a few of the available system information properties
        System.out.println("Serial Number     :  " + SystemInfo.getSerial());
        System.out.println("CPU Revision      :  " + SystemInfo.getCpuRevision());
        System.out.println("CPU Architecture  :  " + SystemInfo.getCpuArchitecture());
        System.out.println("CPU Part          :  " + SystemInfo.getCpuPart());
        System.out.println("MIPS              :  " + SystemInfo.getBogoMIPS());
        System.out.println("Processor         :  " + SystemInfo.getProcessor());
        System.out.println("Hardware Revision :  " + SystemInfo.getRevision());

        // display some of the network information
        System.out.println("Hostname          :  " + NetworkInfo.getHostname());
        for (String ipAddress : NetworkInfo.getIPAddresses())
            System.out.println("IP Addresses      :  " + ipAddress);
        for (String fqdn : NetworkInfo.getFQDNs())
            System.out.println("FQDN              :  " + fqdn);
        for (String nameserver : NetworkInfo.getNameservers())
            System.out.println("Nameserver        :  " + nameserver);
    }
}
// END SNIPPET: system-info-snippet