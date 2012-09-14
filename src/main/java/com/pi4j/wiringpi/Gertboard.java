package com.pi4j.wiringpi;

import com.pi4j.util.NativeLibraryLoader;

/**
 * <h1>WiringPi Gertboard Library</h1>
 * 
 * <p>
 * The Gertboard has an on-board Digital to Analog (DAC) converter and an Analog to Digital (ADC)
 * converters. These are connected via the SPI bus back to the Raspberry Pi host.
 * </p>
 * 
 * <p>
 * Each of the DAC and ADC chips has 2 channels.
 * </p>
 * 
 * <p>
 * The DAC has a resolution of 8 bits and produces an output voltage between 0 and 2.047 volts, the
 * ADC has a resolution of 10 bits and can take an input voltage between 0 and 3.3 volts.
 * </p>
 * 
 * <p>
 * Part of the wiringPi library includes code to setup and drive these chips in an easy to use
 * manner.
 * </p>
 * 
 * <p>
 * To use in a program, first you need to make sure that the 5 SPI jumpers are present on the
 * Gertboard (there are 7 in total, 5 for the SPI, 2 to connect the serial to the ATmega), the
 * photo below shows all 7 jumpers in-place.
 * </p>
 * 
 * <p>
 * Before using the Pi4J library, you need to ensure that the Java VM in configured with access to
 * the following system libraries:
 * <ul>
 * <li>pi4j</li>
 * <li>wiringPi</li>
 * </ul>
 * <blockquote> This library depends on the wiringPi native system library.</br> (developed by
 * Gordon Henderson @ <a href="https://projects.drogon.net/">https://projects.drogon.net/</a>)
 * </blockquote>
 * </p>
 * 
 * @see <a href="http://www.pi4j.com/">http://www.pi4j.com/</a>
 * @see <a
 *      href="https://projects.drogon.net/raspberry-pi/gertboard/analog-inout/">https://projects.drogon.net/raspberry-pi/gertboard/analog-inout/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class Gertboard
{
    public static final int SPI_ADC_SPEED = 1000000;
    public static final int SPI_DAC_SPEED = 1000000;
    public static final int SPI_A2D = 0;
    public static final int SPI_D2A = 1;

    static
    {
        // Load the platform library
        NativeLibraryLoader.load("pi4j", "libpi4j.so");
    }

    /**
     * <h1>int gertboardAnalogWrite (int channel, int value)</h1>
     * 
     * <p>
     * This outputs the supplied value (0-255) to the given channel (0 or 1). The output voltage is:
     * </p>
     * 
     * <pre>
     * vOut = value / 255 * 2.047
     * </pre>
     * 
     * <p>
     * or to find the value for a given voltage:
     * </p>
     * 
     * <pre>
     * value = vOut / 2.047 * 255
     * </pre>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/gertboard/analog-inout/">https://projects.drogon.net/raspberry-pi/gertboard/analog-inout/</a>
     * @param chan <p>
     *            Analog channel to write to (0 or 1).
     *            </p>
     * @param value <p>
     *            The output value (0-255) supplied to the given channel (0 or 1).
     *            </p>
     */
    public static native void gertboardAnalogWrite(int chan, int value);

    /**
     * <h1>int gertboardAnalogRead (int channel)</h1>
     * 
     * <p>
     * This returns a value from 0 to 1023 representing the value on the supplied channel (0 or 1).
     * To convert this to a voltage, use the following formula:
     * </p>
     * 
     * <pre>
     * vIn = value * 3.3 / 1023
     * </pre>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/gertboard/analog-inout/">https://projects.drogon.net/raspberry-pi/gertboard/analog-inout/</a>
     * @param chan <p>
     *            Analog channel to read from (0 or 1).
     *            </p>
     * @return <p>
     *         This returns a value from 0 to 1023 representing the value on the supplied channel (0
     *         or 1).
     *         </p>
     */
    public static native int gertboardAnalogRead(int chan);

    /**
     * <h1>int gertboardSPISetup (void)</h1>
     * 
     * <p>
     * This must be called to initialize the SPI bus to communicate with the Gertboards ADC and DAC
     * chips. If the return value is < 0 then an error occurred and errno will be set appropriately.
     * </p>
     * 
     * @see <a
     *      href="https://projects.drogon.net/raspberry-pi/gertboard/analog-inout/">https://projects.drogon.net/raspberry-pi/gertboard/analog-inout/</a>
     * @return <p>
     *         If the return value is < 0 then an error occurred and errno will be set
     *         appropriately. If the return value is '0' or greater than the call was successful.
     *         </p>
     */
    public static native int gertboardSPISetup();
}
