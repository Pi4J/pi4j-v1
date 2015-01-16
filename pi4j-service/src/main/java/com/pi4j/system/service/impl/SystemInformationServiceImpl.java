package com.pi4j.system.service.impl;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: OSGi Service
 * FILENAME      :  SystemInformationServiceImpl.java  
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
import com.pi4j.system.SystemInfo;
import com.pi4j.system.service.SystemInformationService;

public class SystemInformationServiceImpl implements SystemInformationService
{
    public SystemInformationServiceImpl()
    {
    }
    
    public String getProcessor() throws IOException, InterruptedException
    {
        return SystemInfo.getProcessor();
    }

    public String getBogoMIPS() throws IOException, InterruptedException
    {
        return SystemInfo.getBogoMIPS();
    }

    public String[] getCpuFeatures() throws IOException, InterruptedException
    {
        return SystemInfo.getCpuFeatures();
    }

    public String getCpuImplementer() throws IOException, InterruptedException
    {
        return SystemInfo.getCpuImplementer();
    }

    public String getCpuArchitecture() throws IOException, InterruptedException
    {
        return SystemInfo.getCpuArchitecture();
    }

    public String getCpuVariant() throws IOException, InterruptedException
    {
        return SystemInfo.getCpuVariant();
    }

    public String getCpuPart() throws IOException, InterruptedException
    {
        return SystemInfo.getCpuPart();
    }

    public String getCpuRevision() throws IOException, InterruptedException
    {
        return SystemInfo.getCpuRevision();
    }

    public String getHardware() throws IOException, InterruptedException
    {
        return SystemInfo.getHardware();
    }

    public String getRevision() throws IOException, InterruptedException
    {
        return SystemInfo.getRevision();
    }

    public String getSerial() throws IOException, InterruptedException
    {
        return SystemInfo.getSerial();
    }   
}
