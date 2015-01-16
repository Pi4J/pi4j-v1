package com.pi4j.system;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  NetworkInterface.java  
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
@SuppressWarnings("unused")
public class NetworkInterface {

    private final String linkEncap;
    private final String ipAddress;
    private final String macAddress;
    private final String broadcastAddress;
    private final String subnetMask;
    private final String mtu;
    private final String metric;
    
    public NetworkInterface(String linkEncap,String macAddress,String ipAddress,String broadcastAddress,String subnetMask,String mtu,String metric) {
        this.linkEncap = linkEncap;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.broadcastAddress = broadcastAddress;
        this.subnetMask = subnetMask;
        this.mtu = mtu;
        this.metric = metric;        
    }
    
    public String getLinkEncap() {
        return linkEncap;
    }
 
    public String getIPAddress() {
        return ipAddress;
    }

    public String getMACAddress() {
        return macAddress;
    }
    
    public String getBroadcastAddress() {
        return broadcastAddress;
    }
    
    public String getSubnetMask() {
        return subnetMask;
    }
    
    public String getMTU() {
        return mtu;
    }
    
    public String getMetric() {
        return metric;
    }
}
