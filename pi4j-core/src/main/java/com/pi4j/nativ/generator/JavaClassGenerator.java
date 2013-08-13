package com.pi4j.nativ.generator;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  JavaClassGenerator.java  
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * <p>
 * This class will generate a java file out of a .h-header-file. This java file
 * can then be used as an input for the javah execution. The class that will be
 * generated will have the same name as the filename of the input file (first
 * character will be uppercased). The signature of all methods that are flagged
 * with "extern" keyword will be converted to their Java counterpart.
 * </p>
 * 
 * @author Stijn Deknudt
 */
public class JavaClassGenerator {
	public static void main(String[] args) throws IOException {
		if (args.length != 3) {
			System.out
					.println(String
							.format("Usage: java %s <headerfile> <package> <output-base-directory>",
									JavaClassGenerator.class.getName()));
			System.out.println("headerfile = path + filename of .h-file");
			System.out
					.println("package = the package to generate the output file");
			System.out
					.println("output-base-directory = the base directory where to put the generated java file (the file will be put in the package structure under this base directory)");
			System.out
					.println(String
							.format("Example: java -cp ./target/classes/ %s mcp23017 com.pi4j.wiringpi ./src/main/java/com/pi4j/wiringPi",
									JavaClassGenerator.class.getName()));
		} else {
			String headerFileString = args[0];
			File headerFile = new File(headerFileString);
			String headerFilename = headerFile.getName().substring(0,
					headerFile.getName().indexOf(".h"));
			String packageName = args[1];
			String outputBaseDirectory = args[2];
			String outputDirectory = outputBaseDirectory + File.separator
					+ packageName.replaceAll("\\.", File.separator);

			String className = headerFilename.substring(0, 1).toUpperCase()
					+ headerFilename.substring(1);

			// First part of the output file
			StringBuffer outputBuffer = new StringBuffer(
					String.format(
							"package %s;\n\nimport com.pi4j.util.NativeLibraryLoader;\n\n"
									+ "/**\n * This class was generated with the com.pi4j.nativ.generator.JavaClassGenerator. Please don't change this class manually\n */\n"
									+ "public class %s {\n\n"
									+ "    // private constructor\n    private %s() {\n        // forbid object construction\n    }\n\n"
									+ "    static {\n        // Load the platform library\n        NativeLibraryLoader.load(\"pi4j\", \"libpi4j.so\");\n    }\n\n",
							packageName, className, className));

			Scanner scanner = new Scanner(headerFile);
			boolean skipBlock = false;
			boolean functionDefinition = false;
			while (scanner.hasNextLine()) {
				String nextLine = (String) scanner.nextLine();
				// Skip comment and definition blocks
				if (nextLine.trim().startsWith("/*")
						|| nextLine.trim().startsWith("#ifdef")) {
					skipBlock = true;
					continue;
				}
				if (nextLine.trim().endsWith("*/")
						|| nextLine.trim().endsWith("#endif")) {
					skipBlock = false;
					continue;
				}
				if (skipBlock) {
					continue;
				}
				// skip empty lines
				if (nextLine.trim().equals("")) {
					continue;
				}
				// Strip comments on the same line
				String nextLineWithoutComments = nextLine.substring(0,
						(nextLine.indexOf("//") > 0) ? nextLine.indexOf("//")
								: nextLine.length());
				// Try to find the functions
				if (nextLineWithoutComments.contains("extern")) {
					functionDefinition = true;
					outputBuffer.append("\n    public static native");
				}
				if (functionDefinition) {
					StringTokenizer stringTokenizer = new StringTokenizer(
							nextLineWithoutComments);
					while (stringTokenizer.hasMoreTokens()
							&& functionDefinition) {
						String nextToken = stringTokenizer.nextToken();
						if (nextToken.endsWith("char")) {
							String afterNextToken = stringTokenizer.nextToken();
							if (afterNextToken.startsWith("*")) {
								nextToken = nextToken.replaceAll("char", "String ") + afterNextToken;
							} else {
								nextToken = nextToken + " " + afterNextToken;
							}
						}
						nextToken = nextToken.replaceAll("const", "");
						nextToken = nextToken.replaceAll("\\*", "");
						nextToken = nextToken.replaceAll("\\.\\.\\.", "String... args");
						nextToken = nextToken.replaceAll("uint8_t", "byte");
						nextToken = nextToken.replaceAll("\\[.+\\]", "[]");
						nextToken = nextToken.replace("unsigned", "");
						if (!nextToken.equals("extern")) {
							if (!nextToken.equals(",")
									&& !nextToken.equals(")")
									&& !nextToken.equals("")
									&& !nextToken.equals("(")) {
								outputBuffer.append(" ");
							}
							outputBuffer.append(nextToken);
						}
						if (nextToken.endsWith(";")) {
							functionDefinition = false;
						}
					}
				}

			}
			outputBuffer.append("\n}");

			File outputDirectoryFile = new File(outputDirectory);
			if (!outputDirectoryFile.exists()) {
				outputDirectoryFile.mkdirs();
			}
			BufferedWriter outputFile = new BufferedWriter(new FileWriter(
					outputDirectory + File.separator + className + ".java"));
			outputFile.write(outputBuffer.toString());
			outputFile.close();
			scanner.close();
		}
	}
}
