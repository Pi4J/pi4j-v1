package com.pi4j.system;

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
