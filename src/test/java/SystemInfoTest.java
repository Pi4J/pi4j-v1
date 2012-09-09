import java.io.IOException;

import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;


public class SystemInfoTest
{

    /**
     * @param args
     * @throws InterruptedException 
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException, InterruptedException
    {
        System.out.println("Serial Number     :  " + SystemInfo.getSerial());
        System.out.println("Processor         :  " + SystemInfo.getProcessor());
        System.out.println("Hardware Revision :  " + SystemInfo.getRevision());
        
        System.out.println("Hostname          :  " + NetworkInfo.getHostname());
        for(String ipAddress : NetworkInfo.getIPAddresses())
            System.out.println("IP Addresses      :  " + ipAddress);
        for(String fqdn : NetworkInfo.getFQDNs())
            System.out.println("FQDN              :  " + fqdn);
        for(String nameserver : NetworkInfo.getNameservers())
            System.out.println("Nameserver        :  " + nameserver);    
    }
}
