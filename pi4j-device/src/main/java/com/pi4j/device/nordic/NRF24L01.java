package com.pi4j.device.nordic;

import static com.pi4j.wiringpi.Gpio.INPUT;
import static com.pi4j.wiringpi.Gpio.OUTPUT;
import static com.pi4j.wiringpi.Gpio.PUD_UP;
import static com.pi4j.wiringpi.Gpio.digitalRead;
import static com.pi4j.wiringpi.Gpio.digitalWrite;
import static com.pi4j.wiringpi.Gpio.pinMode;
import static com.pi4j.wiringpi.Gpio.pullUpDnControl;
import static com.pi4j.wiringpi.Gpio.wiringPiSetup;
import static com.pi4j.wiringpi.GpioUtil.DIRECTION_IN;
import static com.pi4j.wiringpi.GpioUtil.DIRECTION_OUT;
import static com.pi4j.wiringpi.GpioUtil.export;
import static com.pi4j.wiringpi.GpioUtil.unexport;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Device Abstractions
 * FILENAME      :  NRF24L01.java
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
/**
 * NRF24L01.java is the driver for nodic nRF24L01 seriser 2.4G RF chip. see http://www.nordicsemi.com/eng/Products/2.4GHz-RF/nRF24L01P
 * it is simplex communication, but it abstract the chip as duplex. It owns one data listener for receiving.
 * this app mocked SPI bus for communication implementation.
 * 
 * this class is designed as singleton pattern since hardware is monopolized.
 *
 * <h1>Sample Usage</h1>
 * <pre>
 * NRF24L01 nrf=NRF24L01.getInstance();
 * nrf.start();//there will be thread daemon up, if your system does not have other user thread, setup one for dummy
 * nrf.setReceiverListener(.....);
 *
 * <br>somewhere to send
 * nrf.send(.....);
 *
 * <br>somewhere to shutdown
 * nrf.shutdown();
 * </pre>
 * 
 * RF/GPIO table:<br>
 * <ul>
 * <li>GPIO_21 LED bubble</li>
 * <li>GPIO_2 IRQ</li>
 * <li>GPIO_12 MISO</li>
 * <li>GPIO_11 CE</li>
 * <li>GPIO_13 MOSI</li>
 * <li>GPIO_14 SCLK</li>
 * <li>GPIO_0 CSN</li>
 * </ul>
 *
 * @author Alex maoanapex88@163.com
 */
public class NRF24L01 implements IRegister, Runnable {
	/**
	 * one led bubble, GPIO 21, if working, it will light up
	 */
	private static final int LED = 21;
	/**
	 * irq GPIO_2
	 */
	private static final int IRQ = 2;
	/**
	 * miso GPIO_12
	 */
	private static final int MISO = 12;
	/**
	 * ce GPIO_11
	 */
	private static final int CE = 11;
	/**
	 * mosi GPIO_13
	 */
	private static final int MOSI = 13;
	/**
	 * sclk GPIO_14
	 */
	private static final int SCLK = 14;
	/**
	 * csn GPIO_0
	 */
	private static final int CSN = 0;
	/**
	 * default data width 16, NRF support 32 in max
	 */
	private static final short RECEIVE_DATA_WIDTH = 16;
	/**
	 * default local listening chanel 96
	 */
	private int localRFChanel=96;
	/**
	 * default local RF address which is one byte length n 5
	 */
	private int[] localRFAddress={ 53, 69, 149, 231, 231 };
	/**
	 * one flag marking thread running
	 */
	private volatile boolean running=false;
	/**
	 * the IRQ watching thread handler
	 */
	private Thread irqWatchThread;
	/**
	 * the attached receive listener, it is empty listener by default.
	 * empty listener will only print received data byte array to STDOUT.
	 */
	private ReceiveListener listener=new EmptyReceiveListener();
	/**
	 * one blocking queue for send data, when sending data, data first will
	 * be queued and then wait NRF send period to pull data and send it out.
	 */
	private BlockingQueue<DataPackage> fifo=new LinkedBlockingQueue<DataPackage>(16);
	/**
	 * one thread pool for processing data listener, all received data callback will be invoked from
	 * thread and not thread safe.
	 */
	private ExecutorService executorService;
	static {
		wiringPiSetup();
	}
	/**
	 * the singleton instance
	 */
	private static final NRF24L01 nrf=new NRF24L01();
	/**
	 * @return return the singleton instance
	 */
	public static final NRF24L01 getInstance() {
		return nrf;
	}
	/**
	 * the default constructor will do steps as follows,
	 * <ul>
	 * <li>provision CSN as output</li>
	 * <li>provision SCLK as output</li>
	 * <li>provision MOSI as output</li>
	 * <li>provision CE as output</li>
	 * <li>provision MISO as output, then pull up resistor buit-in</li>
	 * <li>provision IRQ as INPUT, then pull up resistor buit-in</li>
	 * <li>provision LED as output, then pull up resistor buit-in</li>
	 * <li>do init and then go into listen mode</li>
	 * </ul>
	 */
	private NRF24L01() {
		/*CSN*/
		export(CSN, DIRECTION_OUT);
		pinMode(CSN, OUTPUT);

		/*SCLK*/
		export(SCLK, DIRECTION_OUT);
		pinMode(SCLK, OUTPUT);

		/*mosi*/
		export(MOSI, DIRECTION_OUT);
		pinMode(MOSI, OUTPUT);

		/*ce*/
		export(CE, DIRECTION_OUT);
		pinMode(CE, OUTPUT);

		/*miso*/
		export(MISO, DIRECTION_IN);
		pinMode(MISO, OUTPUT);
		pullUpDnControl(MISO, PUD_UP);

		/*irq*/
		export(IRQ, DIRECTION_IN);
		pinMode(MISO, INPUT);
		pullUpDnControl(IRQ, PUD_UP);

		/*LED light*/
		export(LED, DIRECTION_OUT);
		pinMode(LED, OUTPUT);
		pullUpDnControl(LED, PUD_UP);

		init();
		setRxMode(localRFChanel, 5, localRFAddress);
	}
	/**
	 * setRxMode is used to config RF channel and address via SPI command.
	 * it uses chanel 0 as receive address and same as send
	 * hardcode to 250kbps 0dBm, then enable RX_DR irq; block TX_DS+MAX_RT; enable CRC powerup; in receive mode
	 * @param rfChannel from 0-128
	 * @param addrWidth fixed to 5
	 * @param rxAddr one array length of 5 representing the address
	 */
	private final void setRxMode(int rfChannel, int addrWidth, int[] rxAddr) {
		digitalWrite(CE, 0);
		writeRegister((W_REGISTER+SETUP_AW), (addrWidth - 2)); // set address width
		writeBuffer((W_REGISTER+RX_ADDR_P0), rxAddr, addrWidth); // use channel 0, same address for receive
		writeRegister((W_REGISTER+RF_CH), rfChannel); // RF setup
		writeRegister((W_REGISTER+RX_PW_P0), RECEIVE_DATA_WIDTH); // channel 0 same data width
		writeRegister((W_REGISTER+RF_SETUP), 0x27); // inital 250Kbps, power 0dBm
		// (+22dBm with PA), LNA?
		writeRegister((W_REGISTER+STATUS), 0x7f); // clear RX_DR,TX_DS,MAX_RT flags
		writeRegister((W_REGISTER+CONFIG), 0x3f); // enable RX_DR irq, block TX_DS+MAX_RT, enable CRC powerup, in receive mode
	}

	/**
	 * send SPI command to init device and power up, clean up read/write buffer
	 * then light up LED bubble on GPIO_21
	 */
	private final void init() {
		digitalWrite(CE, 0);
		digitalWrite(CSN, 1);
		digitalWrite(SCLK, 0);

		writeRegister((W_REGISTER+EN_AA), 0x01);
		writeRegister((W_REGISTER+EN_RXADDR), 0x01); // enable channel 0
		writeRegister((W_REGISTER+SETUP_RETR), 0x1f ); // set auto retry delay 500us, retry 15 times
		writeRegister((W_REGISTER+STATUS), 0x7e); // clear RX_DR,TX_DS,MAX_RT flags
		writeRegister((W_REGISTER+CONFIG), 0x7e); // enable RX_DR irq, block TX_DS+MAX_RT, enable CRC powerup, in receive mode PTX

		flushTx();
		flushRx();

		digitalWrite(LED, 1);
	}
	/**
	 * write given byte data to SPI protocol, see https://en.wikipedia.org/wiki/SPI
	 * @param spiData
	 * @return 0 if succeed
	 */
	private final int spiReadWrite(int spiData) {
		for (int i = 0; i < 8; i++) {
			if ((0x80 & spiData) == 0x80) digitalWrite(MOSI, 1);
			else digitalWrite(MOSI, 0);

			spiData <<= 1;
			digitalWrite(SCLK, 1);

			if (digitalRead(MISO)==1) spiData |= 0x01;
			digitalWrite(SCLK, 0);
		}
		return spiData & 0xff;
	}
	
	private final int writeRegister(int regAddr, int writeData) {
		digitalWrite(CSN, 0);
		int val = spiReadWrite(regAddr);
		spiReadWrite(writeData);
		digitalWrite(CSN, 1);
		return val;
	}
	private final int readRegister(int regAddr) {
		digitalWrite(CSN, 0);
		spiReadWrite(regAddr);
		int val = spiReadWrite(0x00);
		digitalWrite(CSN, 1);
		return val;
	}

	private final int writeBuffer(int regAddr, int[] txData, int dataLen) {
		digitalWrite(CSN, 0);
		int val = spiReadWrite(regAddr);
		for (int i = 0; i < dataLen; i++) {
			spiReadWrite(txData[i]);
		}
		digitalWrite(CSN, 1);
		return val;

	}
	private final int readBuffer(int regAddr, int[] rxData, int dataLen) {
		digitalWrite(CSN, 0);
		int val = spiReadWrite(regAddr);
		for (int i = 0; i < dataLen; i++) {
			rxData[i] = spiReadWrite(0);
		}
		digitalWrite(CSN, 1);
		return val;
	}

	private final void flushRx() {
		digitalWrite(CSN, 0);
		spiReadWrite(FLUSH_RX);
		digitalWrite(CSN, 1);
	}

	private final void flushTx() {
		digitalWrite(CSN, 0);
		spiReadWrite(FLUSH_TX);
		digitalWrite(CSN, 1);
	}

	private final boolean isDataAvaid() {
		int status;
		if (digitalRead(IRQ) == 0) {
			status = readRegister(R_REGISTER + STATUS);
			if ((status & 0x40) == 0x40) {
				// read FIFO
				status = readRegister(R_REGISTER + FIFO_STATUS);
				if ((status & 0x01) == 0x01) {
					writeRegister(W_REGISTER + STATUS, 0x40);
					// clear FIFO
				} else {
					// RX FIFO is not null, data in FIFO
					return true;
				}
			}
		}
		return false;
	}
	
	private final int[] nrfGetOneDataPacket() {
		int[] dataBuffer = new int[RECEIVE_DATA_WIDTH];
		readBuffer(R_RX_PAYLOAD, dataBuffer, RECEIVE_DATA_WIDTH);
		return dataBuffer;
	}
	/**
	 * method to start the NRF main loop, you must call start after you get this class as singleton.
	 * duplicated calling of start will take no effect. there is one running flag to avoid duplicated start.
	 * 
	 * start method will start IRQ watch thread and listener thread pool.
	 */
	public final void start() {
		if(running) {
			System.err.println("It is already started, call start do nothing");
			return ;
		}
		running=true;

		irqWatchThread=new Thread(this, "NRF24L01+ Daemon");
		irqWatchThread.setPriority(Thread.MAX_PRIORITY);//in high priority
		irqWatchThread.setDaemon(true);//daemon
		irqWatchThread.start();
		executorService=Executors.newCachedThreadPool();
	}
	/**
	 * You must call shutdown explicitly when you shutdown your application. otherwise, hardware pins will
	 * be blocked.
	 * 
	 * shutdown will release all GPIO and unexport them.
	 */
	public final void shutdown() {
		running=false;
		executorService.shutdown();
		
		digitalWrite(LED, 0);
		
		digitalWrite(CSN, 0);
		digitalWrite(SCLK, 0);
		digitalWrite(MOSI, 0);
		digitalWrite(CE, 0);
		digitalWrite(MISO, 0);
		digitalWrite(IRQ, 0);
		digitalWrite(LED, 0);

		unexport(CSN);
		unexport(SCLK);
		unexport(MOSI);
		unexport(CE);
		unexport(MISO);
		unexport(IRQ);
		unexport(LED);
	}
	/**
	 * send is public interface to end user, send method does not really send data out, it will push
	 * data package to FIFO send queue,then waiting its period to send out.
	 * 
	 * @param rfChannel according to RF data sheet, can be value 0-127
	 * @param rfPower the power for send, it is enum data, can be 1, 2, 3
	 * @param maxRetry max retry times, it can be integer value less then 9
	 * @param addrWidth it is fixed to 5
	 * @param txAddr, one byte array of 5 which it RF address
	 * @param dataWidth your sending data width, i.e. txData width
	 * @param txData the byte array to send
	 */
	public final void send(int rfChannel, int rfPower, int maxRetry, int addrWidth, int[] txAddr, int dataWidth, int[] txData){
		fifo.add(new DataPackage(rfChannel, rfPower, maxRetry, addrWidth, txAddr, dataWidth, txData));
	}
	/**
	 * run is the core main loop of RF, it is one infinitely loop with one running flag.
	 * In loop body, it will check if any data valid, if valid, then will receive data and then
	 * pass to listener callback.
	 * 
	 * then loop will check sending block queue if any data, if any send data is queue, if will take one
	 * and the send it out.
	 * 
	 * at last step, it will switch back to listen mode
	 */
	public final void run() {
		while(running){
			/*receive*/
			while (isDataAvaid()) {
				final int[] data = nrfGetOneDataPacket();
				executorService.execute(new Runnable() {
					public void run() {
						listener.dataRecived(data);
					}
				});
			}

			/*read FIFO for data send*/
			try {
				DataPackage pkg = fifo.poll(25, TimeUnit.MILLISECONDS);
				if(pkg==null) continue;
				int retry = nrfSendData(pkg.chanel, pkg.power, pkg.maxRetry, pkg.addrWidth, pkg.txAddr, pkg.dataWidth, pkg.txData);
				pkg.sentTimestamp=System.currentTimeMillis();
				pkg.retry = retry;
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally{
				setRxMode(this.localRFChanel, 5, this.localRFAddress);
			}
		}
	}
	/**
	 * send data out
	 * @param rfChannel
	 * @param rfPower can be 1,2,3,4 enum  250Kbps+-18dBm, 250Kbps+-12dBm, 250Kbps+-6dBm, 250Kbps+0dBm
	 * @param maxRetry how many software retry, it is NOT chipset retry!
	 * @param addrWidth it is 5
	 * @param txAddr 5 bytes of RF data address
	 * @param dataWidth your data length
	 * @param txData your data payload
	 * @return result of send, >=253 for failed
	 */
	private final int nrfSendData(int rfChannel, int rfPower, int maxRetry, int addrWidth, int[] txAddr, int dataWidth, int[] txData) {
		int ret = 0;
		int retryCnt = 0;
		// check params
		if (rfChannel > 125 || rfPower == 0 || rfPower > 4 || maxRetry > 9 || addrWidth < 3 || addrWidth > 5 || dataWidth == 0 || dataWidth > 32) {
			return 253;
		}
		digitalWrite(CE, 0);
		writeRegister((W_REGISTER+SETUP_AW), (addrWidth - 2));
		writeBuffer((W_REGISTER+TX_ADDR), txAddr, addrWidth);
		writeBuffer((W_REGISTER+RX_ADDR_P0), txAddr, addrWidth);
		if (rfPower == 1){
			writeRegister((W_REGISTER+RF_SETUP), 0x21); // 250Kbps -18dBm
		}
		// (+7dBm with PA), LNA?
		else if (rfPower == 2) {
			writeRegister((W_REGISTER+RF_SETUP), 0x23); // 250Kbps -12dBm
		}
		// (+15dBm with PA), LNA?
		else if (rfPower == 3) {
			writeRegister((W_REGISTER+RF_SETUP), 0x25); // 250Kbps -6dBm
		}
		// (+20dBm with PA), LNA?
		else if (rfPower == 4) {
			writeRegister((W_REGISTER+RF_SETUP), 0x27); // 250Kbps 0dBm
		}
		// (+22dBm with PA),
		// LNA?
		writeRegister((W_REGISTER+RF_CH), rfChannel);
		writeRegister((W_REGISTER+STATUS), 0x7f); // clear RX_DR,TX_DS,MAX_RT flags
		writeRegister((W_REGISTER+CONFIG), 0x4e); // block RX_DR irq
		// irq TX_DS,MAX_RT，CRC enable PTX power on
		for (retryCnt = 0; retryCnt <= maxRetry; retryCnt++) {
			writeBuffer(W_TX_PAYLOAD, txData, dataWidth);//write
			digitalWrite(CE, 1);
			while (digitalRead(IRQ)==1) {

			}
			digitalWrite(CE, 0);// must wait IRQ then set CE!
			ret = checkSendStatus();
			if (ret <= 15) {
				ret += (16 * retryCnt);
				break;
			}
		}

		return ret;
	}
	private final int checkSendStatus() {
		int status = readRegister(R_REGISTER+STATUS);
		//LOGGER.debug("status={0}", status);
		if ((status & 0x20) == 0x20) {
			writeRegister((W_REGISTER + STATUS), 0x7f);
			return (readRegister(R_REGISTER + OBSERVE_TX) & 0x0f);
		}
		else if ((status & 0x10) == 0x10) {
			writeRegister((W_REGISTER + STATUS), 0x7f);
			flushTx();
			return 255;
		}
		else {
			return 252;
		}
	}
	/**
	 * set other chanel and address for this chip
	 * @param chanel
	 * @param addr
	 */
	public final void setLocalAddress(int chanel, int[] addr) {
		this.localRFChanel=chanel;
		this.localRFAddress=addr;
		setRxMode(localRFChanel, localRFAddress.length, localRFAddress);
	}
	/**
	 * set your business data listener
	 * @param l ReceiveListener which is callback function on data received
	 */
	public final void setReceiveListener(ReceiveListener l) {
		this.listener = l;
	}
	/**
	 * remove ReceiveListener, class will use EmptyReceiveListener which will only print byte recevied to stdout
	 */
	public final void removeReceiveListener(){
		this.listener = new EmptyReceiveListener();
	}
	/**
	 * one default data listener, it is empty, just print out bytes in console
	 */
	private final class EmptyReceiveListener implements ReceiveListener {
		private final DateFormat df=new SimpleDateFormat("hh:mm:ss - ");
		public void dataRecived(int[] data) {
			System.out.print(df.format(new Date()));
			for(int i=0;i<data.length;i++) {
				System.out.print(data[i]&0x00ff);
				System.out.print(", ");
			}
			System.out.println();
		}
	}
	/**
	 * the data package structure. DataPackage holds the data bytes will send out including
	 * its chanel, power, target address and etc. all also DataPackage will calculate the latency
	 */
	public final class DataPackage {
		public int retry;//result of times retry
		public final int chanel;//send to chanel
		public final int power; // power
		public final int maxRetry;//maxtry
		public final int addrWidth;
		public final int[] txAddr;
		public final int dataWidth;
		public final int[] txData;//payload
		public final long createTimestamp;//time to create data package
		public long sentTimestamp;//time send out

		public DataPackage(int chanel, int power, int maxRetry, int addrWidth, int[] txAddr, int dataWidth, int[] txData) {
			super();
			this.chanel = chanel;
			this.power = power;
			this.maxRetry = maxRetry;
			this.addrWidth = addrWidth;
			this.txAddr = txAddr;
			this.dataWidth = dataWidth;
			this.txData = txData;
			createTimestamp=System.currentTimeMillis();
		}
		/**
		 * the latency
		 * @return the latency the time escaped from data was queued to final send out
		 */
		public final int getLetency(){
			return (int)(sentTimestamp - createTimestamp);
		}
		/**
		 * @return　how many time of retry for sending data out.
		 */
		public final int getRetry(){
			return retry;
		}
	}
}
