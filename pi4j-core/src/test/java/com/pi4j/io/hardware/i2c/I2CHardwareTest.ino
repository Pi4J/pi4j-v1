/*
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Hardware Testing
 * FILENAME      :  I2CHardwareTest.ino
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
 *
 * This is a sample program to perform manual hardware testing of the I2C bus and data READ/WRITE operations.
 * The test will attempt to WRITE to 255 registers on TEST device #99 on I2C BUS #1.  This Arduino test code 
 * should be running with I2C pins (SDA/SCL) connected to the Raspberry Pi's I2C bus.
 * 
 * THIS CODE WAS TESTED ON AN ARDUINO DUE (3.3VDC) DEVICE
 * PLEASE NOTE THAT YOU SHOULD ONLY ATTEMPT TO CONNECT THE I2C BUS ON A RASPBERRY PI TO AN ARDUINO DEVICE BASED ON 3.3VDC.
 * USING A 5VDC ARDUINO COULD DAMAGE YOUR RASPBERRY PI.
 * 
 */

#include <Wire.h>

#define I2C_DEVICE 99

struct I2cRegister {
    uint8_t data[32] = { 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0 };
    uint16_t length = 0;
};


struct I2cCache {
    I2cRegister reg[256];
    uint8_t address = 0;
    TwoWire* wire;
    void reset(){
        address = 0;
        wire = nullptr;
        for(int i = 0; i < 256; i++){
            reg[i].length = 0;            
            memset(reg[i].data, 0, sizeof reg[i].data);
        }
    }
};

I2cCache i2cCache;


// callback for received data
void receiveI2CData(int byteCount){
    if(byteCount == 0) return; // ignore any zero byte callbacks
    uint8_t address = i2cCache.wire->read();
    uint8_t length = byteCount - 1; // substract for address byte

    // update active register address
    i2cCache.address = address;

    Serial.print("--> I2C <REGISTER:");
    Serial.print(address);
    Serial.print("> RECEIVE [");
    Serial.print(length);
    Serial.print("] BYTES");
    if(length == 0){
        Serial.print("; <--GET-->");
    } else {
        Serial.print("; <--SET-->");
    }
    Serial.println();

    // if a data payload is included, then we need to cache
    // the value in the  register's data store
    if(length > 0){

        // clear register data store
        memset(i2cCache.reg[address].data, 0, sizeof i2cCache.reg[address].data);

        // update register data length
        i2cCache.reg[address].length = length;

        // read all available bytes from the I2C bus into the register data store
        i2cCache.wire->readBytes(i2cCache.reg[address].data, length);
    }
}

// callback for sending data
void sendI2CData(){
    uint8_t address = i2cCache.address;
    uint8_t length = i2cCache.reg[address].length;
    i2cCache.wire->write(i2cCache.reg[address].data, length);    
    
    // debug
    // Serial.print("--> I2C <REGISTER:");
    // Serial.print(address);
    // Serial.print("> SEND [");
    // Serial.print(length);
    // Serial.println("] BYTES");    
}

void setup() {
    Serial.begin(250000);
    while(!Serial);

    // end I2C on existing bus if previously assigned
    if(i2cCache.wire != nullptr){
        i2cCache.wire->end();
    }

    // reset I2C cache
    i2cCache.reset();

    // setup which I2C bus to enable
    i2cCache.wire = &Wire;   // setup I2C BUS 0
    //i2cCache.wire = &Wire1;  // setup I2C BUS 1

    // initialize i2c as slave
    i2cCache.wire->begin(I2C_DEVICE);

    // define callbacks for i2c communication
    i2cCache.wire->onReceive(receiveI2CData);
    i2cCache.wire->onRequest(sendI2CData);

    Serial.println("-------------------------------------");
    Serial.println(" Pi4J I2C TEST PROGRAM (DEVICE #99)  ");
    Serial.println("-------------------------------------");
}

void loop() {

}