package com.pi4j.gpio.helpers;


public class GpioHelpers {

    public static short bytesToShort(byte[] bytes, int position) {
        int size = bytes.length;
        int value = 0;

        if (size > position) {
            value = (short) bytes[position];
            if (value < 0) {
                value = 255 + value;
            }
        }

        return (short) value;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }
}
