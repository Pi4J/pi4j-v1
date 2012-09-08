#include "com_pi4j_wiringpi_GpioPin.h"

// constants
#define	MAX_PI_HEADER_PINS 26
#define	MAX_GPIO_PINS   17

/*
// This array is used to determine if a
// given header pin number is valid for
// GPIO usage.
static int headerPins[MAX_PI_HEADER_PINS] =
{
    0, // HEADER PIN 01 | 3.3VDC POWER       |
    0, // HEADER PIN 02 | 5VDC POWER         |
	1, // HEADER PIN 03 | GPIO 0 (SDA)       | (WPi-08)
	0, // HEADER PIN 04 | DO NOT CONNECT     |
	1, // HEADER PIN 05 | GPIO 1 (SCL)       | (WPi-09)
	0, // HEADER PIN 06 | GROUND             |
	1, // HEADER PIN 07 | GPIO 4 (GPCLK0)    | (WPi-07)
	1, // HEADER PIN 08 | GPIO 14 (TxD)      | (WPi-15)
	0, // HEADER PIN 09 | DO NOT CONNECT     |
	1, // HEADER PIN 10 | GPIO 15 (RxD)      | (WPi-16)
	1, // HEADER PIN 11 | GPIO 17            | (WPi-00)
	1, // HEADER PIN 12 | GPIO 18 (PCM_CLK)  | (WPi-01)
	1, // HEADER PIN 13 | GPIO 21 (PCM_DOUT) | (WPi-02)
	0, // HEADER PIN 14 | DO NOT CONNECT     |
	1, // HEADER PIN 15 | GPIO 22            | (WPi-03)
	1, // HEADER PIN 16 | GPIO 23            | (WPi-04)
	0, // HEADER PIN 17 | DO NOT CONNECT     |
	1, // HEADER PIN 18 | GPIO 24            | (WPi-05)
	1, // HEADER PIN 19 | GPIO 10 (MOSI)     | (WPi-12)
	0, // HEADER PIN 20 | DO NOT CONNECT     |
	1, // HEADER PIN 21 | GPIO 9 (MISO)      | (WPi-13)
	1, // HEADER PIN 22 | GPIO 25            | (WPi-06)
	1, // HEADER PIN 23 | GPIO 11 (SCKL)     | (WPi-14)
	1, // HEADER PIN 24 | GPIO 8 (CE0)       | (WPi-10)
	0, // HEADER PIN 25 | DO NOT CONNECT     |
	1  // HEADER PIN 26 | GPIO 7 (CE1)       | (WPi-11)
};
*/

// This array is used to determine what
// internal index position is assigned to
// a given GPIO pin. A '-1' is assigned
// to any invalid GPIO pin number.
static int gpioPinIndex[MAX_PI_HEADER_PINS] =
{
    0, // GPIO 00   |  HEADER PIN 03
    1, // GPIO 01   |  HEADER PIN 05
   -1, // GPIO 02   |  N/A
   -1, // GPIO 03   |  N/A
    2, // GPIO 04   |  HEADER PIN 07
   -1, // GPIO 05   |  N/A
   -1, // GPIO 06   |  N/A
    3, // GPIO 07   |  HEADER PIN 26
    4, // GPIO 08   |  HEADER PIN 24
    5, // GPIO 09   |  HEADER PIN 21
    6, // GPIO 10   |  HEADER PIN 19
    7, // GPIO 11   |  HEADER PIN 23
   -1, // GPIO 12   |  N/A
   -1, // GPIO 13   |  N/A
    8, // GPIO 14   |  HEADER PIN 08
    9, // GPIO 15   |  HEADER PIN 10
   -1, // GPIO 16   |  N/A
   10, // GPIO 17   |  HEADER PIN 11
   11, // GPIO 18   |  HEADER PIN 12
   -1, // GPIO 19   |  N/A
   -1, // GPIO 20   |  N/A
   12, // GPIO 21   |  HEADER PIN 13
   13, // GPIO 22   |  HEADER PIN 15
   15, // GPIO 23   |  HEADER PIN 16
   15, // GPIO 24   |  HEADER PIN 18
   16  // GPIO 25   |  HEADER PIN 22
};


/**
 * --------------------------------------------------------
 * GET GPIO PIN INDEX
 * --------------------------------------------------------
 */
int getPinIndex(int pin)
{
	// validate lower bounds
	if(pin < 0)
		return -1;

	// validate upper bounds
	if(pin > MAX_PI_HEADER_PINS)
		return -1;

	// return the pin index
	// (will return -1 for invalid pin)
	return gpioPinIndex[pin];
}


/**
 * --------------------------------------------------------
 * GET GPIO PIN INDEX
 * --------------------------------------------------------
 */
int isPinValid(int pin)
{
	// validate lower bounds
	if(pin < 0)
		return 0;

	// validate upper bounds
	if(pin > MAX_PI_HEADER_PINS)
		return 0;

	// if the pin index is zero or greater, then the pin is valid
	// (will return 0 for invalid pin)
	if(gpioPinIndex[pin] >= 0)
		return 1;
	else
		return 0;
}
