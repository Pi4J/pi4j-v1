package com.pi4j.system;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  NetworkInfo.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2014 Pi4J
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
import java.util.ArrayList;
import java.util.List;

import com.pi4j.util.ExecUtil;

public class NetworkInfo  {

    // private constructor 
    private NetworkInfo() {
        // forbid object construction 
    }
    
    public static String getHostname() throws IOException, InterruptedException {
        return ExecUtil.execute("hostname --short")[0];
    }

    public static String getFQDN() throws IOException, InterruptedException {
        return ExecUtil.execute("hostname --fqdn")[0];
    }

    public static String[] getIPAddresses() throws IOException, InterruptedException {
        return ExecUtil.execute("hostname --all-ip-addresses", " ");
    }

    public static String getIPAddress() throws IOException, InterruptedException {
        return ExecUtil.execute("hostname --ip-address")[0];
    }

    public static String[] getFQDNs() throws IOException, InterruptedException {
        return ExecUtil.execute("hostname --all-fqdns", " ");
    }

    public static String[] getNameservers() throws IOException, InterruptedException {
        String[] nameservers = ExecUtil.execute("cat /etc/resolv.conf");
        List<String> result = new ArrayList<String>();
        for (String nameserver : nameservers) {
            if (nameserver.startsWith("nameserver")) {
                result.add(nameserver.substring(11).trim());
            }
        }
        return result.toArray(new String[0]);
    }
    
//    public static Map<String,NetworkInterface> getNetworkInterfaces() throws IOException, InterruptedException {
//
//        Map<String,NetworkInterface> interfaces = new HashMap<String, NetworkInterface>();
//
//        List<String> result = new ArrayList<String>();
//        Process p = Runtime.getRuntime().exec("ifconfig");
//        p.waitFor();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//        String line = reader.readLine();
//        String key = null;
//        while (line != null) {
//        
//            if (!line.isEmpty()) {
//            
//                // if the line does start with string data and not spaces, then 
//                // it is a new interface record
//                if (!line.startsWith("  ")) {
//                
//                    String[] parts = line.split(" ", 2);
//                    key = parts[0].trim();
//                    //interfaces.put(key, value);
//                }
//
//                if (key != null && !key.isEmpty()) {
//                
//                    String[] properties = line.split("  ");
//                    for (String property : properties) {
//                        String[] propparts = property.split(":",2);
//                        
//                    }
//                }
//                
////                if (split != null || split.isEmpty()) {
////                    result.add(line.trim());
////                    System.out.println(line.trim());
////                } else {
////                    String[] parts = line.trim().split(split);
////                    for (String part : parts) {
////                        if (part != null && !part.isEmpty()) {
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
