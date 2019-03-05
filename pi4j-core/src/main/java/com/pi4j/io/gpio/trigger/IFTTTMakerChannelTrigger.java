package com.pi4j.io.gpio.trigger;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  IFTTTMakerChannelTrigger.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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


import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.IFTTTMakerChannelTriggerEvent;
import com.pi4j.io.gpio.event.IFTTTMakerChannelTriggerListener;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SuppressWarnings("unused")
public class IFTTTMakerChannelTrigger extends GpioTriggerBase {

    private final String apikey;
    private final String eventName;
    private IFTTTMakerChannelTriggerListener listener = null;

    protected static final String IFTTT_MAKER_CHANNEL_URL = "https://maker.ifttt.com/trigger/%s/with/key/%s";

    public IFTTTMakerChannelTrigger(String apikey, String eventName) {
        super();
        this.apikey = apikey;
        this.eventName = eventName;
    }

    public IFTTTMakerChannelTrigger(String apikey, String eventName, PinState state) {
        super(state);
        this.apikey = apikey;
        this.eventName = eventName;
    }

    public IFTTTMakerChannelTrigger(String apikey, String eventName, PinState[] states) {
        super(states);
        this.apikey = apikey;
        this.eventName = eventName;
    }

    public IFTTTMakerChannelTrigger(String apikey, String eventName, List<PinState> states) {
        super(states);
        this.apikey = apikey;
        this.eventName = eventName;
    }

    public IFTTTMakerChannelTrigger(String apikey, String eventName, IFTTTMakerChannelTriggerListener listener) {
        super();
        this.apikey = apikey;
        this.eventName = eventName;
        this.listener = listener;
    }

    public IFTTTMakerChannelTrigger(String apikey, String eventName, PinState state, IFTTTMakerChannelTriggerListener listener) {
        super(state);
        this.apikey = apikey;
        this.eventName = eventName;
        this.listener = listener;
    }

    public IFTTTMakerChannelTrigger(String apikey, String eventName, PinState[] states, IFTTTMakerChannelTriggerListener listener) {
        super(states);
        this.apikey = apikey;
        this.eventName = eventName;
        this.listener = listener;
    }

    public IFTTTMakerChannelTrigger(String apikey, String eventName, List<PinState> states, IFTTTMakerChannelTriggerListener listener) {
        super(states);
        this.apikey = apikey;
        this.eventName = eventName;
        this.listener = listener;
    }

    /**
     * Use this method to register/assign a custom trigger listener.
     *
     * This callback listener is fired when the IFTTTMakerChannelTrigger
     * receives a GPIO pin change that invokes the trigger.  This listener
     * provides the consumer an opportunity to be notified of the trigger event,
     * to optionally abort the triggered event before sending data to the IFTTT
     * Maker Channel API, or an opportunity to override any data payload values
     * before transmitting to the IFTTT Maker Channel API.
     *
     * @param listener
     */
    public void setTriggerListener(IFTTTMakerChannelTriggerListener listener){
        this.listener = listener;
    }

    @Override
    public void invoke(GpioPin pin, PinState state) {
        try {
            // create an ISO-8601 complaint date format for timestamp
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

            // create the default JSON payload that will get supplied as the default
            // data for 'value3' in the IFTTT Maker Channel trigger API
            String json = "{\"pin\":{\"name\":\"" + pin.getName() +
                    "\",\"address\":\"" + pin.getPin().getAddress() +
                    "\",\"provider\":\"" + pin.getPin().getProvider() +
                    "\",\"mode\":\"" + pin.getMode().getName() +
                    "\",\"direction\":\"" + pin.getMode().getDirection() +
                    "\",\"pull\":\"" + pin.getPullResistance().getName() +
                    "\"},\"state\":{" +
                    "\"name\":\"" + state.getName() +
                    "\",\"value\":\"" + state.getValue() +
                    "\",\"is-high\":\"" + state.isHigh() +
                    "\",\"is-low\":\"" + state.isLow() +
                    "\"},\"timestamp\":\"" + df.format(new Date()) + "\"}";

            // create an IFTTT Maker Channel event
            IFTTTMakerChannelTriggerEvent event = new IFTTTMakerChannelTriggerEvent(
                    this,                               // IFTTTMakerChannelTrigger instance
                    pin,                                // GPIO PIN instance
                    state,                              // GPIO PIN STATE
                    eventName,                          // IFTTT EVENT NAME
                    pin.getName(),                      // VALUE 1
                    Integer.toString(state.getValue()), // VALUE 2
                    json);                              // VALUE 3

            // if the consumer configured a custom listener for callback events,
            // then we need to invoke the listner instance 'onTriggered' callback
            // method with the IFTTTMakerChannelTriggerEvent instance.
            if(this.listener != null){
                if(this.listener.onTriggered(event) == false) {
                    // abort
                    return;
                }
            }

            // we must URL encode the IFTTT payload data values (value1, value2, value3)
            String value1 = URLEncoder.encode(event.getValue1(), StandardCharsets.UTF_8.name());
            String value2 = URLEncoder.encode(event.getValue2(), StandardCharsets.UTF_8.name());
            String value3 = URLEncoder.encode(event.getValue3(), StandardCharsets.UTF_8.name());

            // create the URL parameters and post data
            String urlParameters  = "value1=" + value1 + "&value2=" + value2 + "&value3=" + value3;
            byte[] postData       = urlParameters.getBytes(StandardCharsets.UTF_8);

            // create the IFTTT Maker Channel trigger URL & connection
            URL url = new URL(String.format(IFTTT_MAKER_CHANNEL_URL, eventName, apikey));
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length", Integer.toString(postData.length));
            connection.setUseCaches(false);
            try( DataOutputStream stream = new DataOutputStream(connection.getOutputStream())) {
                stream.write( postData );
            }

            // get HTTP response
            int responseCode = connection.getResponseCode();

            // if the HTTP post resulted in an error, then raise an exception
            if(responseCode != 200) {
                System.err.println("IFTTT MakerChannel ERROR Response Code: " + responseCode);
                BufferedReader in = new BufferedReader( new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                throw new RuntimeException(response.toString());
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
