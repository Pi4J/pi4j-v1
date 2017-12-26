package com.pi4j.io.gpio;

import java.util.Map;

public class SimulatedGpioProvider extends GpioProviderBase implements GpioProvider {

   // public static final String NAME = "Simulator GPIO Provider";
	
	public static String NAME;
	
	public SimulatedGpioProvider()
	{
		 Map<String, String> env = System.getenv();
		 
		 String config = env.get("SimulatedPlatform");
		 
		 // If no specific platform is specified we default to simulating the raspberry pi.
		 if (config == null)
			 NAME=RaspiGpioProvider.NAME;
		 
//		 try
//		{
//			Class<GpioProvider> providerClass = (Class<GpioProvider>) Class.forName(config);
//			provider
//			
//		}
//		catch (ClassNotFoundException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		 
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
