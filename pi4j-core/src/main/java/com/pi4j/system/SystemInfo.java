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
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pi4j.util.ExecUtil;
import com.pi4j.util.StringUtil;

public class SystemInfo {

    // private constructor 
    private SystemInfo() {
        // forbid object construction 
    }
    
    public enum BoardType {
        UNKNOWN,
        ModelA_Rev0,
        ModelB_Rev1,
        ModelB_Rev2
    }    
    
    private static Map<String, String> cpuInfo;

    private static String getCpuInfo(String target) throws IOException, InterruptedException {
        // if the CPU data has not been previously acquired, then acquire it now
        if (cpuInfo == null) {
            cpuInfo = new HashMap<String, String>();
            String result[] = ExecUtil.execute("cat /proc/cpuinfo");
            if(result != null){
                for(String line : result) {
                    String parts[] = line.split(":", 2);
                    if (parts.length >= 2 && !parts[0].trim().isEmpty() && !parts[1].trim().isEmpty()) {
                        cpuInfo.put(parts[0].trim(), parts[1].trim());
                    }
                }
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
    
    public static String getOsFirmwareBuild() throws IOException, InterruptedException {
        String result[] = ExecUtil.execute("/opt/vc/bin/vcgencmd version");
        if(result != null){
            for(String line : result) {
                if(line.startsWith("version ")){                
                    return line.substring(8);
                }
            }
        }
        throw new RuntimeException("Invalid command or response.");
    }    

    public static String getOsFirmwareDate() throws IOException, InterruptedException, ParseException {
        String result[] = ExecUtil.execute("/opt/vc/bin/vcgencmd version");
        if(result != null){
            for(String line : result) {
                return line; // return 1st line
            }
        }
        throw new RuntimeException("Invalid command or response.");
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
                if ( StringUtil.contains(System.getProperty("sun.boot.library.path"), gnueabihf) ||
                     StringUtil.contains(System.getProperty("java.library.path"), gnueabihf) ||
                     StringUtil.contains(System.getProperty("java.home"), gnueabihf) || 
                     getBashVersionInfo().contains("gnueabihf") ||
                     hasReadElfTag("Tag_ABI_HardFP_use")) {
                        return true; //
                }
                return false;
            } } );
    }
    

    private static List<Long> getMemory() throws IOException, InterruptedException {
        // Memory information is in the form
        // root@mypi:/home/pi# free -b
        //              total       used       free     shared    buffers     cached
        // Mem:     459771904  144654336  315117568          0   21319680   63713280
        // -/+ buffers/cache:   59621376  400150528
        // Swap:    104853504          0  104853504
        List<Long> values = new ArrayList<>();
        String result[] = ExecUtil.execute("free -b");
        if(result != null){
            for(String line : result) {
                if(line.startsWith("Mem:")){
                    String parts[] = line.split(" ");                    
                    for(String part : parts){
                        part = part.trim();
                        if(!part.isEmpty() && !part.equalsIgnoreCase("Mem:")){
                            values.add(new Long(part));
                        }
                    }
                }
            }
        }
        return values;
    }
    
    public static long getMemoryTotal() throws IOException, InterruptedException {
        List<Long> values = getMemory();
        if(!values.isEmpty() && values.size() > 0){
            return values.get(0); // total memory value is in first position
        }
        return -1;
    }

    public static long getMemoryUsed() throws IOException, InterruptedException {
        List<Long> values = getMemory();
        if(!values.isEmpty() && values.size() > 1){
            return values.get(1); // used memory value is in second position
        }
        return -1;
    }

    public static long getMemoryFree() throws IOException, InterruptedException {
        List<Long> values = getMemory();
        if(!values.isEmpty() && values.size() > 2){
            return values.get(2); // free memory value is in third position
        }
        return -1;
    }

    public static long getMemoryShared() throws IOException, InterruptedException {
        List<Long> values = getMemory();
        if(!values.isEmpty() && values.size() > 3){
            return values.get(3); // shared memory value is in fourth position
        }
        return -1;
    }

    public static long getMemoryBuffers() throws IOException, InterruptedException {
        List<Long> values = getMemory();
        if(!values.isEmpty() && values.size() > 4){
            return values.get(4); // buffers memory value is in fifth position
        }
        return -1;
    }

    public static long getMemoryCached() throws IOException, InterruptedException {
        List<Long> values = getMemory();
        if(!values.isEmpty() && values.size() > 5){
            return values.get(5); // cached memory value is in sixth position
        }
        return -1;
    }

    public static BoardType getBoardType() throws IOException, InterruptedException
    {
        // The following info obtained from:
        // http://www.raspberrypi.org/archives/1929
        // http://raspberryalphaomega.org.uk/?p=428
        // http://www.raspberrypi.org/phpBB3/viewtopic.php?p=281039#p281039
        switch(getRevision())
        {
        case "0002":  // Model B Revision 1
        case "0003":  // Model B Revision 1 + Fuses mod and D14 removed
            return BoardType.ModelB_Rev1;
        case "0004":  // Model B Revision 2 256MB (Sony)
        case "0005":  // Model B Revision 2 256MB (Qisda)
        case "0006":  // Model B Revision 2 256MB (Egoman)
            return BoardType.ModelB_Rev2;
        case "0007":  // Model A 256MB (Egoman)
        case "0008":  // Model A 256MB (Sony)
        case "0009":  // Model A 256MB (Qisda)
            return BoardType.ModelA_Rev0;
        case "000d":  // Model B Revision 2 512MB (Egoman)
        case "000e":  // Model B Revision 2 512MB (Sony)
        case "000f":  // Model B Revision 2 512MB (Qisda)
            return BoardType.ModelB_Rev2;
        default:
            return BoardType.UNKNOWN;
        }
    }

    public static float getCpuTemperature() throws IOException, InterruptedException, NumberFormatException {
        // CPU temperature is in the form
        // pi@mypi$ /opt/vc/bin/vcgencmd measure_temp
        // temp=42.3'C
        // Support for this was added around firmware version 3357xx per info
        // at http://www.raspberrypi.org/phpBB3/viewtopic.php?p=169909#p169909
        String result[] = ExecUtil.execute("/opt/vc/bin/vcgencmd measure_temp");
        if(result != null){
            for(String line : result) {
                String parts[] = line.split("[=']", 3);                
                return Float.parseFloat(parts[1]);
            }
        }
        throw new RuntimeException("Invalid command or response.");
    }

    private static float getVoltage(String id) throws IOException, InterruptedException, NumberFormatException {
        String result[] = ExecUtil.execute("/opt/vc/bin/vcgencmd measure_volts " + id);
        if(result != null){
            for(String line : result) {
                String parts[] = line.split("[=V]", 3);                
                return Float.parseFloat(parts[1]);
            }
        }
        throw new RuntimeException("Invalid command or response.");
    }
    
    public static float getCpuVoltage() throws IOException, InterruptedException, NumberFormatException {
        return getVoltage("core");
    }

    public static float getMemoryVoltageSDRam_C() throws IOException, InterruptedException, NumberFormatException {
        return getVoltage("sdram_c");
    }

    public static float getMemoryVoltageSDRam_I() throws IOException, InterruptedException, NumberFormatException {
        return getVoltage("sdram_i");
    }

    public static float getMemoryVoltageSDRam_P() throws IOException, InterruptedException, NumberFormatException {
        return getVoltage("sdram_p");
    }
    

    private static boolean getCodecEnabled(String codec) throws IOException, InterruptedException {
        String result[] = ExecUtil.execute("/opt/vc/bin/vcgencmd codec_enabled " + codec);
        if(result != null){
            for(String line : result) {
                String parts[] = line.split("=", 2);                
                return parts[1].trim().equalsIgnoreCase("enabled");
            }
        }
        throw new RuntimeException("Invalid command or response.");
    }
    
    public static boolean getCodecH264Enabled() throws IOException, InterruptedException {
        return getCodecEnabled("H264");
    }

    public static boolean getCodecMPG2Enabled() throws IOException, InterruptedException {
        return getCodecEnabled("MPG2");
    }

    public static boolean getCodecWVC1Enabled() throws IOException, InterruptedException {
        return getCodecEnabled("WVC1");
    }
    

    private static long getClockFrequency(String target) throws IOException, InterruptedException {
        String result[] = ExecUtil.execute("/opt/vc/bin/vcgencmd measure_clock " + target.trim());
        if(result != null){
            for(String line : result) {
                String parts[] = line.split("=", 2);                
                return Long.parseLong(parts[1].trim());
            }
        }
        throw new RuntimeException("Invalid command or response.");
    }
    
    public static long getClockFrequencyArm() throws IOException, InterruptedException {
        return getClockFrequency("arm");
    }

    public static long getClockFrequencyCore() throws IOException, InterruptedException {
        return getClockFrequency("core");
    }

    public static long getClockFrequencyH264() throws IOException, InterruptedException {
        return getClockFrequency("h264");
    }

    public static long getClockFrequencyISP() throws IOException, InterruptedException {
        return getClockFrequency("isp");
    }

    public static long getClockFrequencyV3D() throws IOException, InterruptedException {
        return getClockFrequency("v3d");
    }

    public static long getClockFrequencyUART() throws IOException, InterruptedException {
        return getClockFrequency("uart");
    }

    public static long getClockFrequencyPWM() throws IOException, InterruptedException {
        return getClockFrequency("pwm");
    }

    public static long getClockFrequencyEMMC() throws IOException, InterruptedException {
        return getClockFrequency("emmc");
    }
    
    public static long getClockFrequencyPixel() throws IOException, InterruptedException {
        return getClockFrequency("pixel");
    }
    
    public static long getClockFrequencyVEC() throws IOException, InterruptedException {
        return getClockFrequency("vec");
    }
    
    public static long getClockFrequencyHDMI() throws IOException, InterruptedException {
        return getClockFrequency("hdmi");
    }

    public static long getClockFrequencyDPI() throws IOException, InterruptedException {
        return getClockFrequency("dpi");
    }
    
    
    /*
     * this method will to obtain the version info string from the 'bash' program
     * (this method is used to help determine the HARD-FLOAT / SOFT-FLOAT ABI of the system)
     */
    private static String getBashVersionInfo() {
        String versionInfo = "";
        try {
            String result[] = ExecUtil.execute("bash --version");
            for(String line : result) {
                if(!line.isEmpty()) { 
                    versionInfo = line; // return only first output line of version info
                    break;
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
            String result[] = ExecUtil.execute("/usr/bin/readelf -A /proc/self/exe");
            if(result != null){
                for(String line : result) {
                    line = line.trim();
                    if (line.startsWith(tag) && line.contains(":")) {
                        String lineParts[] = line.split(":", 2);
                        if(lineParts.length > 1)
                            tagValue = lineParts[1].trim();
                        break;
                    }
                }
            }            
        }
        catch (IOException ioe) { ioe.printStackTrace(); }
        catch (InterruptedException ie) { ie.printStackTrace(); }
        return tagValue;
    }
}
