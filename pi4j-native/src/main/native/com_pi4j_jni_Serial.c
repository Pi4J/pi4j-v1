/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_jni_Serial.c
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2017 Pi4J
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
#include <jni.h>
#include "com_pi4j_jni_Exception.h"
#include "com_pi4j_jni_Serial.h"

/* Source for com_pi4j_jni_Serial */

#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <stdarg.h>
#include <string.h>
#include <termios.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <linux/serial.h>
#include <err.h>

// determine result data array length from the number of bytes available on the receive buffer
int getAvailableByteCount(int fd){
    int length;
    if (ioctl (fd, FIONREAD, &length) == -1){
        return -1;
    }
    return length;
}

/*
 *********************************************************************************
 *	Open and initialise the serial port, setting all the right
 *	serial communication port parameters
 *********************************************************************************
 */

// constants
#define	PARITY_NONE    0L
#define	PARITY_ODD     1L
#define	PARITY_EVEN    2L
#define	PARITY_MARK    3L
#define	PARITY_SPACE   4L
#define CMSPAR   010000000000

#define	DATA_BITS_5    5L
#define	DATA_BITS_6    6L
#define	DATA_BITS_7    7L
#define	DATA_BITS_8    8L

#define	STOP_BITS_1    1L
#define	STOP_BITS_2    2L

#define	FLOW_CONTROL_NONE       0L
#define	FLOW_CONTROL_HARDWARE   1L
#define	FLOW_CONTROL_SOFTWARE   2L

/*
 * Class:     com_pi4j_jni_Serial
 * Method:    open
 * Signature: (Ljava/lang/String;IIIII)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_Serial_open
  (JNIEnv *env, jclass obj, jstring port, jint baud, jint dataBits, jint parity, jint stopBits,
   jint flowControl)
{
    struct termios options ;
    speed_t myBaud ;
    int     status, fd ;
	char device[256];

	// get device char arrat from Java string object
	int len = (*env)->GetStringLength(env, port);
	(*env)->GetStringUTFRegion(env, port, 0, len, device);

    // open serial port //
    if ((fd = open (device, O_RDWR | O_NOCTTY | O_NDELAY | O_NONBLOCK)) == -1){
        int err_number = errno;
        char err_message[100];
        sprintf(err_message, "Unable to open the serial port/device. (Error #%d)", err_number);
        throwIOException(env, err_message);
        return -1 ;
    }

    // allow READ/WRITE operation on serial port
    if(fcntl (fd, F_SETFL, O_RDWR) == -1){
        int err_number = errno;
        char err_message[100];
        sprintf(err_message, "Unable to configure serial file descriptor for R/W access. (Error #%d)", err_number);
        throwIOException(env, err_message);
        return -2 ;
    }

    // load serial options/configuration
    if(tcgetattr (fd, &options) == -1){
        int err_number = errno;
        char err_message[100];
        sprintf(err_message, "Failed to GET terminal attributes for the serial port. (Error #%d)", err_number);
        throwIOException(env, err_message);
    }

    cfmakeraw   (&options) ;


    // -------------------
    // BAUD RATE
    // -------------------

    // determine baud rate
    switch (baud)
    {
        case     50:	myBaud =     B50 ; break ;
        case     75:	myBaud =     B75 ; break ;
        case    110:	myBaud =    B110 ; break ;
        case    134:	myBaud =    B134 ; break ;
        case    150:	myBaud =    B150 ; break ;
        case    200:	myBaud =    B200 ; break ;
        case    300:	myBaud =    B300 ; break ;
        case    600:	myBaud =    B600 ; break ;
        case   1200:	myBaud =   B1200 ; break ;
        case   1800:	myBaud =   B1800 ; break ;
        case   2400:	myBaud =   B2400 ; break ;
        case   4800:	myBaud =   B4800 ; break ;
        case   9600:	myBaud =   B9600 ; break ;
        case  19200:	myBaud =  B19200 ; break ;
        case  38400:	myBaud =  B38400 ; break ;
        case  57600:	myBaud =  B57600 ; break ;
        case 115200:	myBaud = B115200 ; break ;
        case 230400:	myBaud = B230400 ; break ;

        default:{

            // CUSTOM BAUD RATE IMPL FROM:
            // http://jim.sh/ftx/files/linux-custom-baudrate.c

            struct serial_struct serinfo;

            // custom baud rate
            myBaud = 0;

            /* Custom divisor for non-standard BAUD */
            serinfo.reserved_char[0] = 0;
            if (ioctl(fd, TIOCGSERIAL, &serinfo) < 0)
                return -1;
            serinfo.flags &= ~ASYNC_SPD_MASK;
            serinfo.flags |= ASYNC_SPD_CUST;
            serinfo.custom_divisor = (serinfo.baud_base + (baud / 2)) / baud;
            if (serinfo.custom_divisor < 1)
                serinfo.custom_divisor = 1;
            if (ioctl(fd, TIOCSSERIAL, &serinfo) < 0){
                int err_number = errno;
                char err_message[100];
                sprintf(err_message, "Invalid/unsupported BAUD rate.[TIOCSSERIAL] (Error #%d)", err_number);
                throwIOException(env, err_message);
                return -3 ;  // INVALID/UNSUPPORTED BAUD RATE
            }
            if (ioctl(fd, TIOCGSERIAL, &serinfo) < 0){
                int err_number = errno;
                char err_message[100];
                sprintf(err_message, "Invalid/unsupported BAUD rate. [TIOCGSERIAL] (Error #%d)", err_number);
                throwIOException(env, err_message);
                return -3 ;  // INVALID/UNSUPPORTED BAUD RATE
            }
            if (serinfo.custom_divisor * baud != serinfo.baud_base) {
                warnx("actual baudrate is %d / %d = %f",
                      serinfo.baud_base, serinfo.custom_divisor,
                      (float)serinfo.baud_base / serinfo.custom_divisor);
            }
        }
    }

    // set baud rate; custom baud rates are designated with a 'myBaud == 0' and the B38400 baud config should be set
    if(cfsetispeed (&options, myBaud ?: B38400) == -1){
        int err_number = errno;
        char err_message[100];
        sprintf(err_message, "Failed to set (input) serial port baud rate. (Error #%d)", err_number);
        throwIOException(env, err_message);
    }
    if(cfsetospeed (&options, myBaud ?: B38400)){
        int err_number = errno;
        char err_message[100];
        sprintf(err_message, "Failed to set (output) serial port baud rate. (Error #%d)", err_number);
        throwIOException(env, err_message);
    }

    // -----------------------
    // HARDWARE CONFIGURATION
    // -----------------------

    // ignore modem status lines.
    options.c_cflag |= CLOCAL;

    // enable receiver
    options.c_cflag |= CREAD;

    // set data bits configuration
    options.c_cflag &= ~CSIZE ;

    // set data bits configuration
    switch(dataBits)
    {
        case DATA_BITS_5:
            options.c_cflag |= CS5 ; // 5 data bits
            break;
        case DATA_BITS_6:
            options.c_cflag |= CS6 ; // 6 data bits
            break;
        case DATA_BITS_7:
            options.c_cflag |= CS7 ; // 7 data bits
            break;
        case DATA_BITS_8:
            options.c_cflag |= CS8 ; // 8 data bits
            break;

        default:{
            throwIOException( env, "Invalid/unsupported DATA BITS configuration property");
            return -4 ;  // INVALID/UNSUPPORTED DATA BITS CONFIG
        }
    }

    // set parity configuration
    switch(parity)
    {
        case PARITY_NONE :
            options.c_cflag &= ~PARENB;
            break;
        case PARITY_ODD :
            options.c_cflag |= PARENB | PARODD;
            break;
        case PARITY_EVEN :
            options.c_cflag |= PARENB;
            options.c_cflag &= ~PARODD;
            break;
        case PARITY_SPACE :
            // https://viereck.ch/linux-mark-space-parity/
            options.c_cflag |= PARENB | CMSPAR;
            options.c_cflag &= ~PARODD;
            break;
        case PARITY_MARK :
            // https://viereck.ch/linux-mark-space-parity/
            options.c_cflag |= PARENB | CMSPAR | PARODD;
            break;

        default:{
            throwIOException( env, "Invalid/unsupported PARITY configuration property");
            return -5 ;  // INVALID/UNSUPPORTED PARITY CONFIG
        }
    }

    // set stop bits
    if(stopBits == STOP_BITS_1){
        options.c_cflag &= ~CSTOPB; // 1 stop bit
    }
    else if(stopBits == STOP_BITS_2){
        options.c_cflag |= CSTOPB;  // 2 stop bits
    }
    else{
        throwIOException( env, "Invalid/unsupported STOP BITS configuration property");
        return -6; // INVALID/UNSUPPORTED STOP BITS CONFIG
    }

    // set hardware flow control configuration
    if(flowControl == FLOW_CONTROL_HARDWARE){
        options.c_cflag |= CRTSCTS; // enable hardware flow control
    }
    else{
        options.c_cflag &= ~CRTSCTS; // disable hardware flow control
    }


    // ------------------------
    // LOCAL MODE CONFIGURATION
    // ------------------------

    // disable echo
    options.c_lflag &= ~ECHO; // disable ECHO

    // do not echo erase character as error-correcting backspace (this is not a terminal)
    options.c_lflag &= ~ECHOE;

    // disable canonical input (we want raw input)
    options.c_lflag &= ~ICANON;

    // disable extended functions
    options.c_lflag &= ~IEXTEN;

    // disable SIGINTR, SIGSUSP, SIGDSUSP, and SIGQUIT signals
    options.c_lflag &= ~ISIG;


    // ------------------------
    // OUTPUT MODE CONFIGURATION
    // ------------------------

    // disable post-process of output
    options.c_oflag &= ~OPOST;


    // ------------------------
    // INPUT MODE CONFIGURATION
    // ------------------------

    // configure software flow contorl
    if(flowControl == FLOW_CONTROL_SOFTWARE){
        options.c_iflag |= (IXON | IXOFF | IXANY);  // enable software flow control
    }
    else{
        options.c_iflag &= ~(IXON | IXOFF | IXANY); // disable software flow control
    }

    // --------------------------------
    // CONTROL CHARACTER CONFIGURATION
    // --------------------------------
    options.c_cc [VMIN]  =   0 ;
    options.c_cc [VTIME] = 100 ;	// Ten seconds (100 deciseconds)


    // apply serial port configuration options now
    if(tcsetattr (fd, TCSANOW | TCSAFLUSH, &options) == -1){
        int err_number = errno;
        char err_message[100];
        sprintf(err_message, "Failed to SET terminal attributes for the serial port. (Error #%d)", err_number);
        throwIOException(env, err_message);
    }


    // get the state of the "MODEM" bits
    ioctl (fd, TIOCMGET, &status);

    // manually configure the pin states
    status |= TIOCM_DTR ;  // drive the DATA TERMINAL READY pin to the HIGH state; so senders know its OK to send data
    status |= TIOCM_RTS ;  // drive the REQUEST TO SEND pin to the HIGH state; so senders know its OK to send data

    // set the state of the "MODEM" bits
    ioctl (fd, TIOCMSET, &status);

    // flush serial receive and transmit buffers
    tcflush (fd, TCIOFLUSH);

    // ok, lets take a short breath ...
    usleep(50000) ; // 50 ms

    // flush serial receive and transmit buffers
    tcflush (fd, TCIOFLUSH);

    // return file descriptor
	return fd;
}


/*
 *********************************************************************************
 *	Close/release the serial port
 *********************************************************************************
 */

/*
 * Class:     com_pi4j_jni_Serial
 * Method:    close
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_jni_Serial_close
  (JNIEnv *env, jclass obj, jint fd)
{
    // close serial port
    if(close(fd) == -1){
        int err_number = errno;
        char err_message[100];
        sprintf(err_message, "Failed to close serial file descriptor. (Error #%d)", err_number);
        throwIOException(env, err_message);
    }
}

/*
 *********************************************************************************
 *	Flush the serial buffers (both tx & rx)
 *********************************************************************************
 */

/*
 * Class:     com_pi4j_jni_Serial
 * Method:    flush
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_jni_Serial_flush
  (JNIEnv *env, jclass obj, jint fd)
{
	// drain the transmit buffer to the serial port
	if(tcdrain(fd) != 0){
        int err_number = errno;
        char err_message[100];
        sprintf(err_message, "Failed to drain serial output (TX) buffers. (Error #%d)", err_number);
        throwIOException(env, err_message);
	}
}

/*
 * Class:     com_pi4j_jni_Serial
 * Method:    discardInput
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_jni_Serial_discardInput
  (JNIEnv *env, jclass obj, jint fd)
{
    // flush the receive buffer for the serial port
    if(tcflush (fd, TCIFLUSH) != 0){
        int err_number = errno;
        char err_message[100];
        sprintf(err_message, "Failed to discard serial input (RX) buffer. (Error #%d)", err_number);
        throwIOException(env, err_message);
    }
}


/*
 * Class:     com_pi4j_jni_Serial
 * Method:    discardOutput
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_jni_Serial_discardOutput
  (JNIEnv *env, jclass obj, jint fd)
{
    // flush the transmit buffer for the serial port
    if(tcflush (fd, TCOFLUSH) != 0){
        int err_number = errno;
        char err_message[100];
        sprintf(err_message, "Failed to discard serial output (TX) buffer. (Error #%d)", err_number);
        throwIOException(env, err_message);
    }
}


/*
 * Class:     com_pi4j_jni_Serial
 * Method:    discardAll
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_jni_Serial_discardAll
  (JNIEnv *env, jclass obj, jint fd)
{
    // flush the transmit and receive buffers for the serial port
	if(tcflush (fd, TCIOFLUSH) != 0){
        int err_number = errno;
        char err_message[100];
        sprintf(err_message, "Failed to discard serial input (RX) and output (TX) buffers. (Error #%d)", err_number);
        throwIOException(env, err_message);
	}
}



/*
 *********************************************************************************
 *	Send a BREAK signal to connected device
 *********************************************************************************
 */

/*
 * Class:     com_pi4j_jni_Serial
 * Method:    sendBreak
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_jni_Serial_sendBreak
  (JNIEnv *env, jclass obj, jint fd, jint duration)
{
	// transmit break
	if(tcsendbreak(fd, duration) != 0){
        int err_number = errno;
        char err_message[100];
        sprintf(err_message, "Failed to transmit BREAK signal to serial port. (Error #%d)", err_number);
        throwIOException(env, err_message);
	}
}

/*
 * Class:     com_pi4j_jni_Serial
 * Method:    setBreak
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_jni_Serial_setBreak
  (JNIEnv *env, jclass obj, jint fd, jboolean enabled)
{
  if(enabled == JNI_FALSE){
     if(ioctl(fd, TIOCCBRK, NULL) == 0) return;
  }
  else{
     if(ioctl(fd, TIOCSBRK, NULL) == 0) return;
  }

  // raise IOException if failed
  int err_number = errno;
  char err_message[100];
  sprintf(err_message, "Failed to set RTS pin state. (Error #%d)", err_number);
  throwIOException(env, err_message);
}

/*
 *********************************************************************************
 *	Serial Port PIN states
 *********************************************************************************
 */


/*
 * Class:     com_pi4j_jni_Serial
 * Method:    setRTS
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_jni_Serial_setRTS
  (JNIEnv *env, jclass obj, jint fd, jboolean enabled)
{
  int status = 0;
  int ret;

  status |= TIOCM_RTS;

  if(enabled == JNI_FALSE)
  {
     ret = ioctl(fd, TIOCMBIC, &status);
  }
  else
  {
     ret = ioctl(fd, TIOCMBIS, &status);
  }

  // raise IOException if failed
  if(ret != 0)
  {
    int err_number = errno;
    char err_message[100];
    sprintf(err_message, "Failed to set RTS pin state. (Error #%d)", err_number);
    throwIOException(env, err_message);
  }
}

/*
 * Class:     com_pi4j_jni_Serial
 * Method:    setDTR
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_jni_Serial_setDTR
  (JNIEnv *env, jclass obj, jint fd, jboolean enabled)
{
  int status = 0;
  int ret;

  status |= TIOCM_DTR;

  if(enabled == JNI_FALSE)
  {
     ret = ioctl(fd, TIOCMBIC, &status);
  }
  else
  {
     ret = ioctl(fd, TIOCMBIS, &status);
  }

  // raise IOException if failed
  if(ret != 0)
  {
    int err_number = errno;
    char err_message[100];
    sprintf(err_message, "Failed to set DTR pin state. (Error #%d)", err_number);
    throwIOException(env, err_message);
  }
}

/*
 * Class:     com_pi4j_jni_Serial
 * Method:    getRTS
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_pi4j_jni_Serial_getRTS
  (JNIEnv *env, jclass obj, jint fd)
{
  int status;

  // attempt to get status
  if (ioctl(fd, TIOCMGET, &status) == -1)
  {
    // raise IOException if failed
    int err_number = errno;
    char err_message[100];
    sprintf(err_message, "Failed to get RTS pin state. (Error #%d)", err_number);
    throwIOException(env, err_message);
  }
  else {
    return (status & TIOCM_RTS) ? JNI_TRUE : JNI_FALSE;
  }
  return JNI_FALSE;
}

/*
 * Class:     com_pi4j_jni_Serial
 * Method:    getDTR
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_pi4j_jni_Serial_getDTR
  (JNIEnv *env, jclass obj, jint fd)
{
  int status;

  // attempt to get status
  if (ioctl(fd, TIOCMGET, &status) == -1)
  {
    // raise IOException if failed
    int err_number = errno;
    char err_message[100];
    sprintf(err_message, "Failed to get DTR pin state. (Error #%d)", err_number);
    throwIOException(env, err_message);
  }
  else {
    return (status & TIOCM_DTR) ? JNI_TRUE : JNI_FALSE;
  }
  return JNI_FALSE;
}

/*
 * Class:     com_pi4j_jni_Serial
 * Method:    getCTS
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_pi4j_jni_Serial_getCTS
  (JNIEnv *env, jclass obj, jint fd)
{
  int status;

  // attempt to get status
  if (ioctl(fd, TIOCMGET, &status) == -1)
  {
    // raise IOException if failed
    int err_number = errno;
    char err_message[100];
    sprintf(err_message, "Failed to get CTS pin state. (Error #%d)", err_number);
    throwIOException(env, err_message);
  }
  else {
    return (status & TIOCM_CTS) ? JNI_TRUE : JNI_FALSE;
  }
  return JNI_FALSE;
}

/*
 * Class:     com_pi4j_jni_Serial
 * Method:    getDSR
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_pi4j_jni_Serial_getDSR
  (JNIEnv *env, jclass obj, jint fd)
{
  int status;

  // attempt to get status
  if (ioctl(fd, TIOCMGET, &status) == -1)
  {
    // raise IOException if failed
    int err_number = errno;
    char err_message[100];
    sprintf(err_message, "Failed to get DSR pin state. (Error #%d)", err_number);
    throwIOException(env, err_message);
  }
  else {
    return (status & TIOCM_DSR) ? JNI_TRUE : JNI_FALSE;
  }
  return JNI_FALSE;
}

/*
 * Class:     com_pi4j_jni_Serial
 * Method:    getRI
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_pi4j_jni_Serial_getRI
  (JNIEnv *env, jclass obj, jint fd)
{
  int status;

  // attempt to get status
  if (ioctl(fd, TIOCMGET, &status) == -1)
  {
    // raise IOException if failed
    int err_number = errno;
    char err_message[100];
    sprintf(err_message, "Failed to get RI pin state. (Error #%d)", err_number);
    throwIOException(env, err_message);
  }
  else {
    return (status & TIOCM_RI) ? JNI_TRUE : JNI_FALSE;
  }
  return JNI_FALSE;
}

/*
 * Class:     com_pi4j_jni_Serial
 * Method:    getCD
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_pi4j_jni_Serial_getCD
  (JNIEnv *env, jclass obj, jint fd)
{
  int status;

  // attempt to get status
  if (ioctl(fd, TIOCMGET, &status) == -1)
  {
    // raise IOException if failed
    int err_number = errno;
    char err_message[100];
    sprintf(err_message, "Failed to get CD pin state. (Error #%d)", err_number);
    throwIOException(env, err_message);
  }
  else {
    return (status & TIOCM_CD) ? JNI_TRUE : JNI_FALSE;
  }
  return JNI_FALSE;
}


/*
 *********************************************************************************
 *	Return the number of bytes of data available to be read in the serial port
 *********************************************************************************
 */

/*
 * Class:     com_pi4j_jni_Serial
 * Method:    available
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_Serial_available
  (JNIEnv *env, jclass obj, jint fd)
{
    return getAvailableByteCount(fd);
}

/*
 *********************************************************************************
 *	Enable/Disable Echo (receive data back to sender)
 *********************************************************************************
 */

/*
 * Class:     com_pi4j_jni_Serial
 * Method:    echo
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_jni_Serial_echo
(JNIEnv *env, jclass obj, jint fd, jboolean enabled)
{
    struct termios options ;

    // get and modify current options:
    if(tcgetattr (fd, &options) == -1){
        int err_number = errno;
        char err_message[100];
        sprintf(err_message, "Failed to GET terminal attributes for the serial port. (Error #%d)", err_number);
        throwIOException(env, err_message);
    }

    if(enabled == JNI_FALSE){
        // disable ECHO
        options.c_lflag &= ~ECHO;
    }
    else{
        // enable ECHO
        options.c_lflag |= ECHO;
    }

    // set updated options
    if(tcsetattr (fd, TCSANOW, &options) == -1){
        int err_number = errno;
        char err_message[100];
        sprintf(err_message, "Failed to SET terminal attributes for the serial port. (Error #%d)", err_number);
        throwIOException(env, err_message);
    }
}

/*
 *********************************************************************************
 *	Write data in byte array to the serial port transmit buffer
 *********************************************************************************
 */

/*
 * Class:     com_pi4j_jni_Serial
 * Method:    write
 * Signature: (I[BJ)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_jni_Serial_write
(JNIEnv *env, jclass obj, jint fd, jbyteArray data, jlong length)
{
    jbyte *ptr = (*env)->GetByteArrayElements(env, data, 0);
    if(write (fd, ptr, length) == -1){
        int err_number = errno;
        char err_message[100];
        sprintf(err_message, "Failed to write data to serial port. (Error #%d)", err_number);
        throwIOException(env, err_message);
    }
	(*env)->ReleaseByteArrayElements(env, data, ptr, 0);
}


/*
 *********************************************************************************
 *	Write data in byte buffer to the serial port transmit buffer
 *********************************************************************************
 */

/*
* Class:     com_pi4j_jni_Serial
* Method:    write
* Signature: (IOL)V
*/
JNIEXPORT void JNICALL Java_com_pi4j_jni_Serial_writeBuffer
(JNIEnv *env, jclass obj, jint fd, jobject buffer, jlong length)
{
    jbyte *ptr = (*env)->GetDirectBufferAddress(env, buffer);
    if(write (fd, ptr, length) == -1){
        int err_number = errno;
        char err_message[100];
        sprintf(err_message, "Failed to write data to serial port. (Error #%d)", err_number);
        throwIOException(env, err_message);
    }
}


/*
 *********************************************************************************
 *	Reads bytes from serial port receive buffer
 *	Note: An empty array is returned if no data is available in the receive buffer
 *	10 seconds.
 *********************************************************************************
 */

/*
 * Class:     com_pi4j_jni_Serial
 * Method:    read
 * Signature: (II)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_pi4j_jni_Serial_read__II
  (JNIEnv *env, jclass obj, jint fd, jint length)
{
    // determine result data array length from the number of bytes available on the receive buffer
    int availableBytes;
    availableBytes = getAvailableByteCount(fd);

    // check for error return; if error, then return NULL
    if (availableBytes < 0){
        int err_number = errno;
        char err_message[100];
        sprintf(err_message, "Error attempting to read data from serial port. (Error #%d)", err_number);
        throwIOException(env, err_message);
        return (*env)->NewByteArray(env, 0);  // ERROR READING RX BUFFER
    }

    // reduce length if it exceeds the number available
    if(length > availableBytes){
        length = availableBytes;
    }

    // create a new payload result byte array
    jbyte result[length];

    // copy the data bytes from the serial receive buffer into the payload result
    int i;
    for (i = 0; i < length; i++) {

        // read a single byte from the RX buffer
        uint8_t x ;
        if (read (fd, &x, 1) != 1){
            int err_number = errno;
            char err_message[100];
            sprintf(err_message, "Failed to read data from serial port. (Error #%d)", err_number);
            throwIOException(env, err_message);
            return (*env)->NewByteArray(env, 0); // ERROR READING RX BUFFER
        }

        // assign the single byte; cast to unsigned char
        result[i] = (unsigned char)(((int)x) & 0xFF);
    }

    // create a java array object and copy the raw payload bytes
    jbyteArray javaResult = (*env)->NewByteArray(env, length);
    (*env)->SetByteArrayRegion(env, javaResult, 0, length, result);
    return javaResult;
}


/*
 *********************************************************************************
 *	Reads all available bytes from serial port receive buffer
 *	Note: An empty array is returned if no data is available in the receive buffer
 *	10 seconds.
 *********************************************************************************
 */

/*
 * Class:     com_pi4j_jni_Serial
 * Method:    read
 * Signature: (I)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_pi4j_jni_Serial_read__I
  (JNIEnv *env, jclass obj, jint fd)
{
    // determine result data array length from the number of bytes available on the receive buffer
    int length;
    length = getAvailableByteCount(fd);

    // check for error return; if error, then return NULL
    if (length < 0){
        int err_number = errno;
        char err_message[100];
        sprintf(err_message, "Error attempting to read data from serial port. (Error #%d)", err_number);
        throwIOException(env, err_message);
        return (*env)->NewByteArray(env, 0);  // ERROR READING RX BUFFER
    }

    // create a new payload result byte array
    jbyte result[length];

    // copy the data bytes from the serial receive buffer into the payload result
    int i;
    for (i = 0; i < length; i++) {

        // read a single byte from the RX buffer
        uint8_t x ;
        if (read (fd, &x, 1) != 1){
            int err_number = errno;
            char err_message[100];
            sprintf(err_message, "Failed to read data from serial port. (Error #%d)", err_number);
            throwIOException(env, err_message);
            return (*env)->NewByteArray(env, 0);   // ERROR READING RX BUFFER
        }

        // assign the single byte; cast to unsigned char
        result[i] = (unsigned char)(((int)x) & 0xFF);
    }

    // create a java array object and copy the raw payload bytes
    jbyteArray javaResult = (*env)->NewByteArray(env, length);
    (*env)->SetByteArrayRegion(env, javaResult, 0, length, result);
    return javaResult;
}

/*
 *********************************************************************************
 *	Reads all available bytes from serial port receive buffer into provided
 *  direct buffer.  Returns the number of bytes written to the direct buffer.
 *********************************************************************************
 */

/*
 * Class:     com_pi4j_jni_Serial
 * Method:    readToBuffer
 * Signature: (IOI)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_Serial_readToBuffer
  (JNIEnv *env, jclass obj, jint fd, jobject buffer, jint length)
{
    // determine result data array length from the number of bytes available on the receive buffer
    int availableBytes;
    availableBytes = getAvailableByteCount(fd);

    // check for error return; if error, then return NULL
    if (availableBytes < 0){
        int err_number = errno;
        char err_message[100];
        sprintf(err_message, "Error attempting to read data from serial port. (Error #%d)", err_number);
        throwIOException(env, err_message);
        return -1;   // ERROR READING RX BUFFER
    }

    // reduce length if it exceeds the number available
    if(length > availableBytes){
        length = availableBytes;
    }

    jbyte *result = (*env)->GetDirectBufferAddress(env, buffer);

    // copy the data bytes from the serial receive buffer into the payload result
    int i;
    for (i = 0; i < length; i++) {

        // read a single byte from the RX buffer
        uint8_t x ;
        if (read (fd, &x, 1) != 1){
            int err_number = errno;
            char err_message[100];
            sprintf(err_message, "Failed to read data from serial port. (Error #%d)", err_number);
            throwIOException(env, err_message);
            return -1;   // ERROR READING RX BUFFER
        }

        // assign the single byte; cast to unsigned char
        result[i] = (unsigned char)(((int)x) & 0xFF);
    }

    // return number of bytes written into buffer
    return length;
}
