/*
 * **********************************************************************
 * This file is part of the pi4j project: http://www.pi4j.com/
 * 
 * pi4j is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * pi4j is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with pi4j. If not,
 * see <http://www.gnu.org/licenses/>.
 * **********************************************************************
 */
package com.pi4j.io.serial;

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
