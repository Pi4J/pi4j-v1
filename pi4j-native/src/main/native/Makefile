#
# Makefile:
#	libpi4j - Java (JNI) library wrapper for WiringPi for the Raspberry Pi
#

TARGET=libpi4j.so

#DEBUG	= -g -O0
DEBUG	= -O3
CC	= gcc
INCLUDE	= -I/usr/lib/jvm/jdk-7-oracle-armhf/include -I/usr/lib/jvm/jdk-7-oracle-armhf/include/linux -I/usr/local/include -I/usr/local/include/linux
CFLAGS	= $(DEBUG) -Wall $(INCLUDE) -Winline -pipe $(LIBS) 

LIBS    =  -L/usr/local/lib /usr/local/lib/libwiringPi.a /usr/local/lib/libwiringPiDev.a

# Should not alter anything below this line
###############################################################################

SRC	=   com_pi4j_jni_Exception.c      \
        com_pi4j_jni_I2C.c            \
	    com_pi4j_jni_Serial.c         \
	    com_pi4j_jni_SerialInterrupt.c\
	    com_pi4j_wiringpi_Gpio.c      \
        com_pi4j_wiringpi_Serial.c    \
        com_pi4j_wiringpi_I2C.c       \
        com_pi4j_wiringpi_Lcd.c       \
        com_pi4j_wiringpi_Shift.c     \
        com_pi4j_wiringpi_SoftPwm.c   \
        com_pi4j_wiringpi_SoftTone.c  \
        com_pi4j_wiringpi_Spi.c       \
        com_pi4j_wiringpi_Gertboard.c \
        com_pi4j_wiringpi_Nes.c       \
        com_pi4j_wiringpi_GpioPin.c   \
        com_pi4j_wiringpi_GpioUtil.c  \
        com_pi4j_wiringpi_GpioInterrupt.c \
        com_pi4j_jni_Loader.c

OBJ	=   com_pi4j_jni_Exception.o      \
        com_pi4j_jni_I2C.o            \
	    com_pi4j_jni_Serial.o         \
	    com_pi4j_jni_SerialInterrupt.o\
	    com_pi4j_wiringpi_Gpio.o      \
        com_pi4j_wiringpi_Serial.o    \
        com_pi4j_wiringpi_I2C.o       \
        com_pi4j_wiringpi_Lcd.o       \
        com_pi4j_wiringpi_Shift.o     \
        com_pi4j_wiringpi_SoftPwm.o   \
        com_pi4j_wiringpi_SoftTone.o  \
        com_pi4j_wiringpi_Spi.o       \
        com_pi4j_wiringpi_Gertboard.o \
        com_pi4j_wiringpi_Nes.o       \
        com_pi4j_wiringpi_GpioPin.o   \
        com_pi4j_wiringpi_GpioUtil.o  \
        com_pi4j_wiringpi_GpioInterrupt.o \
        com_pi4j_jni_Loader.o

all:		$(TARGET)

$(TARGET):	$(OBJ)
	@echo [LINK] $(OBJ)
	@$(CC) $(OBJ) -shared -o $(TARGET) $(INCLUDE) $(LIBS)
	@size   $(TARGET)

.c.o:
	@echo [COMPILE] "$(CC) -c $(CFLAGS)" $<
	@$(CC) -c $(CFLAGS) $< -o $@

clean:
	rm -f $(OBJ) $(TARGET) *~ core tags Makefile.bak

tags:	$(SRC)
	@echo [ctags]
	@ctags $(SRC)

depend:
	makedepend -Y $(SRC)

install:	$(TARGET)
	@echo [install]
	install -m 0755 -d /usr/local/lib
	install -m 0755 -d /usr/local/include
	install -m 0644 $(TARGET)	/usr/local/lib
	# install -m 0644 pi4j.jar	/usr/share/java

uninstall:
	@echo [uninstall]
	rm -f /usr/local/lib/$(TARGET)
	# rm -f /usr/share/java/pi4j.jar


# DO NOT DELETE
com_pi4j_jni_Exception.o: com_pi4j_jni_Exception.h
com_pi4j_jni_I2C.o: com_pi4j_jni_I2C.h
com_pi4j_jni_Serial.o: com_pi4j_jni_Serial.h
com_pi4j_jni_SerialInterrupt.o: com_pi4j_jni_SerialInterrupt.h
com_pi4j_wiringpi_Gpio.o: com_pi4j_wiringpi_Gpio.h  
com_pi4j_wiringpi_GpioPin.o: com_pi4j_wiringpi_GpioPin.h
com_pi4j_wiringpi_GpioUtil.o: com_pi4j_wiringpi_GpioUtil.h
com_pi4j_wiringpi_GpioInterrupt.o: com_pi4j_wiringpi_GpioInterrupt.h
com_pi4j_wiringpi_Serial.o: com_pi4j_wiringpi_Serial.h
com_pi4j_wiringpi_Lcd.o: com_pi4j_wiringpi_Lcd.h
com_pi4j_wiringpi_Shift.o: com_pi4j_wiringpi_Shift.h
com_pi4j_wiringpi_SoftPwm.o: com_pi4j_wiringpi_SoftPwm.h
com_pi4j_wiringpi_SoftTone.o: com_pi4j_wiringpi_SoftTone.h
com_pi4j_wiringpi_Spi.o: com_pi4j_wiringpi_Spi.h
com_pi4j_wiringpi_Gertboard.o: com_pi4j_wiringpi_Gertboard.h
com_pi4j_wiringpi_Nes.o: com_pi4j_wiringpi_Nes.h
com_pi4j_jni_Loader.o: com_pi4j_jni_Loader.h
