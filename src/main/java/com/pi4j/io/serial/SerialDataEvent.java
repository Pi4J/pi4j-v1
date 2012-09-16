package com.pi4j.io.serial;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library
 * FILENAME      :  SerialDataEvent.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 Pi4J
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


import java.util.EventObject;

/**
 * <h1>Serial Data Event</h1>
 * 
 * <p>
 * This class provides the serial data event object.
 * </p>
 * 
 * <p>
 * Before using the Pi4J library, you need to ensure that the Java VM in configured with access to
 * the following system libraries:
 * <ul>
 * <li>pi4j</li>
 * <li>wiringPi</li>
 * </ul>
 * <blockquote> This library depends on the wiringPi native system library.</br> (developed by
 * Gordon Henderson @ <a href="https://projects.drogon.net/">https://projects.drogon.net/</a>)
 * </blockquote>
 * </p>
 * 
 * @see #Serial
 * @see #SerialDataListener
 * @see #SerialFactory
 * 
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class SerialDataEvent extends EventObject
{
    private static final long serialVersionUID = 1L;
    private String data;

    /**
     * <h1>Default event constructor</h1>
     * 
     * @param obj <p>
     *            The com.pi4j.io.serial.Serial class instance that initiates this event.
     *            </p>
     * @param data <p>
     *            The data received.
     *            </p>
     */
    public SerialDataEvent(Object obj, String data)
    {
        super(obj);
        this.data = data;
    }

    /**
     * <p>
     * Get the data string received.
     * </p>
     * 
     * @return <p>
     *         The data string received.
     *         </p>
     */
    public String getData()
    {
        return data;
    }
}
