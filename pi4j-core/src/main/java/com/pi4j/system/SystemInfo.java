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
import java.util.HashMap;
import java.util.Map;

public class SystemInfo
{
    private static Map<String, String> cpuInfo;

    private static String getCpuInfo(String target) throws IOException, InterruptedException
    {
        // if the CPU data has not been previously acquired, then acquire it now
        if (cpuInfo == null)
        {
            cpuInfo = new HashMap<String, String>();
            Process p = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = reader.readLine();
            while (line != null)
            {
                String parts[] = line.split(":", 2);
                if (parts.length >= 2 && !parts[0].trim().isEmpty() && !parts[1].trim().isEmpty())
                {
                    cpuInfo.put(parts[0].trim(), parts[1].trim());
                }
                line = reader.readLine();
            }
        }

        if (cpuInfo.containsKey(target))
            return cpuInfo.get(target);

        throw new RuntimeException("Invalid target: " + target);
    }

    public static String getProcessor() throws IOException, InterruptedException
    {
        return getCpuInfo("Processor");
    }

    public static String getBogoMIPS() throws IOException, InterruptedException
    {
        return getCpuInfo("BogoMIPS");
    }

    public static String[] getCpuFeatures() throws IOException, InterruptedException
    {
        return getCpuInfo("Features").split(" ");
    }

    public static String getCpuImplementer() throws IOException, InterruptedException
    {
        return getCpuInfo("CPU implementer");
    }

    public static String getCpuArchitecture() throws IOException, InterruptedException
    {
        return getCpuInfo("CPU architecture");
    }

    public static String getCpuVariant() throws IOException, InterruptedException
    {
        return getCpuInfo("CPU variant");
    }

    public static String getCpuPart() throws IOException, InterruptedException
    {
        return getCpuInfo("CPU part");
    }

    public static String getCpuRevision() throws IOException, InterruptedException
    {
        return getCpuInfo("CPU revision");
    }

    public static String getHardware() throws IOException, InterruptedException
    {
        return getCpuInfo("Hardware");
    }

    public static String getRevision() throws IOException, InterruptedException
    {
        return getCpuInfo("Revision");
    }

    public static String getSerial() throws IOException, InterruptedException
    {
        return getCpuInfo("Serial");
    }
}
