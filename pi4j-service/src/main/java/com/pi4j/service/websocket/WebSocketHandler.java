package com.pi4j.service.websocket;

/*-
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java remote services (REST + WebSockets)
 * FILENAME      :  WebSocketHandler.java
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
import com.pi4j.service.AppConfig;
import com.pi4j.service.GpioControllerInstance;
import java.util.Collection;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    WebSocketHandler() {
        System.out.println("WS handler initialized");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("WS connected");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("WS closed: " + status);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void handleTransportError(WebSocketSession session, Throwable ex) {
        System.out.println("WS error: " + ex.getMessage());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println("WS message: " + message);

        try {
            session.sendMessage(new TextMessage("ECHO " + message));
        } catch (Exception ex) {
            System.err.println("Error while handling text message from websocket: " + ex.getMessage());
        }
    }

    /**
     * Get the current state of the pins.
     *
     * @return
     */
    /*@MessageMapping("/ws/pins/states")
    @SendTo("/topic/pins/states")
    public Collection<GpioPin> getStates() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        return context.getBean(GpioControllerInstance.class).getGpioController().getProvisionedPins();
    }*/
}


