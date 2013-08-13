package com.pi4j.wiringpi;

import com.pi4j.util.NativeLibraryLoader;

/**
 * This class was generated with the com.pi4j.nativ.generator.JavaClassGenerator. Please don't change this class manually
 */
public class JavaClassGeneratorTest-inputfile {

    // private constructor
    private JavaClassGeneratorTest-inputfile() {
        // forbid object construction
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("pi4j", "libpi4j.so");
    }


    public static native void myFunction( int test, String text );
    public static native void mySecondFunction( int test,String  text);
    public static native void myStringAndCharFunction( String aString, String  anotherString, char aCharacter);
}