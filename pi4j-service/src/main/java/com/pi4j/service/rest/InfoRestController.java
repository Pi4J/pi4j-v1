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
import java.util.Map;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides a REST interface to expose all board info.
 *
 * Based on https://pi4j.com/1.2/example/system-info.html
 *
 * @author Frank Delporte (<a href="https://www.webtechie.be">https://www.webtechie.be</a>)
 */
@RestController
@RequestMapping("info")
public class InfoRestController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Get the platform info.
     */
    @GetMapping(path = "platform", produces = "application/json")
    public Map<String, String> getPlatform() {
        Map<String, String> map = new TreeMap<>();

        Platform platform = PlatformManager.getPlatform();

        try {
            map.put("id", platform.getId());
        } catch (NullPointerException ex) {
            map.put("id", "UNKNOWN");
        }

        map.put("label", platform.getLabel());
        map.put("name", platform.name());

       return map;
    }

    /**
     * Get the hardware info.
     */
    @GetMapping(path = "hardware", produces = "application/json")
    public Map<String, String> getHardwareInfo() {
        Map<String, String> map = new TreeMap<>();

        try {
            map.put("SerialNumber", SystemInfo.getSerial());
        } catch (Exception ex) {
            logger.error("SerialNumber not available, error: {}", ex.getMessage());
        }

        try {
            map.put("CpuRevision", SystemInfo.getCpuRevision());
        } catch (Exception ex) {
            logger.error("CpuRevision not available, error: {}", ex.getMessage());
        }

        try {
            map.put("CpuArchitecture", SystemInfo.getCpuArchitecture());
        } catch (Exception ex) {
            logger.error("CpuArchitecture not available, error: {}", ex.getMessage());
        }

        try {
            map.put("CpuPart", SystemInfo.getCpuPart());
        } catch (Exception ex) {
            logger.error("CpuPart not available, error: {}", ex.getMessage());
        }

        try {
            map.put("CpuTemperature", String.valueOf(SystemInfo.getCpuTemperature()));
        } catch (Exception ex) {
            logger.error("CpuTemperature not available, error: {}", ex.getMessage());
        }

        try {
            map.put("CpuCoreVoltage", String.valueOf(SystemInfo.getCpuVoltage()));
        } catch (Exception ex) {
            logger.error("CpuCoreVoltage not available, error: {}", ex.getMessage());
        }

        try {
            map.put("CpuModelName", SystemInfo.getModelName());
        } catch (Exception ex) {
            logger.error("CpuModelName not available, error: {}", ex.getMessage());
        }

        try {
            map.put("Processor", SystemInfo.getProcessor());
        } catch (Exception ex) {
            logger.error("Processor not available, error: {}", ex.getMessage());
        }

        try {
            map.put("Hardware", SystemInfo.getHardware());
        } catch (Exception ex) {
            logger.error("Hardware not available, error: {}", ex.getMessage());
        }

        try {
            map.put("HardwareRevision", SystemInfo.getRevision());
        } catch (Exception ex) {
            logger.error("HardwareRevision not available, error: {}", ex.getMessage());
        }

        try {
            map.put("IsHardFloatABI", String.valueOf(SystemInfo.isHardFloatAbi()));
        } catch (Exception ex) {
            logger.error("IsHardFloatABI not available, error: {}", ex.getMessage());
        }

        try {
            map.put("BoardType", SystemInfo.getBoardType().name());
        } catch (Exception ex) {
            logger.error("BoardType not available, error: {}", ex.getMessage());
        }

        return map;
    }

    /**
     * Get the memory info.
     */
    @GetMapping(path = "memory", produces = "application/json")
    public Map<String, Float> getMemoryInfo() {
        Map<String, Float> map = new TreeMap<>();

        try {
            map.put("Total", (float) SystemInfo.getMemoryTotal());
        } catch (Exception ex) {
            logger.error("Total memory not available, error: {}", ex.getMessage());
        }

        try {
            map.put("Used", (float) SystemInfo.getMemoryUsed());
        } catch (Exception ex) {
            logger.error("Used memory not available, error: {}", ex.getMessage());
        }

        try {
            map.put("Free", (float) SystemInfo.getMemoryFree());
        } catch (Exception ex) {
            logger.error("Free memory not available, error: {}", ex.getMessage());
        }

        try {
            map.put("Shared", (float) SystemInfo.getMemoryShared());
        } catch (Exception ex) {
            logger.error("Shared memory not available, error: {}", ex.getMessage());
        }

        try {
            map.put("Buffers", (float) SystemInfo.getMemoryBuffers());
        } catch (Exception ex) {
            logger.error("Buffers memory not available, error: {}", ex.getMessage());
        }

        try {
            map.put("Cached", (float) SystemInfo.getMemoryCached());
        } catch (Exception ex) {
            logger.error("Cached memory not available, error: {}", ex.getMessage());
        }

        try {
            map.put("SDRamCVoltage", SystemInfo.getMemoryVoltageSDRam_C());
        } catch (Exception ex) {
            logger.error("SDRamCVoltage not available, error: {}", ex.getMessage());
        }

        try {
            map.put("SDRamIVoltage", SystemInfo.getMemoryVoltageSDRam_I());
        } catch (Exception ex) {
            logger.error("SDRamIVoltage not available, error: {}", ex.getMessage());
        }

        try {
            map.put("SDRamPVoltage", SystemInfo.getMemoryVoltageSDRam_P());
        } catch (Exception ex) {
            logger.error("SDRamPVoltage not available, error: {}", ex.getMessage());
        }

        return map;
    }

    /**
     * Get the OS info.
     */
    @GetMapping(path = "os", produces = "application/json")
    public Map<String, String> getOsInfo() {
        Map<String, String> map = new TreeMap<>();

        try {
            map.put("Name", SystemInfo.getOsName());
        } catch (Exception ex) {
            logger.error("OS name not available, error: {}", ex.getMessage());
        }
        try {
            map.put("Version", SystemInfo.getOsVersion());
        } catch (Exception ex) {
            logger.error("OS version not available, error: {}", ex.getMessage());
        }
        try {
            map.put("Architecture", SystemInfo.getOsArch());
        } catch (Exception ex) {
            logger.error("OS architecture not available, error: {}", ex.getMessage());
        }
        try {
            map.put("FirmwareBuild", SystemInfo.getOsFirmwareBuild());
        } catch (Exception ex) {
            logger.error("OS firmware build not available, error: {}", ex.getMessage());
        }
        try {
            map.put("FirmwareDate", SystemInfo.getOsFirmwareDate());
        } catch (Exception ex) {
            logger.error("OS firmware date not available, error: {}", ex.getMessage());
        }

        return map;
    }

    /**
     * Get the Java info.
     */
    @GetMapping(path = "java", produces = "application/json")
    public Map<String, String> getJavaInfo() {
        Map<String, String> map = new TreeMap<>();

        map.put("Vendor ", SystemInfo.getJavaVendor());
        map.put("VendorURL", SystemInfo.getJavaVendorUrl());
        map.put("Version", SystemInfo.getJavaVersion());
        map.put("VM", SystemInfo.getJavaVirtualMachine());
        map.put("Runtime", SystemInfo.getJavaRuntime());

        return map;
    }

    /**
     * Get the network info.
     */
    @GetMapping(path = "network", produces = "application/json")
    public Map<String, String> getSystemInfo() {
        Map<String, String> map = new TreeMap<>();

        var counter = 0;

        try {
            map.put("Hostname", NetworkInfo.getHostname());
        } catch (Exception ex) {
            logger.error("Network hostname not available, error: {}", ex.getMessage());
        }

        try {
            for (String ipAddress : NetworkInfo.getIPAddresses()) {
                map.put("IpAddress" + (counter++), ipAddress);
            }
        } catch (Exception ex) {
            logger.error("IP address not available, error: {}", ex.getMessage());
        }

        try {
            counter = 0;
            for (String fqdn : NetworkInfo.getFQDNs()) {
                map.put("FQDN" + (counter++), fqdn);
            }
        } catch (Exception ex) {
            logger.error("FQDN not available, error: {}", ex.getMessage());
        }

        try {
            counter = 0;
            for (String nameserver : NetworkInfo.getNameservers()) {
                map.put("Nameserver" + (counter++), nameserver);
            }
        } catch (Exception ex) {
            logger.error("Nameserver not available, error: {}", ex.getMessage());
        }

        return map;
    }

    /**
     * Get the codec info.
     */
    @GetMapping(path = "codec", produces = "application/json")
    public Map<String, Boolean> getCodecInfo() {
        Map<String, Boolean> map = new TreeMap<>();

        try {
            map.put("H264CodecEnabled", SystemInfo.getCodecH264Enabled());
        } catch (Exception ex) {
            logger.error("H264 codec enabled not available, error: {}", ex.getMessage());
        }
        try {
            map.put("MPG2CodecEnabled", SystemInfo.getCodecMPG2Enabled());
        } catch (Exception ex) {
            logger.error("MPG2 codec enabled not available, error: {}", ex.getMessage());
        }
        try {
            map.put("WVC1CodecEnabled", SystemInfo.getCodecWVC1Enabled());
        } catch (Exception ex) {
            logger.error("WVC1 codec enabled not available, error: {}", ex.getMessage());
        }

        return map;
    }

    /**
     * Get the clock frequencies.
     */
    @GetMapping(path = "frequencies", produces = "application/json")
    public Map<String, Long> getClockInfo() {
        Map<String, Long> map = new TreeMap<>();

        try {
            map.put("ARM", SystemInfo.getClockFrequencyArm());
        } catch (Exception ex) {
            logger.error("ARM frequency not available, error: {}", ex.getMessage());
        }

        try {
            map.put("Core", SystemInfo.getClockFrequencyCore());
        } catch (Exception ex) {
            logger.error("Core frequency not available, error: {}", ex.getMessage());
        }

        try {
            map.put("H264", SystemInfo.getClockFrequencyH264());
        } catch (Exception ex) {
            logger.error("H264 frequency not available, error: {}", ex.getMessage());
        }

        try {
            map.put("ISP", SystemInfo.getClockFrequencyISP());
        } catch (Exception ex) {
            logger.error("ISP frequency not available, error: {}", ex.getMessage());
        }

        try {
            map.put("V3D", SystemInfo.getClockFrequencyV3D());
        } catch (Exception ex) {
            logger.error("V3D frequency not available, error: {}", ex.getMessage());
        }

        try {
            map.put("UART", SystemInfo.getClockFrequencyUART());
        } catch (Exception ex) {
            logger.error("UART frequency not available, error: {}", ex.getMessage());
        }

        try {
            map.put("PWM", SystemInfo.getClockFrequencyPWM());
        } catch (Exception ex) {
            logger.error("PWM frequency not available, error: {}", ex.getMessage());
        }

        try {
            map.put("EMMC", SystemInfo.getClockFrequencyEMMC());
        } catch (Exception ex) {
            logger.error("EMMC frequency not available, error: {}", ex.getMessage());
        }

        try {
            map.put("Pixel", SystemInfo.getClockFrequencyPixel());
        } catch (Exception ex) {
            logger.error("Pixel frequency not available, error: {}", ex.getMessage());
        }

        try {
            map.put("VEC", SystemInfo.getClockFrequencyVEC());
        } catch (Exception ex) {
            logger.error("VEC frequency not available, error: {}", ex.getMessage());
        }

        try {
            map.put("HDMI", SystemInfo.getClockFrequencyHDMI());
        } catch (Exception ex) {
            logger.error("HDMI frequency not available, error: {}", ex.getMessage());
        }

        try {
            map.put("DPI", SystemInfo.getClockFrequencyDPI());
        } catch (Exception ex) {
            logger.error("DPI frequency not available, error: {}", ex.getMessage());
        }

        return map;
    }
}
