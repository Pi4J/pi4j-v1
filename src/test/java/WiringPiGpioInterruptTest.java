import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioInterrupt;
import com.pi4j.wiringpi.GpioInterruptListener;
import com.pi4j.wiringpi.GpioInterruptEvent;

public class WiringPiGpioInterruptTest
{
    public static void main(String args[]) throws InterruptedException
    {
        System.out.println("<--Pi4J--> GPIO INTERRUPT test program");
        
        // create test listener and add as a GPIO listener
        GpioTestInterruptListener lsnr = new GpioTestInterruptListener();
        GpioInterrupt.addListener(lsnr);

        // setup wiring pi
        if (Gpio.wiringPiSetupGpio() == -1)
        {
            System.out.println(" ==>> GPIO SETUP FAILED");
            return;
        }

        // configure GPIO 14 as an OUTPUT; 
        Gpio.pinMode(25, Gpio.OUTPUT);
        Gpio.pinMode(18, Gpio.OUTPUT);
        
        // configure GPIO 4 as an INPUT pin; enable it for callbacks
        Gpio.pinMode(4, Gpio.INPUT);
        Gpio.pullUpDnControl(4, Gpio.PUD_DOWN);        
        GpioInterrupt.enablePinStateChangeCallback(4);

        // configure GPIO 17 as an INPUT pin; enable it for callbacks
        Gpio.pinMode(17, Gpio.INPUT);
        Gpio.pullUpDnControl(17, Gpio.PUD_DOWN);        
        GpioInterrupt.enablePinStateChangeCallback(17);

        // continuously loop to prevent program from exiting
        for (;;)
        {
            Thread.sleep(5000);
        }
    }
}

class GpioTestInterruptListener implements GpioInterruptListener
{
    public void pinStateChange(GpioInterruptEvent event)
    {
        System.out.println("Raspberry Pi PIN [" + event.getPin() +"] is in STATE [" + event.getState() + "]");
        
        if(event.getPin() == 4)
        {
            Gpio.digitalWrite(25, event.getStateValue());
        }
        if(event.getPin() == 17)
        {
            Gpio.digitalWrite(18, event.getStateValue());
        }
    }
}
