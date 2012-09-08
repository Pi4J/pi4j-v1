import com.pi4j.wiringpi.Serial;



public class WiringPiSerialTest
{
    
    public static void main(String args[]) throws InterruptedException
    {
        System.out.println("<--Pi4J--> SERIAL test program");

        int fd = Serial.serialOpen(Serial.DEFAULT_COM_PORT, 38400);
        if (fd == -1)
        {
            System.out.println(" ==>> SERIAL SETUP FAILED");
            return;
        }
        
        while(true)
        {
            Serial.serialPuts(fd, "TEST\r\n");

            int dataavail = Serial.serialDataAvail(fd);
            
            while(dataavail > 0)
            {
                int data = Serial.serialGetchar(fd);
                System.out.print((char)data);                
                dataavail = Serial.serialDataAvail(fd);
            }
            
            Thread.sleep(1000);
        }
    }
}
