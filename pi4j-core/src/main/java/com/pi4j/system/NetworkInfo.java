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
 * Copyright (C) 2012 - 2019 Pi4J
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


import com.pi4j.util.ExecUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        return ExecUtil.execute("hostname -i")[0];
    }

    public static String[] getFQDNs() throws IOException, InterruptedException {
        return ExecUtil.execute("hostname --all-fqdns", " ");
    }

    public static String[] getNameservers() throws IOException, InterruptedException {
        String[] nameservers = ExecUtil.execute("cat /etc/resolv.conf");
        List<String> result = new ArrayList<>();
        for (String nameserver : nameservers) {
            if (nameserver.startsWith("nameserver")) {
                result.add(nameserver.substring(11).trim());
            }
        }
        return result.toArray(new String[result.size()]);
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
