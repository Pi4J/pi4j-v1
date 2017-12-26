package com.pi4j.io.gpio;

import java.util.Map;


/**
 * A simulator to aid in development of RI Pi systems using a standard PC dev environment.
 * 
 * To use the simulator you need two environment variables:
 * 
 * The standard PI4J platform statement MUST point to the simulator:
 * 
 * PI4J_PLATFORM=Simulated
 * 
 * A second environment variable that defines the platform that is to be simulated.
 * 
 * SimulatedPlatform=<Real Platform's Name>
 * 
 * e.g.
 * 
 * SimultatedPlatform=RaspberryPi GPIO Provider
 * 
 * If you don't provide a value for theSimulatedPlatform the system assumes that you want to use
 * the raspberry pi platform: RaspiGpioProvider
 * 
 * @author bsutton
 *
 */
public class SimulatedGpioProvider extends GpioProviderBase implements GpioProvider {
	
	
	

	// We use the name of the platform that we are simulating.
    public static  String NAME;
	
	public SimulatedGpioProvider()
	{
		 Map<String, String> env = System.getenv();
		 
		 String config = env.get("SimulatedPlatform");
		 
		 // If no specific platform is specified we default to simulating the raspberry pi.
		 if (config == null)
			 NAME=RaspiGpioProvider.NAME;
	 
		 NAME = config;
	}

    @Override
    public String getName() {
    	
        return NAME;
    }

    public void setState(Pin pin, PinState state) {
        // cache pin state
        getPinCache(pin).setState(state);

        // dispatch event
        dispatchPinDigitalStateChangeEvent(pin, state);
    }

    public void setAnalogValue(Pin pin, double value) {
        // cache pin state
        getPinCache(pin).setAnalogValue(value);

        // dispatch event
        dispatchPinAnalogValueChangeEvent(pin, value);
    }

}
