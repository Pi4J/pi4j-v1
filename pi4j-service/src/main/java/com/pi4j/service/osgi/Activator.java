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
 * Copyright (C) 2012 - 2015 Pi4J
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
