package com.pi4j.io.serial.impl;

import java.util.Vector;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataListener;

public class SerialDataMonitorThread extends Thread
{
    public static final int DELAY = 100; // milliseconds
    boolean exiting = false;
    Serial serial;
    private Vector<SerialDataListener> listeners;

    public SerialDataMonitorThread(Serial serial, Vector<SerialDataListener> listeners)
    {
        this.serial = serial;
        this.listeners = listeners;
    }

    public synchronized void exit()
    {
        exiting = true;
    }

    // This method is called when the thread runs
    @SuppressWarnings("unchecked")
    public void run()
    {
        StringBuffer buffer = new StringBuffer();

        while (!exiting)
        {
            if (serial.isOpen())
            {
                if (serial.availableBytes() > 0)
                {
                    // reset buffer data
                    buffer.setLength(0);

                    // reset data from serial port
                    while (serial.availableBytes() > 0)
                        buffer.append(serial.read());

                    // when done reading, emit the event if there are any listeners
                    if (listeners.size() > 0)
                    {
                        // create a cloned copy of the listeners
                        Vector<SerialDataListener> dataCopy;
                        dataCopy = (Vector<SerialDataListener>) listeners.clone();

                        // iterate over the listeners and send teh data events
                        for (int i = 0; i < dataCopy.size(); i++)
                        {
                            SerialDataEvent event = new SerialDataEvent(serial, buffer.toString());
                            ((SerialDataListener) dataCopy.elementAt(i)).dataReceived(event);
                        }
                    }
                }
            }

            // wait for a small interval before attempting to read serial data again
            try
            {
                Thread.sleep(DELAY);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
