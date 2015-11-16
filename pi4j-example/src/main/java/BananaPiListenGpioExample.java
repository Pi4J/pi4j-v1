import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * This example code demonstrates how to setup a listener
 * for GPIO pin state changes on the BananaPi.
 *
 * The internal resistance is set to PULL UP. So when
 * connecting GPIO pin #2 to a ground pin, you should
 * see the GpioPinListenerDigital fire the event.
 *
 * @author Robert Savage
 */
public class BananaPiListenGpioExample {

    public static void main(String args[]) throws InterruptedException {
        System.out.println("<--Pi4J--> GPIO Listen Example ... started.");

        // ####################################################################
        //
        // since we are not using the default Raspberry Pi platform, we should
        // assign the default provider as the BananaPi provider.
        //
        // ####################################################################
        GpioFactory.setDefaultProvider(new BananaPiGpioProvider());

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // ####################################################################
        //
        // When provisioning a pin, use the BananaPiPin class
        //
        // ####################################################################

        // provision gpio pin #02 as an input pin with its internal pull up resistor enabled
        final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(BananaPiPin.GPIO_02, PinPullResistance.PULL_UP);

        // create and register gpio pin listener
        myButton.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                // display pin state on console
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
            }

        });

        System.out.println(" ... complete the GPIO #02 circuit and see the listener feedback here in the console.");

        // keep program running until user aborts (CTRL-C)
        while(true) {
            Thread.sleep(500);
        }

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        // gpio.shutdown();   <--- implement this method call if you wish to terminate the Pi4J GPIO controller
    }
}
