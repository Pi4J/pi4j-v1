package com.pi4j.io.gpio.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.pi4j.io.gpio.RaspiPin;
import org.junit.Test;

public class RaspiBinTest {

    @Test
    public void test2() {
        assertEquals("GPIO 2", RaspiPin.getPinByAddress(2).getName());
    }

    @Test
    public void test15() {
        assertEquals("GPIO 15", RaspiPin.getPinByAddress(15).getName());
    }

    @Test
    public void test99() {
        assertNull(RaspiPin.getPinByAddress(99));
    }
}
