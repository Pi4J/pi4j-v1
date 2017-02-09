package com.pi4j.util;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  NativeLibraryLoader.java
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NativeLibraryLoader {

	private static final Set<String> loadedLibraries = new TreeSet<>();
	private static final Logger logger = Logger.getLogger(NativeLibraryLoader.class.getName());
	private static boolean initialized;

	// private constructor
	private NativeLibraryLoader() {
		// forbid object construction
	}

	public static synchronized void load(String fileName) {
		// check for debug property; if found enable all logging levels
		if (!initialized) {
			initialized = true;
			if (System.getProperty("pi4j.debug") != null) {
				logger.setLevel(Level.ALL);
				try {
					// create an appending file handler
					FileHandler fileHandler = new FileHandler("pi4j.log");
					fileHandler.setLevel(Level.ALL);
					ConsoleHandler consoleHandler = new ConsoleHandler();
					consoleHandler.setLevel(Level.ALL);

					// add to the desired loggers
					logger.addHandler(fileHandler);
					logger.addHandler(consoleHandler);
				} catch (IOException e) {
					System.err.println("Unable to setup logging to debug. No logging will be done. Error: ");
					e.printStackTrace();
				}
			}
		}

		// first, make sure that this library has not already been previously loaded
		if (loadedLibraries.contains(fileName)) {
			logger.fine("Library [" + fileName + "] has already been loaded; no need to load again.");
			return;
		}

		loadedLibraries.add(fileName);

        //
        // path = /lib/{platform}/{linking:static|dynamic}/{filename}
        //
        String platform = System.getProperty("pi4j.platform", "raspberrypi");
        String linking = System.getProperty("pi4j.linking", "static");
		String path = "/lib/" + platform + "/" + linking + "/" + fileName;
		logger.fine("Attempting to load [" + fileName + "] using path: [" + path + "]");
		try {
			loadLibraryFromClasspath(path);
			logger.fine("Library [" + fileName + "] loaded successfully using embedded resource file: [" + path + "]");
		} catch (Exception | UnsatisfiedLinkError e) {
			logger.log(Level.SEVERE, "Unable to load [" + fileName + "] using path: [" + path + "]", e);
			// either way, we did what we could, no need to remove now the library from the loaded libraries since we run inside one VM and nothing could possibly change, so there is no point in
			// trying out this logic again
		}
	}

	/**
	 * Loads library from classpath
	 *
	 * The file from classpath is copied into system temporary directory and then loaded. The temporary file is deleted after exiting. Method uses String as filename because the pathname is
	 * "abstract", not system-dependent.
	 *
	 * @param path
	 *            The file path in classpath as an absolute path, e.g. /package/File.ext (could be inside jar)
	 * @throws IOException
	 *             If temporary file creation or read/write operation fails
	 * @throws IllegalArgumentException
	 *             If source file (param path) does not exist
	 * @throws IllegalArgumentException
	 *             If the path is not absolute or if the filename is shorter than three characters (restriction of {@see File#createTempFile(java.lang.String, java.lang.String)}).
	 */
	public static void loadLibraryFromClasspath(String path) throws IOException {
		Path inputPath = Paths.get(path);

		if (!inputPath.isAbsolute()) {
			throw new IllegalArgumentException("The path has to be absolute, but found: " + inputPath);
		}

		String fileNameFull = inputPath.getFileName().toString();
		int dotIndex = fileNameFull.indexOf('.');
		if (dotIndex < 0 || dotIndex >= fileNameFull.length() - 1) {
			throw new IllegalArgumentException("The path has to end with a file name and extension, but found: " + fileNameFull);
		}

		String fileName = fileNameFull.substring(0, dotIndex);
		String extension = fileNameFull.substring(dotIndex);

		Path target = Files.createTempFile(fileName, extension);
		File targetFile = target.toFile();
		targetFile.deleteOnExit();

		try (InputStream source = NativeLibraryLoader.class.getResourceAsStream(inputPath.toString())) {
			if (source == null) {
				throw new FileNotFoundException("File " + inputPath + " was not found in classpath.");
			}
			Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
		}
		// Finally, load the library
		System.load(target.toAbsolutePath().toString());
	}
}
