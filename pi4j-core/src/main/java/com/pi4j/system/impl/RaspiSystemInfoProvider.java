package com.pi4j.system.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  RaspiSystemInfoProvider.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2015 Pi4J
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

import com.pi4j.system.SystemInfo;
import com.pi4j.system.SystemInfoProvider;
import com.pi4j.util.ExecUtil;

import java.io.IOException;
import java.text.ParseException;

/**
 * Raspberry Pi platform specific implementation of the SystemInfoProvider interface.
 */
public class RaspiSystemInfoProvider extends DefaultSystemInfoProvider implements SystemInfoProvider {

    private long getClockFrequency(String target) throws IOException, InterruptedException {
        String result[] = ExecUtil.execute("/opt/vc/bin/vcgencmd measure_clock " + target.trim());
        if(result != null && result.length > 0){
            for(String line : result) {
                String parts[] = line.split("=", 2);
                return Long.parseLong(parts[1].trim());
            }
        }
        throw new UnsupportedOperationException("Invalid command or response.");
    }

    private boolean getCodecEnabled(String codec) throws IOException, InterruptedException {
        String result[] = ExecUtil.execute("/opt/vc/bin/vcgencmd codec_enabled " + codec);
        if(result != null && result.length > 0){
            for(String line : result) {
                String parts[] = line.split("=", 2);
                return parts[1].trim().equalsIgnoreCase("enabled");
            }
        }
        throw new RuntimeException("Invalid command or response.");
    }

    private float getVoltage(String id) throws IOException, InterruptedException, NumberFormatException {
        String result[] = ExecUtil.execute("/opt/vc/bin/vcgencmd measure_volts " + id);
        if(result != null && result.length > 0){
            for(String line : result) {
                String parts[] = line.split("[=V]", 3);
                return Float.parseFloat(parts[1]);
            }
        }
        throw new UnsupportedOperationException("Invalid command or response.");
    }

    @Override
    public String getModelName() throws IOException, InterruptedException, UnsupportedOperationException {
        return getCpuInfo("model name");
    }

    @Override
    public String getOsFirmwareBuild() throws IOException, InterruptedException, UnsupportedOperationException {
        String result[] = ExecUtil.execute("/opt/vc/bin/vcgencmd version");
        if(result != null){
            for(String line : result) {
                if(line.startsWith("version ")){
                    return line.substring(8);
                }
            }
        }
        throw new UnsupportedOperationException("Invalid command or response.");
    }

    @Override
    public String getOsFirmwareDate() throws IOException, InterruptedException, ParseException, UnsupportedOperationException {
        String result[] = ExecUtil.execute("/opt/vc/bin/vcgencmd version");
        if(result != null && result.length > 0){
            for(String line : result) {
                return line; // return 1st line
            }
        }
        throw new UnsupportedOperationException("Invalid command or response.");
    }

    @Override
    public SystemInfo.BoardType getBoardType() throws IOException, InterruptedException, UnsupportedOperationException {
        String revision = getRevision();
        long irevision = Long.parseLong(revision, 16);
        long scheme = (irevision >> 23) & 0x1;
        long ram = (irevision >> 20) & 0x7;
        long manufacturer = (irevision >> 16) & 0xF;
        long processor = (irevision >> 12) & 0xF;
        long type = (irevision >> 4) & 0xFF;
        long rev = irevision & 0xF;

        // determine board type based on revision scheme
        if (scheme == 0) {
            // The following info obtained from:
            // http://www.raspberrypi.org/archives/1929
            // http://raspberryalphaomega.org.uk/?p=428
            // http://www.raspberrypi.org/phpBB3/viewtopic.php?p=281039#p281039
            // http://elinux.org/RPi_HardwareHistory
            switch (revision) {
                case "0002":  // Model B Revision 1
                case "0003":  // Model B Revision 1 + Fuses mod and D14 removed
                    return SystemInfo.BoardType.ModelB_Rev1;
                case "0004":  // Model B Revision 2 256MB (Sony)
                case "0005":  // Model B Revision 2 256MB (Qisda)
                case "0006":  // Model B Revision 2 256MB (Egoman)
                    return SystemInfo.BoardType.ModelB_Rev2;
                case "0007":  // Model A 256MB (Egoman)
                case "0008":  // Model A 256MB (Sony)
                case "0009":  // Model A 256MB (Qisda)
                    return SystemInfo.BoardType.ModelA_Rev1;
                case "000d":  // Model B Revision 2 512MB (Egoman)
                case "000e":  // Model B Revision 2 512MB (Sony)
                case "000f":  // Model B Revision 2 512MB (Qisda)
                    return SystemInfo.BoardType.ModelB_Rev2;
                case "0010":  // Model B Plus 512MB (Sony)
                {             // Model 2B, Rev 1.1, Quad Core, 1GB (Sony)
                    if (getHardware().equalsIgnoreCase("BCM2709"))
                        return SystemInfo.BoardType.Model2B_Rev1;
                    else
                        return SystemInfo.BoardType.ModelB_Plus_Rev1;
                }
                case "0011":  // Compute Module 512MB (Sony)
                    return SystemInfo.BoardType.Compute_Module_Rev1;
                case "0012":  // Model A Plus 512MB (Sony)
                    return SystemInfo.BoardType.ModelA_Plus_Rev1;
                default:
                    return SystemInfo.BoardType.UNKNOWN;
            }
        }
        else if (type == 4) {
            return SystemInfo.BoardType.Model2B_Rev1;
        }
        else {
            return SystemInfo.BoardType.UNKNOWN;
        }
    }

    @Override
    public float getCpuTemperature() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException {
        // CPU temperature is in the form
        // pi@mypi$ /opt/vc/bin/vcgencmd measure_temp
        // temp=42.3'C
        // Support for this was added around firmware version 3357xx per info
        // at http://www.raspberrypi.org/phpBB3/viewtopic.php?p=169909#p169909
        String result[] = ExecUtil.execute("/opt/vc/bin/vcgencmd measure_temp");
        if(result != null && result.length > 0){
            for(String line : result) {
                String parts[] = line.split("[=']", 3);
                return Float.parseFloat(parts[1]);
            }
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public float getCpuVoltage() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException {
        return getVoltage("core");
    }

    @Override
    public float getMemoryVoltageSDRam_C() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException {
        return getVoltage("sdram_c");
    }

    @Override
    public float getMemoryVoltageSDRam_I() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException {
        return getVoltage("sdram_i");
    }

    @Override
    public float getMemoryVoltageSDRam_P() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException {
        return getVoltage("sdram_p");
    }

    @Override
    public boolean getCodecH264Enabled() throws IOException, InterruptedException, UnsupportedOperationException {
        return getCodecEnabled("H264");
    }

    @Override
    public boolean getCodecMPG2Enabled() throws IOException, InterruptedException, UnsupportedOperationException {
        return getCodecEnabled("MPG2");
    }

    @Override
    public boolean getCodecWVC1Enabled() throws IOException, InterruptedException, UnsupportedOperationException {
        return getCodecEnabled("WVC1");
    }

    @Override
    public long getClockFrequencyArm() throws IOException, InterruptedException, UnsupportedOperationException {
        return getClockFrequency("arm");
    }

    @Override
    public long getClockFrequencyCore() throws IOException, InterruptedException, UnsupportedOperationException {
        return getClockFrequency("core");
    }

    @Override
    public long getClockFrequencyH264() throws IOException, InterruptedException, UnsupportedOperationException {
        return getClockFrequency("h264");
    }

    @Override
    public long getClockFrequencyISP() throws IOException, InterruptedException, UnsupportedOperationException {
        return getClockFrequency("isp");
    }

    @Override
    public long getClockFrequencyV3D() throws IOException, InterruptedException, UnsupportedOperationException {
        return getClockFrequency("v3d");
    }

    @Override
    public long getClockFrequencyUART() throws IOException, InterruptedException, UnsupportedOperationException {
        return getClockFrequency("uart");
    }

    @Override
    public long getClockFrequencyPWM() throws IOException, InterruptedException, UnsupportedOperationException {
        return getClockFrequency("pwm");
    }

    @Override
    public long getClockFrequencyEMMC() throws IOException, InterruptedException, UnsupportedOperationException {
        return getClockFrequency("emmc");
    }

    @Override
    public long getClockFrequencyPixel() throws IOException, InterruptedException, UnsupportedOperationException {
        return getClockFrequency("pixel");
    }

    @Override
    public long getClockFrequencyVEC() throws IOException, InterruptedException, UnsupportedOperationException {
        return getClockFrequency("vec");
    }

    @Override
    public long getClockFrequencyHDMI() throws IOException, InterruptedException, UnsupportedOperationException {
        return getClockFrequency("hdmi");
    }

    @Override
    public long getClockFrequencyDPI() throws IOException, InterruptedException, UnsupportedOperationException {
        return getClockFrequency("dpi");
    }
}
