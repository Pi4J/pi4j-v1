import java.util.concurrent.Callable;

import com.pi4j.io.gpio.Gpio;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDirection;
import com.pi4j.io.gpio.GpioPinEdge;
import com.pi4j.io.gpio.GpioPinResistor;
import com.pi4j.io.gpio.GpioPinState;
import com.pi4j.io.gpio.event.GpioListener;
import com.pi4j.io.gpio.event.GpioPinStateChangeEvent;
import com.pi4j.io.gpio.trigger.GpioCallbackTrigger;
import com.pi4j.io.gpio.trigger.GpioPulseStateTrigger;
import com.pi4j.io.gpio.trigger.GpioSetStateTrigger;
import com.pi4j.io.gpio.trigger.GpioSyncStateTrigger;

public class GpioTest
{
    public static void main(String args[]) throws InterruptedException
    {
        System.out.println("<--Pi4J--> GPIO test program");
        
        // create gpio listener & callback handler
        GpioTestListener listener = new GpioTestListener();

        // create gpio trigger
        GpioSetStateTrigger triggerOn = new GpioSetStateTrigger(GpioPin.GPIO_04, GpioPinState.HIGH, GpioPin.GPIO_25, GpioPinState.HIGH);
        GpioSetStateTrigger triggerOff = new GpioSetStateTrigger(GpioPin.GPIO_04, GpioPinState.LOW, GpioPin.GPIO_25, GpioPinState.LOW);
        GpioSyncStateTrigger triggerSync = new GpioSyncStateTrigger(GpioPin.GPIO_04, GpioPinState.allStates(), GpioPin.GPIO_24);
        GpioPulseStateTrigger triggerPulse = new GpioPulseStateTrigger(GpioPin.GPIO_04, GpioPinState.HIGH, GpioPin.GPIO_23, 1000);

        GpioCallbackTrigger triggerCallback = new GpioCallbackTrigger(GpioPin.GPIO_04, GpioPinState.allStates(), 
              new Callable<Void>()
            {
                public Void call() throws Exception
                {
                    System.out.println(" --> GPIO CALLBACK RECEIVED ");
                    return null;
                }
            });
        
        
        // create gpio controller
        Gpio gpio = GpioFactory.createInstance();
        
        // add gpio listener
        gpio.addListener(listener);
        
        // add gpio triggers
        gpio.addTrigger(triggerOn);
        gpio.addTrigger(triggerOff);
        gpio.addTrigger(triggerSync);
        gpio.addTrigger(triggerCallback);
        gpio.addTrigger(triggerPulse);
        
        // setup gpio pin #4 as an input pin whose biased to ground and receives +3VDC to be triggered
        gpio.setup(GpioPin.GPIO_04, GpioPinDirection.IN, GpioPinEdge.BOTH, GpioPinResistor.PULL_DOWN);

        // setup gpio pin #21 as an output pin  
        gpio.setup(GpioPin.GPIO_21, GpioPinDirection.OUT);
        gpio.setState(GpioPin.GPIO_21, GpioPinState.HIGH);

        // setup gpio pin #22 as an output pin  
        gpio.setup(GpioPin.GPIO_22, GpioPinDirection.OUT);
        gpio.setState(GpioPin.GPIO_22, GpioPinState.LOW);

        // setup gpio pin #23 as an output pin  
        gpio.setup(GpioPin.GPIO_23, GpioPinDirection.OUT);
        gpio.pulse(GpioPin.GPIO_23, 1000);
        
        Thread.sleep(5000);
        
        // toggle gpio pin states
        gpio.setState(GpioPin.GPIO_21, GpioPinState.LOW);
        gpio.setState(GpioPin.GPIO_22, GpioPinState.HIGH);
        
        // keep program alive
        for(;;)
        {
            Thread.sleep(500);
        }
    }}

class GpioTestListener implements GpioListener
{
    public void pinStateChanged(GpioPinStateChangeEvent event)
    {
        System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = "
                + event.getState());
    }
}
