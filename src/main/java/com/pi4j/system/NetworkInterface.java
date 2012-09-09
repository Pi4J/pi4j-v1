package com.pi4j.system;

public class NetworkInterface
{
    private String linkEncap;
    private String ipAddress;
    private String macAddress;
    private String broadcastAddress;
    private String subnetMask;
    private String mtu;
    private String metric;
    
    public NetworkInterface(String linkEncap,String macAddress,String ipAddress,String broadcastAddress,String subnetMask,String mtu,String metric)
    {
        this.linkEncap = linkEncap;
        this.ipAddress = ipAddress;
        this.broadcastAddress = broadcastAddress;
        this.subnetMask = subnetMask;
        this.mtu = mtu;
        this.metric = metric;        
    }
    
    public String getLinkEncap()
    {
        return linkEncap;
    }
 
    public String getIPAddress()
    {
        return ipAddress;
    }

    public String getMACAddress()
    {
        return macAddress;
    }
    
    public String getBroadcastAddress()
    {
        return broadcastAddress;
    }
    
    public String getSubnetMask()
    {
        return subnetMask;
    }
    
    public String getMTU()
    {
        return mtu;
    }
    
    public String getMetric()
    {
        return metric;
    }
}
