package com.pi4j.temperature;

public class TemperatureConversion
{
    public static final double ABSOLUTE_ZERO_CELSIUS = -273.15;
    public static final double ABSOLUTE_ZERO_FARENHEIT = -459.67;
    public static final double ABSOLUTE_ZERO_KELVIN = 0;
    public static final double ABSOLUTE_ZERO_RANKINE = 0;
    
    /**
     * Convert a temperature value from one temperature scale to another.
     * 
     * @param from TemperatureScale
     * @param to TemperatureScale
     * @param temperature value
     * @return converted temperature value in the requested to scale
     */
    public static double convert(TemperatureScale from, TemperatureScale to, double temperature) {

        switch(from) {
        
            case FARENHEIT:
                return convertFromFarenheit(to, temperature);
            case CELSIUS:
                return convertFromCelsius(to, temperature);
            case KELVIN:
                return convertFromKelvin(to, temperature);
            case RANKINE:
                return convertFromRankine(to, temperature);
            default:
                throw(new RuntimeException("Invalid termpature conversion"));
        }
    }
    

    /**
     * Convert a temperature value from the Farenheit temperature scale to another.
     * 
     * @param to TemperatureScale
     * @param temperature value in degrees Farenheit
     * @return converted temperature value in the requested to scale
     */
    public static double convertFromFarenheit (TemperatureScale to, double temperature) {
        
        switch(to) {
        
            case FARENHEIT:
                return temperature;
            case CELSIUS:
                return convertFarenheitToCelsius(temperature);
            case KELVIN:
                return convertFarenheitToKelvin(temperature);
            case RANKINE:
                return convertFarenheitToRankine(temperature);
            default:
                throw(new RuntimeException("Invalid termpature conversion"));
        }
    }

    /**
     * Convert a temperature value from another temperature scale into the Farenheit temperature scale.
     * 
     * @param to TemperatureScale
     * @param temperature value from other scale 
     * @return converted temperature value in degrees Farenheit
     */
    public static double convertToFarenheit (TemperatureScale from, double temperature) {
        
        switch(from) {
        
            case FARENHEIT:
                return temperature;
            case CELSIUS:
                return convertCelsiusToFarenheit(temperature);
            case KELVIN:
                return convertKelvinToFarenheit(temperature);
            case RANKINE:
                return convertRankineToFarenheit(temperature);
            default:
                throw(new RuntimeException("Invalid termpature conversion"));
        }
    }
    
    /**
     * Convert a temperature value from the Celsius temperature scale to another.
     * 
     * @param to TemperatureScale
     * @param temperature value in degrees centigrade
     * @return converted temperature value in the requested to scale
     */    
    public static double convertFromCelsius(TemperatureScale to, double temperature) {
                
        switch(to) {
        
            case FARENHEIT:
                return convertCelsiusToFarenheit(temperature);
            case CELSIUS:
                return temperature;
            case KELVIN:
                return convertCelsiusToKelvin(temperature);
            case RANKINE:
                return convertCelsiusToRankine(temperature);
            default:
                throw(new RuntimeException("Invalid termpature conversion"));
        }
    }
    
    /**
     * Convert a temperature value from another temperature scale into the Celsius temperature scale.
     * 
     * @param to TemperatureScale
     * @param temperature value from other scale 
     * @return converted temperature value in degrees centigrade
     */
    public static double convertToCelsius (TemperatureScale from, double temperature) {
        
        switch(from) {
        
            case FARENHEIT:
                return convertFarenheitToCelsius(temperature);
            case CELSIUS:
                return temperature;
            case KELVIN:
                return convertKelvinToCelsius(temperature);
            case RANKINE:
                return convertRankineToCelsius(temperature);
            default:
                throw(new RuntimeException("Invalid termpature conversion"));
        }
    }    

    /**
     * Convert a temperature value from the Kelvin temperature scale to another.
     * 
     * @param to TemperatureScale
     * @param temperature value in Kelvin
     * @return converted temperature value in the requested to scale
     */        
    public static double convertFromKelvin(TemperatureScale to, double temperature) {
        
        switch(to) {
        
            case FARENHEIT:
                return convertKelvinToFarenheit(temperature);
            case CELSIUS:
                return convertKelvinToCelsius(temperature);
            case KELVIN:
                return temperature;
            case RANKINE:
                return convertKelvinToRankine(temperature);
            default:
                throw(new RuntimeException("Invalid termpature conversion"));
        }
    }

    /**
     * Convert a temperature value from another temperature scale into the Kelvin temperature scale.
     * 
     * @param to TemperatureScale
     * @param temperature value from other scale 
     * @return converted temperature value in Kelvin
     */    
    public static double convertToKelvin(TemperatureScale from, double temperature) {
        
        switch(from) {
        
            case FARENHEIT:
                return convertFarenheitToKelvin(temperature);
            case CELSIUS:
                return convertCelsiusToKelvin(temperature);
            case KELVIN:
                return temperature;
            case RANKINE:
                return convertRankineToKelvin(temperature);
            default:
                throw(new RuntimeException("Invalid termpature conversion"));
        }
    }

    /**
     * Convert a temperature value from the Rankine temperature scale to another.
     * 
     * @param to TemperatureScale
     * @param temperature value in degrees Rankine
     * @return converted temperature value in the requested to scale
     */      
    public static double convertFromRankine(TemperatureScale to, double temperature) {
        
        switch(to) {
        
            case FARENHEIT:
                return convertRankineToFarenheit(temperature);
            case CELSIUS:
                return convertRankineToCelsius(temperature);
            case KELVIN:
                return convertRankineToKelvin(temperature);
            case RANKINE:
                return temperature;
            default:
                throw(new RuntimeException("Invalid termpature conversion"));
        }
    }

    /**
     * Convert a temperature value from another temperature scale into the Rankine temperature scale.
     * 
     * @param to TemperatureScale
     * @param temperature value from other scale 
     * @return converted temperature value in degrees Rankine
     */    
    public static double convertToRankine(TemperatureScale from, double temperature) {
        
        switch(from) {
        
            case FARENHEIT:
                return convertFarenheitToRankine(temperature);
            case CELSIUS:
                return convertCelsiusToRankine(temperature);
            case KELVIN:
                return convertKelvinToRankine(temperature);
            case RANKINE:
                return temperature;
            default:
                throw(new RuntimeException("Invalid termpature conversion"));
        }
    }
    
    /**
     * Convert temperature from Farenheit to Celsius temperature scale
     * 
     * FORMULA = [°C] = ([°F] − 32) × 5⁄9
     * 
     * @param temperature
     * @return
     */    
    public static double convertFarenheitToCelsius(double temperature) {
        return ((temperature - 32) * 5/9);
    }

    /**
     * Convert temperature from Farenheit to Kelvin temperature scale
     * 
     * FORMULA = [K] = ([°F] + 459.67) × 5⁄9
     * 
     * @param temperature
     * @return
     */    
    public static double convertFarenheitToKelvin(double temperature) {
        return (((temperature + 459.67) * 5) / 9);
    }

    /**
     * Convert temperature from Farenheit to Rankine temperature scale
     * 
     * FORMULA = [°R] = [°F] + 459.67
     * 
     * @param temperature
     * @return
     */    
    public static double convertFarenheitToRankine(double temperature) {
        return temperature + 459.67;
    }

    /**
     * Convert temperature from Celsius to Farenheit temperature scale
     * 
     * FORMULA = [°F] = [°C] × 9⁄5 + 32
     * 
     * @param temperature
     * @return
     */    
    public static double convertCelsiusToFarenheit(double temperature) {
        return (((temperature * 9) / 5) + 32);
    }

    /**
     * Convert temperature from Celsius to Kelvin temperature scale
     * 
     * FORMULA = [K] = [°C] + 273.15
     * 
     * @param temperature
     * @return
     */    
    public static double convertCelsiusToKelvin(double temperature) {
        return (temperature - ABSOLUTE_ZERO_CELSIUS);
    }

    /**
     * Convert temperature from Celsius to Rankine temperature scale
     * 
     * FORMULA = [°R] = ([°C] + 273.15) × 9⁄5
     * 
     * @param temperature
     * @return
     */    
    public static double convertCelsiusToRankine(double temperature) {
        return (((temperature-ABSOLUTE_ZERO_CELSIUS) * 9) / 5);
    }
    
    /**
     * Convert temperature from Kelvin to Celsius temperature scale
     * 
     * FORMULA = [°C] = [K] − 273.15
     * 
     * @param temperature
     * @return
     */
    public static double convertKelvinToCelsius(double temperature) {
        return (temperature + ABSOLUTE_ZERO_CELSIUS);
    }
    
    /**
     * Convert temperature from Kelvin to Farenheit temperature scale
     * 
     * FORMULA = [°F] = [K] × 9⁄5 − 459.67
     * 
     * @param temperature
     * @return
     */
    public static double convertKelvinToFarenheit(double temperature) {
        return (((temperature * 9) / 5) - 459.67); 
    }

    /**
     * Convert temperature from Kelvin to Rankine temperature scale
     * 
     * FORMULA = [°R] = [K] × 9⁄5
     * 
     * @param temperature
     * @return
     */
    public static double convertKelvinToRankine(double temperature) {
        return ((temperature * 9) / 5); 
    }

    /**
     * Convert temperature from Rankine to Farenheit temperature scale
     * 
     * FORMULA = [°F] = [°R] − 459.67
     * 
     * @param temperature
     * @return
     */
    public static double convertRankineToFarenheit(double temperature) {
        return (temperature-(459.67));
    }

    /**
     * Convert temperature from Rankine to Celsius temperature scale
     * 
     * FORMULA = [°C] = ([°R] − 491.67) × 5⁄9
     * 
     * @param temperature
     * @return
     */
    public static double convertRankineToCelsius(double temperature) {
        return (((temperature-491.67)* 5) / 9);
    }
    
    /**
     * Convert temperature from Rankine to Kelvin temperature scale
     * 
     * FORMULA = [K] = [°R] × 5⁄9
     * 
     * @param temperature
     * @return
     */
    public static double convertRankineToKelvin(double temperature) {
        return ((temperature * 5) / 9);
    }
}
