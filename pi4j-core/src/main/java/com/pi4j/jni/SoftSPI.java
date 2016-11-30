package com.pi4j.jni;

/**
 * This class exposes the call to the SoftSPI read/write function.
 * It takes the pins, speed and clock definitions and writes the
 * values in data, returning the data read from the SPI port in
 * an array with the same dimensions.
 * 
 * @author Tommy « LeChat » Savaria
 */
public class SoftSPI {
	/**
	 * Read/Write on the SPI Port.
	 * @param cs Chip Select Pin
	 * @param clk Clock Pin
	 * @param mosi Master Out Slave In Pin
	 * @param miso Master In Slave Out Pin
	 * @param speed Bus speed (in bits per second)
	 * @param cpol Clock Polarity
	 * @param cpha Clock Phase
	 * @param data Output data
	 * @return Input data
	 */
	public static native byte[] readWrite(int cs, int clk, int mosi, int miso, long speed, boolean cpol, boolean cpha, byte[] data);
}
