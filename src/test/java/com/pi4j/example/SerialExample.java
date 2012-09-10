// START SNIPPET: serial-snippet
package com.pi4j.example;

import java.util.Date;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataListener;
import com.pi4j.io.serial.SerialFactory;

/**
 * This example code demonstrates how to perform serial communications using the Raspberry Pi.
 * 
 * @author Robert Savage
 */
public class SerialExample
{
    public static void main(String args[]) throws InterruptedException
    {
        // create serial data listener
        SerialExampleListener listener = new SerialExampleListener();

        // create an instance of the serial communications class
        Serial serial = SerialFactory.createInstance();

        // add/register the serial data listener
        serial.addListener(listener);

        // open the default serial port provided on the GPIO header
        int ret = serial.open(Serial.DEFAULT_COM_PORT, 38400);
        if (ret == -1)
        {
            System.out.println(" ==>> SERIAL SETUP FAILED");
            return;
        }

        // continuous loop to keep the program running until the user terminates the program
        for (;;)
        {
            // write a formatted string to the serial transmit buffer
            serial.write("CURRENT TIME: %s", new Date().toString());

            // write a individual bytes to the serial transmit buffer
            serial.write((byte) 13);
            serial.write((byte) 10);

            // write a simple string to the serial transmit buffer
            serial.write("Second Line");

            // write a individual characters to the serial transmit buffer
            serial.write('\r');
            serial.write('\n');

            // write a string terminating with CR+LF to the serial transmit buffer
            serial.writeln("Third Line");

            // wait 1 second before continuing
            Thread.sleep(1000);
        }
    }

}

/**
 * This class implements the serial data listener interface with the callback method for event
 * notifications when data is received on the serial port.
 * 
 * @see SerialDataListener
 * @author Robert Savage
 */
class SerialExampleListener implements SerialDataListener
{
    public void dataReceived(SerialDataEvent event)
    {
        // print out the data received to the console
        System.out.print(event.getData());
    }
}

// END SNIPPET: serial-snippet