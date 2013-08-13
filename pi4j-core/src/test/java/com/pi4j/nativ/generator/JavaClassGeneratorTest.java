package com.pi4j.nativ.generator;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  JavaClassGeneratorTest.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2013 Pi4J
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

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class JavaClassGeneratorTest {

	@Test
	public void testMain() throws Exception {
		String inputFileString = JavaClassGeneratorTest.class.getResource("JavaClassGeneratorTest-inputfile.h").getPath();
		File inputFile = new File(inputFileString);
		String[] arguments = {inputFileString, "com.pi4j.wiringpi", inputFile.getParent()};
		JavaClassGenerator.main(arguments);
		List<String> expectedLines = getFileContents("JavaClassGeneratorTest-expectedfile.java");
		List<String> outputLines = getFileContents("com/pi4j/wiringpi/JavaClassGeneratorTest-inputfile.java");
		Iterator<String> outputLinesIterator = outputLines.iterator();
		for (String expectedLine : expectedLines) {
			String outputLine = outputLinesIterator.next();
			assertEquals(expectedLine, outputLine);
		}
	}
	
	private List<String> getFileContents(String filename) throws IOException {
		String fileString = JavaClassGeneratorTest.class.getResource(filename).getFile();
		File file = new File(fileString);
		return Files.readAllLines(file.toPath(), Charset.defaultCharset());
	}
}
