/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: GPIO Extension
 * FILENAME      :  OlimexAvrIoM16-Pi4JGpioExtension.c  
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
/*
 * ---------------------------------------------------------------------------------
 * OlimexAvrIoM16_Pi4JGpioExtension.c
 *
 * Created: 10/13/2012 6:01:40 PM
 * Author: Robert Savage
 *
 * PRODUCT WEB PAGE:
 * https://www.olimex.com/Products/AVR/Development/AVR-IO-M16/
 * ---------------------------------------------------------------------------------
 *
 * FEEDBACK:
 *
 * STATUS UPDATES ARE BROADCAST USING THE FORMAT '$XX' WHERE XX IS THE HEXADECIMAL 
 * VALUE REPRESENTED IN ASCII CHARACTERS. (00 to FF)
 * 
 * The hexadecimal value is a bitmask of the current states of the relays and inputs.
 * The chart below describes the value of each relay and input for each bit of the 
 * byte value.
 * ---------------------------------------------------------------------------------
 * |    1    |    2    |    3    |    4    |    5    |    6    |    7    |    8    |   
 * ---------------------------------------------------------------------------------
 * | RELAY-1 | RELAY-2 | RELAY-3 | RELAY-4 | INPUT-1 | INPUT-2 | INPUT-3 | INPUT-4 |    
 * ---------------------------------------------------------------------------------
 * 
 * COMMANDS:
 *
 *   '?'  -  This command will immediately return the current state value as shown above.
 *   'v'  -  This command will immediately return the program name and version information.
 *   '+X' -  This command will turn ON a relay by number.  The 'X' represents the relay number (1-4).
 *   '-X' -  This command will turn OFF a relay by number.  The 'X' represents the relay number (1-4).
 *   '=X' -  This command will control the ON/OFF states of the relays.  
 *           The 'X' represents the ASCII hexadecimal byte value of the relay states (0-F).  
 *           The relay bits are the four lower bits in the byte and thus this ASCII value
 *           should be only a single character.
 *
 *           EXAMPLES:
 *           ---------
 *            '=F' turns on all four relays
 *            '=0' turns off all four relays
 *            '=1' turns on relay 1 and off all other relays
 *            '=3' turns on relays 1 and 2 and off relays 3 and 4.
 */ 

#include <avr/io.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>

#define	__AVR_ATmega16__	1
#define OSCSPEED	16000000		/* in Hz */

char Txt[80];

const char FORCE_UPDATE = 1;
const char UPDATE_ONLY_ON_CHANGE = 0;
unsigned char lastKnownInputState = 255;

void PORT_Init()
{
	PORTA = 0b00000000;
	DDRA = 0b00000000;

	PORTB = 0b00000000;
	DDRB = 0b00001111;		//set Relay as output (Bit0:3 = 1)

	PORTC = 0b00000000;
	DDRC = 0b00000000;

	PORTD = 0b00000000;     //set POWER LED to LOW (Bit8 = 0)	
	DDRD = 0b10000010;		//set TX as output (Bit1 = 1)
	                        //set POWER LED to output (Bit8 = 1)
}

/*************************************** U A R T *****************************************/

void UART_Init(uint32_t Baud)
{
	unsigned int BaudRate = OSCSPEED / (16 * Baud) - 1;		//calculate BaudRate from page 145

	//set BaudRate into registers
	UBRRH = (unsigned char) BaudRate>>8;
	UBRRL = (unsigned char) BaudRate;
	
	UCSRB = UCSRB | 0b00011000;		// RXEN & TXEN enable (Bits 4, 3 = 1)
	UCSRC = UCSRC | 0b10000110;		// set Frame Format 8 bits per frame (Bit1 = 1, Bit2 = 1), 1 stop bit(Bit3 =0)
}

unsigned char UART_Receive()
{
	if (UCSRA & 0b10000000)
		return UDR;
	else
		return 0;
}

void UART_Transmit(unsigned char Data)
{
	while (!(UCSRA & 0b00100000));
	UDR = Data;
}

/*************************************** W R I T E *****************************************/

//return Length of string
unsigned char Length(char Temp[80])
{
	unsigned char L=0;
	while (Temp[L]) L++;
	return L;
}

//print text
void Print(char Text[80])
{
	unsigned char Len, i, T;
	Len = Length(Text);
	for (i=0; i<Len; i++) 
	{
		T = Text[i];
		UART_Transmit(T);
	}
	strcpy(Text, "");
}

//print text and new line
void PrintLn(char Text[80])
{
	unsigned char Len, i, T;
	Len = Length(Text);
	for (i=0; i<Len; i++) 
	{
		T = Text[i];
		UART_Transmit(T);
	}
	strcpy(Text, "");
	UART_Transmit(13);
	UART_Transmit(10);
}

//print new line
void PrintLine()
{
	UART_Transmit(13);
	UART_Transmit(10);
}

//print welcome message
void PrintWelcome()
{
	PrintLine();
	strcpy(Txt, "*************************"); PrintLn(Txt);
	strcpy(Txt, "*  Pi4J GPIO Extension  *"); PrintLn(Txt);
	strcpy(Txt, "*  -OLIMEX AVR IO M16-  *"); PrintLn(Txt);
	strcpy(Txt, "*     VERSION 1.0.0     *"); PrintLn(Txt);
	strcpy(Txt, "*************************"); PrintLn(Txt);
	strcpy(Txt, "*  (C) 2012,  Pi4J.COM  *"); PrintLn(Txt);
	strcpy(Txt, "*************************"); PrintLn(Txt);
}

//print error message
void PrintError()
{
	PrintLine();
	strcpy(Txt, "ERROR!");
	PrintLn(Txt);
	PrintLine();
}

/******************************** S T A T U S *****************************/

void PrintStatus(unsigned char T)
{
	// convert the value to HEX value in ASCII characters
    unsigned char hex[2]={'0','0'};
    unsigned char hexArray[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    int i=1;

	while(T>0)
	{
		hex[i]=T%16;
		hex[i]=hexArray[hex[i]];
		T=floor(T/16);
		i--;            
	}
		
	UART_Transmit('$');	//output value of T
	UART_Transmit(hex[0]);	
	UART_Transmit(hex[1]);	
	PrintLine();
}

void EvaluateStatus(unsigned char force)
{
	unsigned char T=0;

	//calculating the state value 

	// input pins	
	if (!(PINA & 0b00000100))	T = T + 128;
	if (!(PINA & 0b00000010))	T = T + 64;
	if (!(PINA & 0b00000001))	T = T + 32;
	if (!(PIND & 0b00000100))	T = T + 16;

	// output relays
	if ((PINB & 0b00000001))	T = T + 8;
	if ((PINB & 0b00000010))	T = T + 4;
	if ((PINB & 0b00000100))	T = T + 2;
	if ((PINB & 0b00001000))	T = T + 1;
	
	// if the force flag is set or the value has 
	// changed from the last known state, then
	// update the last known state tracking 
	// variable and print the updated state
	// to the serial connection
	if(force || lastKnownInputState != T)
	{
		lastKnownInputState = T;
		PrintStatus(T);
	}	

	lastKnownInputState = T;
}


/******************************** C O N T R O L *****************************/

void TurnOffRelays()
{
	unsigned char T = 0;
	
	// wait for command input
	while (!(T))
	{
		T = UART_Receive();
	}	
	
	if(T == '4')
		PORTB = PORTB & 0b11111110;
	else if(T == '3')
		PORTB = PORTB & 0b11111101;
	else if(T == '2')	
		PORTB = PORTB & 0b11111011;
	else if(T == '1')
		PORTB = PORTB & 0b11110111;

	EvaluateStatus(FORCE_UPDATE);
}


void TurnOnRelays()
{
	unsigned char T = 0;
	
	// wait for command input
	while (!(T))
	{
		T = UART_Receive();
	}	
	
	if(T == '4')
		PORTB = PORTB | 0b00000001;
	else if(T == '3')
		PORTB = PORTB | 0b00000010;
	else if(T == '2')	
		PORTB = PORTB | 0b00000100;
	else if(T == '1')
		PORTB = PORTB | 0b00001000;
	
	EvaluateStatus(FORCE_UPDATE);
}

void ControlRelays()
{
	unsigned char T=0, T1, Br, Arr[5], Bl = 1;
	for (Br=0; Br<5; Br++) Arr[Br] = 48; //null the elements of the array (Arr[Br] = '0')
	Br = 0;

	while ((T != 10) & (T != 13) & (Br < 1) & Bl)
	{
		T = UART_Receive();
		if ((T != 0) & (T != 10) & (T != 13) & Bl)	
		{
			Br++;
			T1 = T;		//keeping value of T (T has dynamic value (ENTER))
			if ((T1 < 48) | ((T1>57) & (T1<65)) | (T1 > 70)) //if T1(T) isn't 0-9 or A-F
				Bl = 0;
		}
	}

	T = T1;		//T back its value

	if ((!Bl) | (Br == 2))	//check for incorrect input: Length>2 or signes except 0-9 or A-F
	{
		PrintError();
	}		
	else
	{			
		Br = 0;		//null Br
		if (T < 58) 		//T [0..9] (0 - 48, 9 - 57)
			T = T - 48;
		else				//T [10..15] (A - 65, F - 70)
			T = T - 55;

		//transmit T in binary and keep its value in array
		while (T)
		{
			Arr[Br] = T % 2 + 48;
			Br++;
			T = T / 2;
		}

		//check value of each sign and output the relays
		if (Arr[3] == 49) 
			PORTB = PORTB | 0b00000001;
		else
			PORTB = PORTB & 0b11111110;
		if (Arr[2] == 49) 
			PORTB = PORTB | 0b00000010;
		else
			PORTB = PORTB & 0b11111101;
		if (Arr[1] == 49) 
			PORTB = PORTB | 0b00000100;
		else
			PORTB = PORTB & 0b11111011;
		if (Arr[0] == 49) 
			PORTB = PORTB | 0b00001000;
		else
			PORTB = PORTB & 0b11110111;
			
		EvaluateStatus(FORCE_UPDATE);
	}
}

/******************************** C O M M A N D S *****************************/

// determine command by header character
void AcceptCommands()
{
	unsigned char Temp = 0;
	
	// wait for command input
	while (!(Temp))
	{
		Temp = UART_Receive();
		EvaluateStatus(UPDATE_ONLY_ON_CHANGE);
	}		

	// process command input by inspecting header character
	if (Temp == '?') 
	{
		EvaluateStatus(FORCE_UPDATE);
	}				
	else if (Temp == '=') 
	{
		ControlRelays(); 
	}		
	else if (Temp == '+') 
	{
		TurnOnRelays(); 
	}		
	else if (Temp == '-') 
	{
		TurnOffRelays(); 
	}		
	else if (Temp == 'v') 
	{
		PrintWelcome(); 
	}		
	else
	{
		// unknown command
		PrintError();
	}
}

/*********************************** M A I N ********************************/

int main()
{
	PORT_Init();
	UART_Init(19200);
	while (1)
	{
		PrintWelcome();

		while (1)
		{
			AcceptCommands();
			EvaluateStatus(UPDATE_ONLY_ON_CHANGE);
		}			
	}
	return 0;
}
