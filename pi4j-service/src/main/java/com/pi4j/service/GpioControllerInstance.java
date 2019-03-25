package com.pi4j.service;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class GpioControllerInstance {

    /**
     * The GPIO controller.
     */
    private final GpioController gpio;

    /**
     * Constructor
     */
    public GpioControllerInstance() {
        this.gpio = GpioFactory.getInstance();

        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            // This application is running on Windows, so we need to configure Pi4J to run in simulated mode
            // TODO here?
        }
    }

    /**
     * @return The singleton instance of the {@link GpioController}.
     */
    public GpioController getGpioController() {
        return this.gpio;
    }
}
