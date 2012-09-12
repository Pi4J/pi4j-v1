import java.util.concurrent.Callable;

import com.pi4j.io.gpio.Gpio;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinEdge;
import com.pi4j.io.gpio.PinResistor;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioListener;
import com.pi4j.io.gpio.event.GpioPinStateChangeEvent;
import com.pi4j.io.gpio.trigger.GpioCallbackTrigger;
import com.pi4j.io.gpio.trigger.GpioInverseSyncStateTrigger;
import com.pi4j.io.gpio.trigger.GpioPulseStateTrigger;
import com.pi4j.io.gpio.trigger.GpioSetStateTrigger;

public class GpioTest
{
    public static void main(String args[]) throws InterruptedException
    {
        System.out.println("<--Pi4J--> GPIO test program");

        // create gpio listener & callback handler
        GpioTestListener listener = new GpioTestListener();

        // create gpio controller
        Gpio gpio = GpioFactory.createInstance();

        // setup gpio pin #4 as an input pin whose biased to ground and receives +3VDC to be
        // triggered
        GpioPin inputPin = gpio.provisionInputPin(Pin.GPIO_04, "Test-Input", PinEdge.BOTH,
                                                  PinResistor.PULL_DOWN);

        // add gpio listener
        inputPin.addListener(listener);

        // setup gpio pin #21, 22, 23 as output pins
        GpioPin pinLed1 = gpio.provisionOuputPin(Pin.GPIO_21, "LED-One", PinState.LOW);
        GpioPin pinLed2 = gpio.provisionOuputPin(Pin.GPIO_22, "LED-Two", PinState.HIGH);
        GpioPin pinLed3 = gpio.provisionOuputPin(Pin.GPIO_23, "LED-Three", PinState.LOW);

        // create gpio triggers
        inputPin.addTrigger(new GpioSetStateTrigger(PinState.HIGH, pinLed1, PinState.HIGH));
        inputPin.addTrigger(new GpioSetStateTrigger(PinState.LOW, pinLed1, PinState.LOW));
        inputPin.addTrigger(new GpioInverseSyncStateTrigger(pinLed2));
        inputPin.addTrigger(new GpioPulseStateTrigger(PinState.HIGH, pinLed3, 1000));
        inputPin.addTrigger(new GpioCallbackTrigger(PinState.allStates(), new Callable<Void>()
        {
            public Void call() throws Exception
            {
                System.out.println(" --> GPIO TRIGGER CALLBACK RECEIVED ");
                return null;
            }
        }));

        // keep program alive
        for (;;)
        {
            Thread.sleep(500);
        }
    }
}

class GpioTestListener implements GpioListener
{
    public void pinStateChanged(GpioPinStateChangeEvent event)
    {
        System.out.println(" --> GPIO PIN LISTENER STATE CHANGE: " + event.getPin() + " = "
                + event.getState());
    }
}
