package com.pi4j.system;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  NetworkInterface.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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
