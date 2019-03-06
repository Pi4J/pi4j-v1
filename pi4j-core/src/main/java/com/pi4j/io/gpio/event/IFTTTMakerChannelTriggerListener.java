package com.pi4j.io.gpio.event;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  IFTTTMakerChannelTriggerListener.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://www.pi4j.com/
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
public interface IFTTTMakerChannelTriggerListener {

    /**
     * This callback listener is fired when the IFTTTMakerChannelTrigger
     * receives a GPIO pin change that invokes the trigger.  This listener
     * provides the consumer an opportunity to be notified of the trigger event,
     * to optionally abort the triggered event before sending data to the IFTTT
     * Maker Channel API, or an opportunity to override any data payload values
     * before transmitting to the IFTTT Maker Channel API.
     *
     * @param event the IFTTTMakerChannelTriggerEvent event object that
     *              contains details about the GPIO pin, pin state, and
     *              IFTTT Maker Channel event name and data payload.
     * @return Return a 'true' value to continue trasmitting the trigger data to
     *         the IFTTT Maker Channel API.  Return a 'false' value to abort
     *         bypass sending the triggered event to IFTTT.
     */
    public boolean onTriggered(IFTTTMakerChannelTriggerEvent event);
}
