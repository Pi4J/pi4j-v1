package com.pi4j.service.osgi;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: OSGi Service
 * FILENAME      :  Activator.java
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

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.pi4j.io.gpio.service.GpioService;
import com.pi4j.io.gpio.service.impl.GpioServiceImpl;
import com.pi4j.system.service.NetworkInformationService;
import com.pi4j.system.service.SystemInformationService;
import com.pi4j.system.service.impl.NetworkInformationServiceImpl;
import com.pi4j.system.service.impl.SystemInformationServiceImpl;

public class Activator implements BundleActivator
{
    @Override
    public void start(BundleContext bundleContext) throws Exception
    {
        // create a new GPIO service instance
        // and register services with OSGi
        bundleContext.registerService(GpioService.class.getName(), new GpioServiceImpl(), null);
        bundleContext.registerService(SystemInformationService.class.getName(), new SystemInformationServiceImpl(), null);
        bundleContext.registerService(NetworkInformationService.class.getName(), new NetworkInformationServiceImpl(), null);        
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception
    {        
    }
}
