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
 * Signature: (Ljava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_Serial_open
  (JNIEnv *env, jobject obj, jstring port, jint baud, jint dataBits, jint parity, jint stopBits,
   jint flowControl, jbyte echo, jbyte flushRx, jbyte flushTx)
{
    struct termios options ;
    speed_t myBaud ;
    int     status, fd ;
	char device[256];

	// get device char arrat from Java string object
	int len = (*env)->GetStringLength(env, port);
	(*env)->GetStringUTFRegion(env, port, 0, len, device);

    // open serial port
    if ((fd = open (device, O_RDWR | O_NOCTTY | O_NDELAY | O_NONBLOCK)) == -1)
        return -1 ;

    // allow READ/WRITE operation on serial port
    fcntl (fd, F_SETFL, O_RDWR) ;

    // load serial options/configuration
    tcgetattr (fd, &options) ;
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

        default:
            return -2 ;  // INVALID/UNSUPPORTED BAUD RATE
    }

    // set baud rate
    cfsetispeed (&options, myBaud) ;
    cfsetospeed (&options, myBaud) ;


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

        default:
            return -3 ;  // INVALID/UNSUPPORTED DATA BITS CONFIG
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

        default:
            return -4 ;  // INVALID/UNSUPPORTED PARITY CONFIG
    }

    // set stop bits
    if(stopBits == STOP_BITS_1)
        options.c_cflag &= ~CSTOPB; // 1 stop bit
    else if(stopBits == STOP_BITS_2)
        options.c_cflag |= CSTOPB;  // 2 stop bits
    else
        return -5; // INVALID/UNSUPPORTED STOP BITS CONFIG

    // set hardware flow control configuration
    if(flowControl == FLOW_CONTROL_HARDWARE)
        options.c_cflag |= CRTSCTS; // enable hardware flow control
    else
        options.c_cflag &= ~CRTSCTS; // disable hardware flow control


    // ------------------------
    // LOCAL MODE CONFIGURATION
    // ------------------------

    // configure echo back of received bytes to sender
    if(echo <= 0)
        options.c_lflag &= ~ECHO; // disable ECHO
    else
        options.c_lflag |= ECHO;  // enable ECHO

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
    if(flowControl == FLOW_CONTROL_SOFTWARE)
        options.c_iflag |= (IXON | IXOFF | IXANY);  // enable software flow control
     else
        options.c_iflag &= ~(IXON | IXOFF | IXANY); // disable software flow control


    // --------------------------------
    // CONTROL CHARACTER CONFIGURATION
    // --------------------------------
    options.c_cc [VMIN]  =   0 ;
    options.c_cc [VTIME] = 100 ;	// Ten seconds (100 deciseconds)


    // apply serial port configuration options now
    tcsetattr (fd, TCSANOW | TCSAFLUSH, &options) ;


    // get the state of the "MODEM" bits
    ioctl (fd, TIOCMGET, &status);

    // manually configure the pin states
    status |= TIOCM_DTR ;  // drive the DATA TERMINAL READY pin to the HIGH state; so senders know its OK to send data
    status |= TIOCM_RTS ;  // drive the REQUEST TO SEND pin to the HIGH state; so senders know its OK to send data

    // set the state of the "MODEM" bits
    ioctl (fd, TIOCMSET, &status);

    // flush serial recieve buffer
    if(flushRx > 0) tcflush (fd, TCIFLUSH);

    // flush serial recieve buffer
    if(flushTx > 0) tcflush (fd, TCOFLUSH);

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
  (JNIEnv *env, jobject obj, jint fd)
{
    // close serial port
    close(fd);
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
  (JNIEnv *env, jobject obj, jint fd)
{
	// flush the transmit and receive buffers for the serial port
	tcflush (fd, TCIOFLUSH);
}

/*
 * Class:     com_pi4j_jni_Serial
 * Method:    flushTx
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_jni_Serial_flushTx
  (JNIEnv *env, jobject obj, jint fd)
{
	// flush the transmit and receive buffers for the serial port
	tcflush (fd, TCOFLUSH);
}

/*
 *********************************************************************************
 *	Send a BREAK signal to connected device
 *********************************************************************************
 */

/*
 * Class:     com_pi4j_jni_Serial
 * Method:    sendBreak
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_jni_Serial_sendBreak
  (JNIEnv *env, jobject obj, jint fd, jint duration)
{
	// transmit break
	return tcsendbreak(fd, duration);
}

/*
 *********************************************************************************
 *	Write data in byte array to the serial port transmit buffer
 *********************************************************************************
 */

/*
 * Class:     com_pi4j_jni_Serial
 * Method:    write
 * Signature: (I[BI)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_jni_Serial_write
(JNIEnv *env, jobject obj, jint fd, jbyteArray data, jint length)
{
    int i;
    unsigned char c;
    jbyte *body = (*env)->GetByteArrayElements(env, data, 0);
    for (i = 0; i < length; i++) {
        c = (unsigned char) body[i];
        write (fd, &c, 1) ;
    }
	(*env)->ReleaseByteArrayElements(env, data, body, 0);
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
  (JNIEnv *env, jobject obj, jint fd)
{
    return getAvailableByteCount(fd);
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
JNIEXPORT jbyteArray JNICALL Java_com_pi4j_jni_Serial_read
  (JNIEnv *env, jobject obj, jint fd, jint length)
{
    // determine result data array length from the number of bytes available on the receive buffer
    int availableBytes;
    availableBytes = getAvailableByteCount(fd);

    // check for error return; if error, then return NULL
    if (availableBytes < 0){
        return NULL;
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
            return NULL; // ERROR READING RX BUFFER
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
 * Method:    readAll
 * Signature: (I)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_pi4j_jni_Serial_readAll
  (JNIEnv *env, jobject obj, jint fd)
{
    // determine result data array length from the number of bytes available on the receive buffer
    int length;
    length = getAvailableByteCount(fd);

    // check for error return; if error, then return NULL
    if (length < 0){
        return NULL;
    }

    // create a new payload result byte array
    jbyte result[length];

    // copy the data bytes from the serial receive buffer into the payload result
    int i;
    for (i = 0; i < length; i++) {

        // read a single byte from the RX buffer
        uint8_t x ;
        if (read (fd, &x, 1) != 1){
            return NULL;   // ERROR READING RX BUFFER
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
 * Class:     com_pi4j_jni_Serial
 * Method:    echo
 * Signature: (IB)V
 */
JNIEXPORT void JNICALL Java_com_pi4j_jni_Serial_echo
(JNIEnv *env, jobject obj, jint fd, jbyte enabled)
{
    struct termios options ;

    // get and modify current options:
    tcgetattr (fd, &options) ;

    if(enabled <=0 ){
        // disable ECHO
        options.c_lflag &= ~ECHO;
    }
    else{
        // enable ECHO
        options.c_lflag |= ECHO;
    }

    // set updated options
    tcsetattr (fd, TCSANOW, &options) ;
}
