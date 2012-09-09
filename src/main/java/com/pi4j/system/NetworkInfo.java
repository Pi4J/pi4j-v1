package com.pi4j.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NetworkInfo
{ 
    private static String[] executeCommand(String command) throws IOException, InterruptedException
    {
        return executeCommand(command, null);
    }
    private static String[] executeCommand(String command, String split) throws IOException, InterruptedException
    {
        List<String> result = new ArrayList<String>();
        Process p = Runtime.getRuntime().exec(command);
        p.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = reader.readLine();
        while (line != null)
        {
            if(!line.isEmpty())
            {
                if(split == null || split.isEmpty())
                {
                    result.add(line.trim());
                }
                else
                {
                    String[] parts = line.trim().split(split);
                    for(String part : parts)
                    {
                        if(part != null && !part.isEmpty())
                        {
                            result.add(part.trim());
                        }
                    }
                }
            }
            line = reader.readLine();
        }

        if(result.size() > 0)
            return (String[])result.toArray(new String[0]);
        
        throw new RuntimeException("Invalid command: " + command);
    }

    public static String getHostname() throws IOException, InterruptedException
    {
        return executeCommand("hostname --short")[0];
    }
    public static String getFQDN() throws IOException, InterruptedException
    {
        return executeCommand("hostname --fqdn")[0];
    }
    public static String[] getIPAddresses() throws IOException, InterruptedException
    {
        return executeCommand("hostname --all-ip-addresses", " ");
    }
    public static String getIPAddress() throws IOException, InterruptedException
    {
        return executeCommand("hostname --ip-address")[0];
    }
    public static String[] getFQDNs() throws IOException, InterruptedException
    {
        return executeCommand("hostname --all-fqdns", " ");
    }

    public static String[] getNameservers() throws IOException, InterruptedException
    {
        String[] nameservers = executeCommand("cat /etc/resolv.conf");
        List<String> result = new ArrayList<String>();
        for(String nameserver : nameservers)
        {
            if(nameserver.startsWith("nameserver"))
            {
                result.add(nameserver.substring(11).trim());
            }
        }
        return result.toArray(new String[0]);
    }
    
//    public static Map<String,NetworkInterface> getNetworkInterfaces() throws IOException, InterruptedException
//    {
//        Map<String,NetworkInterface> interfaces = new HashMap<String, NetworkInterface>();
//
//        List<String> result = new ArrayList<String>();
//        Process p = Runtime.getRuntime().exec("ifconfig");
//        p.waitFor();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//        String line = reader.readLine();
//        String key = null;
//        while (line != null)
//        {
//            if(!line.isEmpty())                
//            {
//                // if the line does start with string data and not spaces, then 
//                // it is a new interface record
//                if(!line.startsWith("  "))
//                {
//                    String[] parts = line.split(" ", 2);
//                    key = parts[0].trim();
//                    //interfaces.put(key, value);
//                }
//
//                if(key != null && !key.isEmpty())
//                {
//                    String[] properties = line.split("  ");
//                    for(String property : properties)
//                    {
//                        String[] propparts = property.split(":",2);
//                        
//                    }
//                }
//                
////                if(split != null || split.isEmpty())
////                {
////                    result.add(line.trim());
////                    System.out.println(line.trim());
////                }
////                else
////                {
////                    String[] parts = line.trim().split(split);
////                    for(String part : parts)
////                    {
////                        if(part != null && !part.isEmpty())
////                        {
////                            result.add(part.trim());
////                            System.out.println(part.trim());
////                        }
////                    }
////                }
//            }
//            line = reader.readLine();
//        }
//
//
//        
//        //throw new RuntimeException("Invalid command: " + command);
//        
//        return interfaces;
//    }

}
