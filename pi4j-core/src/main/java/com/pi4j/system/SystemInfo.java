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
 * Copyright (C) 2012 Pi4J
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


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

public class SystemInfo {

    // private constructor 
    private SystemInfo() {
        // forbid object construction 
    }
        
    private static Map<String, String> cpuInfo;

    private static String getCpuInfo(String target) throws IOException, InterruptedException {
        // if the CPU data has not been previously acquired, then acquire it now
        if (cpuInfo == null) {
            cpuInfo = new HashMap<String, String>();
            Process p = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                String parts[] = line.split(":", 2);
                if (parts.length >= 2 && !parts[0].trim().isEmpty() && !parts[1].trim().isEmpty()) {
                    cpuInfo.put(parts[0].trim(), parts[1].trim());
                }
                line = reader.readLine();
            }
        }

        if (cpuInfo.containsKey(target)) {
            return cpuInfo.get(target);
        }

        throw new RuntimeException("Invalid target: " + target);
    }

    public static String getProcessor() throws IOException, InterruptedException {
        return getCpuInfo("Processor");
    }

    public static String getBogoMIPS() throws IOException, InterruptedException {
        return getCpuInfo("BogoMIPS");
    }

    public static String[] getCpuFeatures() throws IOException, InterruptedException {
        return getCpuInfo("Features").split(" ");
    }

    public static String getCpuImplementer() throws IOException, InterruptedException {
        return getCpuInfo("CPU implementer");
    }

    public static String getCpuArchitecture() throws IOException, InterruptedException {
        return getCpuInfo("CPU architecture");
    }

    public static String getCpuVariant() throws IOException, InterruptedException {
        return getCpuInfo("CPU variant");
    }

    public static String getCpuPart() throws IOException, InterruptedException {
        return getCpuInfo("CPU part");
    }

    public static String getCpuRevision() throws IOException, InterruptedException {
        return getCpuInfo("CPU revision");
    }

    public static String getHardware() throws IOException, InterruptedException {
        return getCpuInfo("Hardware");
    }

    public static String getRevision() throws IOException, InterruptedException {
        return getCpuInfo("Revision");
    }

    public static String getSerial() throws IOException, InterruptedException {
        return getCpuInfo("Serial");
    }

    public static String getOsName() {
        return System.getProperty("os.name");
    }

    public static String getOsVersion() {
        return System.getProperty("os.version");
    }

    public static String getOsArch()  {
        return System.getProperty("os.arch");
    }
    
    public static String getJavaVendor()  {
        return System.getProperty("java.vendor");
    }
 
    public static String getJavaVendorUrl() {
        return System.getProperty("java.vendor.url");
    }
 
    public static String getJavaVersion() {
        return System.getProperty("java.version");
    }

    public static String getJavaVirtualMachine() {
        return System.getProperty("java.vm.name");
    }

    public static String getJavaRuntime() {
        return AccessController.doPrivileged(new PrivilegedAction<String>()  {
            public String run()  {
                return System.getProperty("java.runtime.name");
            }
        });
    }
    
    /*
     * this method was partially derived from :: (project) jogamp / (developer) sgothel
     * https://github.com/sgothel/gluegen/blob/master/src/java/jogamp/common/os/PlatformPropsImpl.java#L160
     * https://github.com/sgothel/gluegen/blob/master/LICENSE.txt
     */
    public static boolean isHardFloatAbi() {
        
        return AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
            private final String[] gnueabihf = new String[] { "gnueabihf", "armhf" };
            public Boolean run() {                    
                if ( contains(System.getProperty("sun.boot.library.path"), gnueabihf) ||
                     contains(System.getProperty("java.library.path"), gnueabihf) ||
                     contains(System.getProperty("java.home"), gnueabihf) || 
                     getBashVersionInfo().contains("gnueabihf") ||
                     hasReadElfTag("Tag_ABI_HardFP_use")) {
                        return true; //
                }
                return false;
            } } );
    }
    
    /*
     * this method will to obtain the version info string from the 'bash' program
     * (this method is used to help determine the HARD-FLOAT / SOFT-FLOAT ABI of the system)
     */
    private static String getBashVersionInfo() {
        String versionInfo = "";
        try {
            
            String cmd = "bash --version";
            Process p = Runtime.getRuntime().exec(cmd); 
            p.waitFor(); 
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream())); 
            String line = reader.readLine();
            if(p.exitValue() == 0) {
                while(line != null) {
                    if(!line.isEmpty()) { 
                        versionInfo = line; // return only first output line of version info
                        break;
                    }
                    line = reader.readLine();
                }
            }
        }
        catch (IOException ioe) { ioe.printStackTrace(); }
        catch (InterruptedException ie) { ie.printStackTrace(); }
        return versionInfo;
    }

    /*
     * this method will determine if a specified tag exists from the elf info in the '/proc/self/exe' program
     * (this method is used to help determine the HARD-FLOAT / SOFT-FLOAT ABI of the system)
     */    
    private static boolean hasReadElfTag(String tag) {
        String tagValue = getReadElfTag(tag);
        if(tagValue != null && !tagValue.isEmpty())
            return true;
        return false;
    }
    
    /*
     * this method will obtain a specified tag value from the elf info in the '/proc/self/exe' program
     * (this method is used to help determine the HARD-FLOAT / SOFT-FLOAT ABI of the system)
     */    
    private static String getReadElfTag(String tag) {
        String tagValue = null;
        try {
            String cmd = "/usr/bin/readelf -A /proc/self/exe";
            Process p = Runtime.getRuntime().exec(cmd); 
            p.waitFor();
            if(p.exitValue() == 0) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream())); 
                String line = reader.readLine();
                while(line != null) {
                    line = line.trim();
                    if (line.startsWith(tag) && line.contains(":")) {
                        String lineParts[] = line.split(":", 2);
                        if(lineParts.length > 1)
                            tagValue = lineParts[1].trim();
                        break;
                    }
                    line = reader.readLine();
                }
            }
        }
        catch (IOException ioe) { ioe.printStackTrace(); }
        catch (InterruptedException ie) { ie.printStackTrace(); }
        return tagValue;
    }
    
    /*
     * this supporting method was derived from this source:
     * https://github.com/sgothel/gluegen/blob/master/src/java/jogamp/common/os/PlatformPropsImpl.java#L160
     * https://github.com/sgothel/gluegen/blob/master/LICENSE.txt
     */
    private static final boolean contains(String data, String[] search)  {
        if (null != data && null != search) { 
            for (int i=0; i<search.length; i++) {
                if (data.indexOf(search[i]) >= 0) {
                    return true;
                }
            }
        }
        return false;
    }    
}
