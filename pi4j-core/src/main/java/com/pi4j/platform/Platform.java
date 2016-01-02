package com.pi4j.platform;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  Platform.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
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

import com.pi4j.io.gpio.*;
import com.pi4j.io.i2c.I2CFactoryProvider;
import com.pi4j.io.i2c.I2CFactoryProviderBanana;
import com.pi4j.io.i2c.I2CFactoryProviderRaspberry;
import com.pi4j.system.SystemInfoProvider;
import com.pi4j.system.impl.BananaPiSystemInfoProvider;
import com.pi4j.system.impl.BananaProSystemInfoProvider;
import com.pi4j.system.impl.OdroidSystemInfoProvider;
import com.pi4j.system.impl.RaspiSystemInfoProvider;

/**
 * <p>
 * This enumeration defines the platforms supported by Pi4J.
 * </p>
 *
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public enum Platform {
    // SUPPORTED PLATFORMS
    RASPBERRYPI("raspberrypi", "Raspberry Pi"),
    BANANAPI("bananapi", "BananaPi"),
    BANANAPRO("bananapro", "BananaPro"),
    ODROID("odroid", "Odroid");

    // private variables
    protected String platformId = null;
    protected String label = null;

    /**
     * Private default constructor
     * @param platformId
     * @param label
     */
    private Platform(String platformId, String label){
        this.platformId = platformId;
        this.label = label;
    }

    /**
     * Get the platform's friendly string name/label.
     * @return label of platform
     */
    public String getLabel(){
        return this.label;
    }

    /**
     * Get the platform's friendly string name/label.
     * @return label of platform
     */
    public String label(){
        return getLabel();
    }

    /**
     * Get the platform's unique identifier string.
     * @return platform id string
     */
    public String getId(){
        return platformId;
    }

    /**
     * Get the platform's unique identifier string.
     * @return platform id string
     */
    public String id(){
        return getId();
    }

    /**
     * Lookup a platform enumeration by the platform's unique identifier string.
     * @return platform enumeration
     */
    public static Platform fromId(String platformId){
        for(Platform platform : Platform.values()){
            if(platform.id().equalsIgnoreCase(platformId))
                return platform;
        }
        return null;
    }


    public GpioProvider getGpioProvider(){
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
            case ODROID: {
                return new BananaPiGpioProvider();
            }
            default: {
                // if a platform cannot be determine, then assume it's the default RaspberryPi
                return new RaspiGpioProvider();
            }
        }
    }

    public I2CFactoryProvider getI2CFactoryProvider(){
        return getI2CFactoryProvider(this);
    }

    public static I2CFactoryProvider getI2CFactoryProvider(Platform platform){
        // return the I2C provider based on the provided platform
        switch(platform) {
            case RASPBERRYPI: {
                return new I2CFactoryProviderRaspberry();
            }
            case BANANAPI: {
                return new I2CFactoryProviderBanana();
            }
            case BANANAPRO: {
                return new I2CFactoryProviderBanana();
            }
            case ODROID: {
                // TODO: IMPLEMENT ODROID PROVIDER
                return new I2CFactoryProviderBanana();
            }
            default: {
                // if a platform cannot be determine, then assume it's the default RaspberryPi
                return new I2CFactoryProviderRaspberry();
            }
        }
    }

    public SystemInfoProvider getSystemInfoProvider(){
        return getSystemInfoProvider(this);
    }

    public static SystemInfoProvider getSystemInfoProvider(Platform platform){
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
            case ODROID: {
                // TODO: IMPLEMENT ODROID PROVIDER
                return new OdroidSystemInfoProvider();
            }
            default: {
                // if a platform cannot be determine, then assume it's the default RaspberryPi
                return new RaspiSystemInfoProvider();
            }
        }
    }
}
