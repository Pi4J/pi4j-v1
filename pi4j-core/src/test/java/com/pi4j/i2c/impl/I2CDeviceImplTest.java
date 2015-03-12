package com.pi4j.i2c.impl;

import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.pi4j.io.i2c.I2CBus;

public class I2CDeviceImplTest {

	private static final int[] counterForGivenAnswers = new int[] { 0 };
	private static final int COUNTER_FOR_READANSWER_INDEX = 0;
	
	private static final Answer<Integer> blockingReadAnswer =
			new Answer<Integer>() {
				
				@Override
				public Integer answer(final InvocationOnMock invocation) throws Throwable {
					
					int remainingTime = 5000;
					while (remainingTime > 0) {
						
						final long start = System.currentTimeMillis();
						try {
							Thread.sleep(remainingTime);
						} catch (InterruptedException e) {
							// never mind
						}
						remainingTime -= System.currentTimeMillis() - start;
						
					}
					
					return 1;
					
				}
			};
			
	@Test
	public void testI2CDeviceImpl() throws IOException {
		
		
		
	}
	
}
