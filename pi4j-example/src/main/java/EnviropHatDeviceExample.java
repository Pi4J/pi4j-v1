import com.pi4j.device.envirophat.EnviropHat;
import com.pi4j.device.envirophat.EnviropHatException;
import com.pi4j.device.envirophat.EnviropHatTempType;
import com.pi4j.device.envirophat.impl.EnviropHatDevice;

/**
 * This example code demonstrates how to use Enviro pHat
 * on the Raspberry Pi.
 *
 * @author Alex Akunevich
 */
public class EnviropHatDeviceExample {


    public static void main(String[] args) throws EnviropHatException {
        EnviropHat hat = new EnviropHatDevice();

        double tempC = hat.temperature(EnviropHatTempType.CELSIUS);
        System.out.println("Temperature in Celsius: " + tempC);

        double tempF = hat.temperature(EnviropHatTempType.FAHRENHEIT);
        System.out.println("Temperature in Fahrenheit: " + tempF);
    }
}
