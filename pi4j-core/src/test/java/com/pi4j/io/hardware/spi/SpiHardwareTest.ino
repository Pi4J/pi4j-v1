/*
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Hardware Testing 
 * FILENAME      :  SpiHardwareTest.ino
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2021 Pi4J
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
 *
 *
 * @author Robert Savage
 * MOST OF THIS CODE WAS PROVIDED VIA: https://forum.arduino.cc/index.php?topic=558850.0
 *
 * This is a sample program to perform manual hardware testing of the SPI bus and data READ/WRITE operations.
 * The test will attempt to WRITE/READ 20K SPI data transfers between this arduino board and a RaspberryPi running Pi4J.  
 * This Arduino test code should be running with SPI pins (MISO/MOSI/SCLK/CE0) connected to the Raspberry Pi's SPI pins.
 * 
 * THIS CODE WAS TESTED ON AN ARDUINO DUE (3.3VDC) DEVICE
 * PLEASE NOTE THAT YOU SHOULD ONLY ATTEMPT TO CONNECT THE SPI BUS ON A RASPBERRY PI TO AN ARDUINO DEVICE BASED ON 3.3VDC.
 * USING A 5VDC ARDUINO COULD DAMAGE YOUR RASPBERRY PI.
 * 
 */

#include <SPI.h>

void setup() {
    Serial.begin(250000);
    while(!Serial);

    Serial.println("------------------------");
    Serial.println(" Pi4J SPI TEST PROGRAM  ");
    Serial.println("------------------------");

    //SPI serial recieve
    REG_PMC_PCER0 |= PMC_PCER0_PID24;       // Power up SPI clock
    REG_SPI0_WPMR = 0<<SPI_WPMR_WPEN;       //Unlock user interface for SPI

    //Instance SPI0, MISO: PA25, (MISO), MOSI: PA26, (MOSI), SCLK: PA27, (SCLK), NSS: PA28, (NPCS0)
    REG_PIOA_ABSR &= ~PIO_ABSR_P25;         // Transfer Pin control from PIO to SPI
    REG_PIOA_PDR |= PIO_PDR_P25;            // disable pio to control this pin (MOSI)

    REG_PIOA_ABSR &= ~PIO_ABSR_P26;         // Transfer Pin control from PIO to SPI
    REG_PIOA_PDR |= PIO_PDR_P26;            // disable pio to control this pin (MOSI)

    REG_PIOA_ABSR &= ~PIO_ABSR_P27;         // Transfer Pin control from PIO to SPI
    REG_PIOA_PDR |= PIO_PDR_P27;            // disable pio to control this pin (SCLK)

    REG_PIOA_ABSR &= ~PIO_ABSR_P28;         // Transfer Pin control from PIO to SPI
    REG_PIOA_PDR |= PIO_PDR_P28;            //disable pio to control this pin (NSS)

    REG_SPI0_CR = 1;                        // Enable SPI
    REG_SPI0_MR = 0;                        // Slave mode

    SPI0->SPI_IER = SPI_IER_RDRF;           // Receive Data Register Full Interrupt
    NVIC_EnableIRQ(SPI0_IRQn);              // setup interrupt callback

    SPI0->SPI_CSR[0] = SPI_CSR_NCPHA|SPI_CSR_BITS_8_BIT;    // Shift on falling edge and transfer 8 bits.
    
    Serial.println("READY TO COMMUNICATE VIA SPI");
}

bool flag=false; // flag indicating SPI data has been read
uint16_t rx;     // data recevied variable (only 1 byte is stored)

// SPI data received callback handler
void SPI0_Handler()
{
    // read SPI status register
    uint32_t status = SPI0->SPI_SR;
    
    // check SPI status register for data register read availability
    if (status & SPI_SR_RDRF){
        // read data from SPI receive register (data-in)
        rx = SPI0->SPI_RDR & SPI_RDR_RD_Msk;
        flag=true;
    }

    // check SPI status register for data register write availability
    if (status & SPI_SR_TDRE) {
        // write data to SPI transmit register (data-out)
        SPI0->SPI_TDR = (uint16_t)rx;
    }
}

void loop() {
    if(flag){
        flag=false;
        //Serial.print("0x");
        //Serial.print(rx, HEX);
        //Serial.print(", ");
    }
}