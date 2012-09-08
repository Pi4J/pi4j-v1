import com.pi4j.wiringpi.Gpio;

public class WiringPiGpioTest
{
    // Simple sequencer data
    // Triplets of LED, On/Off and delay

    private static final int data[] =
    { 0, 1, 1, 1, 1, 1, 0, 0, 0, 2, 1, 1, 1, 0, 0, 3, 1, 1, 2, 0, 0, 4, 1, 1, 3, 0, 0, 5, 1, 1, 4,
            0, 0, 6, 1, 1, 5, 0, 0, 7, 1, 1, 6, 0, 1, 7, 0, 1,

            0, 0,
            1, // Extra delay

            // Back again

            7, 1, 1, 6, 1, 1, 7, 0, 0, 5, 1, 1, 6, 0, 0, 4, 1, 1, 5, 0, 0, 3, 1, 1, 4, 0, 0, 2, 1,
            1, 3, 0, 0, 1, 1, 1, 2, 0, 0, 0, 1, 1, 1, 0, 1, 0, 0, 1,

            0, 0, 1, // Extra delay

            9, 9, 9, // End marker

    };

    public static void main(String args[]) throws InterruptedException
    {
        int pin;
        int dataPtr;
        int l, s, d;
        
        System.out.println("<--Pi4J--> GPIO test program");

        // setup wiringPi
        if (Gpio.wiringPiSetup() == -1)
        {
            System.out.println(" ==>> GPIO SETUP FAILED");
            return;
        }

        // set GPIO 4 as the input trigger 
        Gpio.pinMode (7, Gpio.INPUT) ;  
        Gpio.pullUpDnControl(7, Gpio.PUD_DOWN);        

        // set all other GPIO as outputs
        for (pin = 0; pin < 7; ++pin)
            Gpio.pinMode(pin, Gpio.OUTPUT);
        
        dataPtr = 0;
        for (;;)
        {
            l = data[dataPtr++]; // LED
            s = data[dataPtr++]; // State
            d = data[dataPtr++]; // Duration (10ths)

            if ((l + s + d) == 27)
            {
                dataPtr = 0;
                continue;
            }

            Gpio.digitalWrite(l, s);
            
            if (Gpio.digitalRead(7) == 1) // Pressed as our switch shorts to ground
                Gpio.delay(d * 10); // Faster!
            else
                Gpio.delay(d * 100);
        }
    }
}

