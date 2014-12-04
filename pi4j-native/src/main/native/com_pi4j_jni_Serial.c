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
 * Copyright (C) 2012 - 2015 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
#include <sys/epoll.h>
#include <linux/serial.h>

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
 * Signature: (Ljava/lang/String;IIIIIZZZ)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_Serial_open
  (JNIEnv *env, jclass obj, jstring port, jint baud, jint dataBits, jint parity, jint stopBits,
   jint flowControl, jboolean echo, jboolean flushRx, jboolean flushTx)
{
    struct serial_struct serinfo;
    struct termios options ;
    speed_t myBaud ;
    int     status, fd ;
	char device[256];

	// get device char arrat from Java string object
	int len = (*env)->GetStringLength(env, port);
	(*env)->GetStringUTFRegion(env, port, 0, len, device);

    // open serial port
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
    options.c_cflag |= CS8 ;

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

    // configure echo back of received bytes to sender
    if(echo == JNI_FALSE){
        options.c_lflag &= ~ECHO; // disable ECHO
    }
    else{
        options.c_lflag |= ECHO;  // enable ECHO
    }

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

    // flush serial recieve buffer
    if(flushRx == JNI_TRUE) tcflush (fd, TCIFLUSH);

    // flush serial recieve buffer
    if(flushTx == JNI_TRUE) tcflush (fd, TCOFLUSH);

    // ok, lets take a short breath ...
    usleep (10000) ;	// 10mS


//
//    // create epoll
//    int epfd = epoll_create(1); // argument must be greater than 0; but value is not used
//
//    struct epoll_event ev;
//    struct epoll_event events;
//    ev.events = EPOLLIN | EPOLLPRI | EPOLLERR | EPOLLHUP;
//    ev.data.fd = fd;
//
//    int res = epoll_ctl(epfd, EPOLL_CTL_ADD, fd, &ev);
//
//    if( res < 0 )
//        printf("Error epoll_ctl: %i\n", errno);
//
//    while(1){
//        // wait for input
//        int n = epoll_wait(epfd, &events, 1, -1);
//
//        printf("Epoll unblocked\n");
//        if(n < 0)
//            perror("Epoll failed\n");
//        else if(n==0)
//            printf("TIMEOUT\n");
//        else
//        {
//           // sleep a little before raising event notification (callback)
//           usleep (50000) ;	// 50mS
//
//           int length = getAvailableByteCount(fd);
//           printf("Bytes available: %d\n", length);
//
//           // copy the data bytes from the serial receive buffer into the payload result
//           int i;
//           for (i = 0; i < length; i++) {
//
//                // read a single byte from the RX buffer
//                uint8_t x ;
//                if (read (fd, &x, 1) != 1){
//                    return NULL;   // ERROR READING RX BUFFER
//                }
//           }
//        }
//    }

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
JNIEXPORT void JNICALL Java_com_pi4j_jni_Serial_flush__I
  (JNIEnv *env, jclass obj, jint fd)
{
	// flush the transmit and receive buffers for the serial port
	if(tcflush (fd, TCIOFLUSH) == -1){
        int err_number = errno;
        char err_message[100];
        sprintf(err_message, "Failed to flush serial input (RX) and output (TX) buffers. (Error #%d)", err_number);
        throwIOException(env, err_message);
	}
}

/*
 * Class:     com_pi4j_jni_Serial
 * Method:    flush
 * Signature: (IZZ)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_jni_Serial_flush__IZZ
  (JNIEnv *env, jclass obj, jint fd, jboolean rxBuffer, jboolean txBuffer)
{
    // RX BUFFER
    if(rxBuffer == JNI_TRUE){
        // flush the receive buffes for the serial port
        if(tcflush (fd, TCIFLUSH) == -1){
            int err_number = errno;
            char err_message[100];
            sprintf(err_message, "Failed to flush serial input (RX) buffer. (Error #%d)", err_number);
            throwIOException(env, err_message);
        }
    }

    // TX BUFFER
    if(txBuffer == JNI_TRUE){
        // flush the transmit buffer for the serial port
        if(tcflush (fd, TCOFLUSH) == -1){
            int err_number = errno;
            char err_message[100];
            sprintf(err_message, "Failed to flush serial output (TX) buffer. (Error #%d)", err_number);
            throwIOException(env, err_message);
        }
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
	if(tcsendbreak(fd, duration) == -1){
        int err_number = errno;
        char err_message[100];
        sprintf(err_message, "Failed to transmit BREAK signal to serial port. (Error #%d)", err_number);
        throwIOException(env, err_message);
	}
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
