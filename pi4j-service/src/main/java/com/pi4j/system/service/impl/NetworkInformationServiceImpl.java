package com.pi4j.system.service.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: OSGi Service
 * FILENAME      :  NetworkInformationServiceImpl.java  
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


import java.io.IOException;
import com.pi4j.system.NetworkInfo;
import com.pi4j.system.service.NetworkInformationService;

@SuppressWarnings("PackageAccessibility")
public class NetworkInformationServiceImpl implements NetworkInformationService
{
    public NetworkInformationServiceImpl()
    {
    }
    
    @Override
    public String getHostname() throws IOException, InterruptedException
    {
        return NetworkInfo.getHostname();
    }

    @Override
    public String getFQDN() throws IOException, InterruptedException
    {
        return NetworkInfo.getFQDN();
    }

    @Override
    public String[] getIPAddresses() throws IOException, InterruptedException
    {
        return NetworkInfo.getIPAddresses();
    }

    @Override
    public String getIPAddress() throws IOException, InterruptedException
    {
        return NetworkInfo.getIPAddress();
    }

    @Override
    public String[] getFQDNs() throws IOException, InterruptedException
    {
        return NetworkInfo.getFQDNs();
    }

    @Override
    public String[] getNameservers() throws IOException, InterruptedException
    {
        return NetworkInfo.getNameservers();
    }    
}
