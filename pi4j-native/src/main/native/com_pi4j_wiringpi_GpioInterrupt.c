/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: JNI Native Library
 * FILENAME      :  com_pi4j_wiringpi_GpioInterrupt.c  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2016 Pi4J
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
#include <stdio.h>
#include <stdint.h>
#include <stdarg.h>
#include <stdlib.h>
#include <poll.h>
#include <fcntl.h>
#include <jni.h>
#include <string.h>
#include <pthread.h>
#include <termios.h>
#include <unistd.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include "com_pi4j_wiringpi_GpioPin.h"
#include "com_pi4j_wiringpi_GpioInterrupt.h"

// constants
#define GPIO_FN_MAXLEN  32
#define GPIO_RDBUF_LEN       5

// pollfds for polling
struct pollfd pfds[MAX_GPIO_PINS];

/*
 * ------------------------------------------------------------------
 * PREPARE PIN FOR STATE CHANGE POLLING
 * ------------------------------------------------------------------
 * Class:     com_pi4j_wiringpi_GpioInterrupt
 * Method:    initPoll
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_GpioInterrupt_initPoll
(JNIEnv *env, jclass class, jint pin)
{
    // get the index position for the requested pin number
    int index = pin;
    int edgePin = getEdgePin(index);
    int fd;
    char fn[GPIO_FN_MAXLEN];
  
    // ensure that the requested pin index is valid
    if(index < 0 && edgePin < 0)
    {
        return -1;
    }
    else
    {
        if (pfds[index].fd == 0) 
        {
            // open the file
            memset(fn, 0x00, GPIO_FN_MAXLEN);
            snprintf(fn, GPIO_FN_MAXLEN-1, "/sys/class/gpio/gpio%d/value", edgePin);
            fd=open(fn, O_RDONLY);
            if(fd<0)
            {
                // return error; unable to get file descriptor
                // (this is likely because the pin has not been exported)
                perror(fn);
                return -1;
            }

            // set polling config structure
            pfds[index].fd=fd;
            pfds[index].events=POLLPRI; // High priority data may be read.

            return 1;
        }
        else
        {
            return 0;
        }
    }
}

/*
 * Class:     com_pi4j_wiringpi_GpioInterrupt
 * Method:    closePoll
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_GpioInterrupt_closePoll
(JNIEnv *env, jclass class, jint pin)
{
    int index = pin;
  
    if (index >= 0 && index < MAX_GPIO_PINS)
    {
      
        if (pfds[index].fd > 0)
        {
            close(pfds[index].fd);
            pfds[index].fd = 0;
            return 1;
        }
        else
        {
            return 0;
        }
    }
    else
    {
        return -1;
    }
}

/*
 * ------------------------------------------------------------------
 * LISTEN FOR PIN STATE CHANGES (polls the pin and returns new value)
 * ------------------------------------------------------------------
 * Class:     com_pi4j_wiringpi_GpioInterrupt
 * Method:    pollPinStateChange
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_com_pi4j_wiringpi_GpioInterrupt_pollPinStateChange
(JNIEnv *env, jclass class, jint pin, jint previousValue, jint pollTimeout)
{
    // get the index position for the requested pin number
    int index = pin;

    struct pollfd pfd;
    int ret,nextValue;
    char rdbuf[GPIO_RDBUF_LEN];
  
    // ensure that the requested pin index is valid and initialzed for poll
    if (index < 0 || index > MAX_GPIO_PINS || pfds[index].fd <= 0)
    {
        return -1;
    }
    else
    {
        pfd = pfds[index];

        // seek to the fist position in the data file
        lseek(pfd.fd, 0, SEEK_SET);

        // attempt to read the pin state from the linux sysfs
        memset(rdbuf, 0x00, GPIO_RDBUF_LEN);
        ret=read(pfd.fd, rdbuf, GPIO_RDBUF_LEN-1);
        if(ret<0)
        {
            // return error; unable to read the data file
            // (this is likely because the user has insufficient permissions)
            perror("read()");
            return -1;
        }

        // initialize last known value and cache the value as the last known state
        nextValue = strncmp(rdbuf, "1", 1) == 0 ? 1 : 0; // only compare the first character; rdbuff may have more junk chars
    
        if (nextValue != previousValue) {
            //printf("changed already %s\n", rdbuf);

            return nextValue;
        }
     
        // continuous thread loop
        for(;;)
        {
            // clear/reset the data buffer
            memset(rdbuf, 0x00, GPIO_RDBUF_LEN);

            // seek to the fist position in the data file
            lseek(pfd.fd, 0, SEEK_SET);

            // wait for data to be written to the GPIO value file
            // (timeout every pollTimeout ms)
            ret=poll(&pfd, 1, pollTimeout);

            // if the return value is less than '0' then
            // an error was thrown; bail out of the thread
            if (ret<0)
            {
                perror("poll()");
                close(pfd.fd);
                return -1;
            }

            // if the return value is equal to '0' then
            // the polling simply timed out and we can return the previousValue
            else if (ret==0)
            {
                //printf("timeout\n");

                return previousValue;
            }

            // if the return value is greater than '0' then
            // a change to the GPIO data file occurred
            else
            {
                // read the data from the file into the data buffer
                memset(rdbuf, 0x00, GPIO_RDBUF_LEN);
                ret=read(pfd.fd, rdbuf, GPIO_RDBUF_LEN-1);
                if (ret<0)
                {
                    // data read error
                    perror("read()");
                    continue;
                }

                // printf("interrupt, value is: %s\n", rdbuf);

                // compare the data in the data buffer with the last known value state
                nextValue = strncmp(rdbuf, "1", 1) ? 1 : 0; // only compare the first character; rdbuff may have more junk chars
                if (nextValue != previousValue)
                {
                    return nextValue;
                } else
                {
                    continue;
                }
            }
        }
    }
}
