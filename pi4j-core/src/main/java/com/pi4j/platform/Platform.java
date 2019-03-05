package com.pi4j.platform;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  Platform.java
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

import com.pi4j.io.gpio.*;
import com.pi4j.io.i2c.I2CFactoryProvider;
import com.pi4j.io.i2c.impl.I2CProviderImpl;
import com.pi4j.system.SystemInfoProvider;
import com.pi4j.system.impl.*;

/**
 * <p>
 * This enumeration defines the platforms supported by Pi4J.
 * </p>
 *
 * @see <a href="https://pi4j.com/">https://pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public enum Platform {
    // SUPPORTED PLATFORMS
    RASPBERRYPI("raspberrypi", "Raspberry Pi"),
    BANANAPI("bananapi", "BananaPi"),
    BANANAPRO("bananapro", "BananaPro"),
    BPI("bpi", "Synovoip BPI"),
    ODROID("odroid", "Odroid"),
    ORANGEPI("orangepi", "OrangePi"),
    NANOPI("nanopi", "NanoPi"),
    SIMULATED("simulated", "Simulated");

    // private variables
    protected String platformId = null;
    protected String label = null;

    /**
     * Private default constructor
     * @param platformId
     * @param label
     */
    private Platform(String platformId, String label) {
        this.platformId = platformId;
        this.label = label;
    }

    /**
     * Get the platform's friendly string name/label.
     * @return label of platform
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Get the platform's friendly string name/label.
     * @return label of platform
     */
    public String label() {
        return getLabel();
    }

    /**
     * Get the platform's unique identifier string.
     * @return platform id string
     */
    public String getId() {
        return platformId;
    }

    /**
     * Get the platform's unique identifier string.
     * @return platform id string
     */
    public String id() {
        return getId();
    }

    /**
     * Lookup a platform enumeration by the platform's unique identifier string.
     * @return platform enumeration
     */
    public static Platform fromId(String platformId) {
        for(Platform platform : Platform.values()) {
            if(platform.id().equalsIgnoreCase(platformId))
                return platform;
        }
        return null;
    }


    public GpioProvider getGpioProvider() {
        return getGpioProvider(this);
    }

    public static GpioProvider getGpioProvider(Platform platform) {
        // create the provider based on the PlatformManager's selected platform
        switch (PlatformManager.getPlatform()) {
            case RASPBERRYPI: {
                return new RaspiGpioProvider();
            }
            case BANANAPI: {
                return new BananaPiGpioProvider();
            }
            case BANANAPRO: {
                return new BananaProGpioProvider();
            }
            case BPI: {
                return new BpiGpioProvider();
            }
            case ODROID: {
                return new OdroidGpioProvider();
            }
            case ORANGEPI: {
                return new OrangePiGpioProvider();
            }
            case NANOPI: {
                return new NanoPiGpioProvider();
            }
            case SIMULATED: {
                return new SimulatedGpioProvider();
            }
            default: {
                // if a platform cannot be determine, then assume it's the default RaspberryPi
                return new RaspiGpioProvider();
            }
        }
    }

    public I2CFactoryProvider getI2CFactoryProvider() {
        return getI2CFactoryProvider(this);
    }

    public static I2CFactoryProvider getI2CFactoryProvider(Platform platform) {
        return new I2CProviderImpl();
    }

    public SystemInfoProvider getSystemInfoProvider() {
        return getSystemInfoProvider(this);
    }

    public static SystemInfoProvider getSystemInfoProvider(Platform platform) {
        // return the system info provider based on the provided platform
        switch(platform) {
            case RASPBERRYPI: {
                return new RaspiSystemInfoProvider();
            }
            case BANANAPI: {
                return new BananaPiSystemInfoProvider();
            }
            case BANANAPRO: {
                return new BananaProSystemInfoProvider();
            }
            case BPI: {
                return new BpiSystemInfoProvider();
            }
            case ODROID: {
                return new OdroidSystemInfoProvider();
            }
            case ORANGEPI: {
                return new OrangePiSystemInfoProvider();
            }
            case NANOPI: {
                return new NanoPiSystemInfoProvider();
            }
            default: {
                // if a platform cannot be determine, then assume it's the default RaspberryPi
                return new RaspiSystemInfoProvider();
            }
        }
    }
}
