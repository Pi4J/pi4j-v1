#ifndef _Included_com_pi4j_wiringpi_GpioPin
#define _Included_com_pi4j_wiringpi_GpioPin
#ifdef __cplusplus
extern "C" {
#endif


/**
 * --------------------------------------------------------
 * GET GPIO PIN INDEX
 * --------------------------------------------------------
 */
int getPinIndex(int);


/**
 * --------------------------------------------------------
 * DETERMINE IF GPIO PIN IS VALID
 * --------------------------------------------------------
 */
int isPinValid(int);


#ifdef __cplusplus
}
#endif
#endif
