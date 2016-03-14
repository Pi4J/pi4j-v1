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
