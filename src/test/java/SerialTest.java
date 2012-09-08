import java.util.Date;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataListener;
import com.pi4j.io.serial.SerialFactory;

public class SerialTest 
{
    
    public static void main(String args[]) throws InterruptedException
    {
        SerialTestListener listener = new SerialTestListener();
        Serial serial = SerialFactory.createInstance();
        serial.addListener(listener);
        
        System.out.println("<--Pi4J--> SERIAL test program");

        int fd = serial.open(Serial.DEFAULT_COM_PORT, 38400);
        if (fd == -1)
        {
            System.out.println(" ==>> SERIAL SETUP FAILED");
            return;
        }
        
        while(true)
        {
            serial.write("CURRENT TIME: %s", new Date().toString());
            serial.write((byte)13);
            serial.write((byte)10);
            serial.write('|');
            serial.write((byte)13);
            serial.write((byte)10);

            Thread.sleep(1000);
            serial.removeListener(listener);
        }
    }


}

class SerialTestListener implements SerialDataListener
{
    public void dataReceived(SerialDataEvent event)
    {
        // print out the data received to the console 
        System.out.print(event.getData());        
    }
}
