package com.pi4j.service.rest;

/*-
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java remote services (REST + WebSockets)
 * FILENAME      :  InfoRestController.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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

import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformManager;
import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides a REST interface to expose all board info.
 *
 * Based on https://pi4j.com/1.2/example/system-info.html
 */
@RestController
public class InfoRestController {

    /**
     * Get the platform info.
     */
    @GetMapping(path = "/info/platform", produces = "application/json")
    public Platform getPlatform() {
        return PlatformManager.getPlatform();
    }

    /**
     * Get the system hardware info.
     */
    @GetMapping(path = "/info/hardware", produces = "application/json")
    public Map<String, String> getHardwareInfo() {
        HashMap<String, String> map = new HashMap<>();

        try {
            map.put("SerialNumber", SystemInfo.getSerial());
        } catch (Exception ex) {
        }

        try {
            map.put("CpuRevision", SystemInfo.getCpuRevision());
        } catch (Exception ex) {
        }

        try {
            map.put("CpuArchitecture", SystemInfo.getCpuArchitecture());
        } catch (Exception ex) {
        }

        try {
            map.put("CpuPart", SystemInfo.getCpuPart());
        } catch (Exception ex) {
        }

        try {
            map.put("CpuTemperature", String.valueOf(SystemInfo.getCpuTemperature()));
        } catch (Exception ex) {
        }

        try {
            map.put("CpuCoreVoltage", String.valueOf(SystemInfo.getCpuVoltage()));
        } catch (Exception ex) {
        }

        try {
            map.put("CpuModelName", SystemInfo.getModelName());
        } catch (Exception ex) {
        }

        try {
            map.put("Processor", SystemInfo.getProcessor());
        } catch (Exception ex) {
        }

        try {
            map.put("Hardware", SystemInfo.getHardware());
        } catch (Exception ex) {
        }

        try {
            map.put("HardwareRevision", SystemInfo.getRevision());
        } catch (Exception ex) {
        }

        try {
            map.put("IsHardFloatABI", String.valueOf(SystemInfo.isHardFloatAbi()));
        } catch (Exception ex) {
        }

        try {
            map.put("BoardType", SystemInfo.getBoardType().name());
        } catch (Exception ex) {
        }

        return map;
    }

    /**
     * Get the system memory info.
     */
    @GetMapping(path = "/info/memory", produces = "application/json")
    public Map<String, Float> getMemoryInfo() {
        HashMap<String, Float> map = new HashMap<>();

        try {
            map.put("Total", (float) SystemInfo.getMemoryTotal());
        } catch (Exception ex) {
        }

        try {
            map.put("Used", (float) SystemInfo.getMemoryUsed());
        } catch (Exception ex) {
        }

        try {
            map.put("Free", (float) SystemInfo.getMemoryFree());
        } catch (Exception ex) {
        }

        try {
            map.put("Shared", (float) SystemInfo.getMemoryShared());
        } catch (Exception ex) {
        }

        try {
            map.put("Buffers", (float) SystemInfo.getMemoryBuffers());
        } catch (Exception ex) {
        }

        try {
            map.put("Cached", (float) SystemInfo.getMemoryCached());
        } catch (Exception ex) {
        }

        try {
            map.put("SDRamCVoltage", SystemInfo.getMemoryVoltageSDRam_C());
        } catch (Exception ex) {
        }

        try {
            map.put("SDdRomIVoltage", SystemInfo.getMemoryVoltageSDRam_I());
        } catch (Exception ex) {
        }

        try {
            map.put("SDRamPVoltage", SystemInfo.getMemoryVoltageSDRam_P());
        } catch (Exception ex) {
        }

        return map;
    }

    /**
     * Get the system OS info.
     */
    @GetMapping(path = "/info/os", produces = "application/json")
    public Map<String, String> getOsInfo() {
        HashMap<String, String> map = new HashMap<>();

        try {
            map.put("Name", SystemInfo.getOsName());
        } catch (Exception ex) {
        }
        try {
            map.put("Version", SystemInfo.getOsVersion());
        } catch (Exception ex) {
        }
        try {
            map.put("Architecture", SystemInfo.getOsArch());
        } catch (Exception ex) {
        }
        try {
            map.put("FirmwareBuild", SystemInfo.getOsFirmwareBuild());
        } catch (Exception ex) {
        }
        try {
            map.put("FirmwareDate", SystemInfo.getOsFirmwareDate());
        } catch (Exception ex) {
        }

        return map;
    }

    /**
     * Get the system Java info.
     */
    @GetMapping(path = "/info/java", produces = "application/json")
    public Map<String, String> getJavaInfo() {
        HashMap<String, String> map = new HashMap<>();

        map.put("Vendor ", SystemInfo.getJavaVendor());
        map.put("VendorURL", SystemInfo.getJavaVendorUrl());
        map.put("Version", SystemInfo.getJavaVersion());
        map.put("VN", SystemInfo.getJavaVirtualMachine());
        map.put("Runtime", SystemInfo.getJavaRuntime());

        return map;
    }

    /**
     * Get the network info.
     */
    @GetMapping(path = "/info/network", produces = "application/json")
    public Map<String, String> getSystemInfo() {
        HashMap<String, String> map = new HashMap<>();

        var counter = 0;

        try {
            map.put("Hostname", NetworkInfo.getHostname());
        } catch (Exception ex) {
        }

        try {
            counter = 0;
            for (String ipAddress : NetworkInfo.getIPAddresses()) {
                map.put("IpAddress" + (counter++), ipAddress);
            }
        } catch (Exception ex) {
        }

        try {
            counter = 0;
            for (String fqdn : NetworkInfo.getFQDNs()) {
                map.put("FQDN" + (counter++), fqdn);
            }
        } catch (Exception ex) {
        }

        try {
            counter = 0;
            for (String nameserver : NetworkInfo.getNameservers()) {
                map.put("Nameserver" + (counter++), nameserver);
            }
        } catch (Exception ex) {
        }

        return map;
    }

    /**
     * Get the codec info.
     */
    @GetMapping(path = "/info/codec", produces = "application/json")
    public Map<String, Boolean> getCodecInfo() {
        HashMap<String, Boolean> map = new HashMap<>();

        try {
            map.put("H264CodecEnabled", SystemInfo.getCodecH264Enabled());
        } catch (Exception ex) {
        }
        try {
            map.put("MPG2CodecEnabled", SystemInfo.getCodecMPG2Enabled());
        } catch (Exception ex) {
        }
        try {
            map.put("WVC1CodecEnabled", SystemInfo.getCodecWVC1Enabled());
        } catch (Exception ex) {
        }

        return map;
    }

    /**
     * Get the clock frequencies.
     */
    @GetMapping(path = "/info/frequencies", produces = "application/json")
    public Map<String, Long> getClockInfo() {
        HashMap<String, Long> map = new HashMap<>();

        try {
            map.put("ARM", SystemInfo.getClockFrequencyArm());
        } catch (Exception ex) {
        }

        try {
            map.put("CORE", SystemInfo.getClockFrequencyCore());
        } catch (Exception ex) {
        }

        try {
            map.put("H264", SystemInfo.getClockFrequencyH264());
        } catch (Exception ex) {
        }

        try {
            map.put("ISP", SystemInfo.getClockFrequencyISP());
        } catch (Exception ex) {
        }

        try {
            map.put("V3D", SystemInfo.getClockFrequencyV3D());
        } catch (Exception ex) {
        }

        try {
            map.put("UART", SystemInfo.getClockFrequencyUART());
        } catch (Exception ex) {
        }

        try {
            map.put("PWM", SystemInfo.getClockFrequencyPWM());
        } catch (Exception ex) {
        }

        try {
            map.put("EMMC", SystemInfo.getClockFrequencyEMMC());
        } catch (Exception ex) {
        }

        try {
            map.put("Pixel", SystemInfo.getClockFrequencyPixel());
        } catch (Exception ex) {
        }

        try {
            map.put("VEC", SystemInfo.getClockFrequencyVEC());
        } catch (Exception ex) {
        }

        try {
            map.put("HDMI", SystemInfo.getClockFrequencyHDMI());
        } catch (Exception ex) {
        }

        try {
            map.put("DPI", SystemInfo.getClockFrequencyDPI());
        } catch (Exception ex) {
        }

        return map;
    }
}
