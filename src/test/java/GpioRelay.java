import com.pi4j.io.gpio.event.GpioListener;
import com.pi4j.io.gpio.event.GpioPinStateChangeEvent;

public class GpioRelay
{
    public static void main(String args[]) throws InterruptedException
    {
        System.out.println("<--Pi4J--> GPIO test program");
        
        // create gpio listener & callback handler
//      GpioRelayListener listener = new GpioRelayListener();

//        // create gpio trigger
//        GpioSetStateTrigger triggerOn = new GpioSetStateTrigger(Pin.GPIO_04, PinState.HIGH, Pin.GPIO_25, PinState.LOW);
//        GpioSetStateTrigger triggerOff = new GpioSetStateTrigger(Pin.GPIO_04, PinState.LOW, Pin.GPIO_25, PinState.HIGH);
//
//        // create gpio controller
//        Gpio gpio = GpioFactory.createInstance();
//        
//        // add gpio listener
//        gpio.addListener(listener);
//        
//        // add gpio triggers
//        gpio.addTrigger(triggerOn);
//        gpio.addTrigger(triggerOff);
//        
//        // setup gpio pin #4 as an input pin whose biased to ground and receives +3VDC to be triggered
//        gpio.setup(Pin.GPIO_04, PinDirection.IN, PinEdge.BOTH, PinResistor.PULL_DOWN);
//
//        // setup gpio pin #25 as an output pin  
//        gpio.setup(Pin.GPIO_25, PinDirection.OUT);
//        //gpio.setPullResistor(GpioPin.GPIO_25, GpioPinResistor.PULL_DOWN);
//        gpio.setState(Pin.GPIO_25, PinState.HIGH);

        // keep program alive
        for(;;)
        {
            Thread.sleep(500);
        }
    }}

class GpioRelayListener implements GpioListener
{
    public void pinStateChanged(GpioPinStateChangeEvent event)
    {
        System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = "
                + event.getState());
    }
}
