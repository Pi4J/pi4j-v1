package com.pi4j.util;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  NativeLibraryLoader.java
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
 * #L%
 */

import com.pi4j.platform.Platform;
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

    public static synchronized void load(String libraryName) {
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
        if (loadedLibraries.contains(libraryName)) {
            logger.fine("Library [" + libraryName + "] has already been loaded; no need to load again.");
            return;
        }

        loadedLibraries.add(libraryName);

        String osArch = System.getProperty("os.arch");
        switch (osArch) {
            case "arm":
                osArch = "armhf";
                break;
            case "aarch64":
                break;
            default:
                throw new IllegalStateException("Unknown os.arch value " + osArch);
        }

        //
        // path = /lib/{platform}/{linking:static|dynamic}/{filename}-{arch}.so
        //
        String platform = System.getProperty("pi4j.platform", Platform.RASPBERRYPI.getId());

        // NOTE: As of 2018-04-23, Pi4J no longer includes
        //       a statically linked wiringPi lib for the Raspberry Pi platform.  The
        //       default linking for the Raspberry Pi platform should always be "dynamic"
        String linking = System.getProperty("pi4j.linking",
                platform.equalsIgnoreCase(Platform.RASPBERRYPI.getId()) ? "dynamic" : "static");

        String path = "/lib/" + platform + "/" + linking + "/" + libraryName + "-" + osArch + ".so";
        logger.fine("Attempting to load [" + libraryName + "] using path: [" + path + "]");
        try {
            loadLibraryFromClasspath(path);
            logger.fine(
                    "Library [" + libraryName + "] loaded successfully using embedded resource file: [" + path + "]");
        } catch (Exception | UnsatisfiedLinkError e) {
            logger.log(Level.SEVERE, "Unable to load [" + libraryName + "] using path: [" + path + "]", e);
            // either way, we did what we could, no need to remove now the library from the loaded libraries
            // since we run inside one VM and nothing could possibly change, so there is no point in
            // trying out this logic again
        }
    }

    /**
     * Loads library from classpath
     * <p>
     * The file from classpath is copied into system temporary directory and then loaded. The temporary file is deleted
     * after exiting. Method uses String as filename because the pathname is "abstract", not system-dependent.
     *
     * @param path The file path in classpath as an absolute path, e.g. /package/File.ext (could be inside jar)
     * @throws IOException              If temporary file creation or read/write operation fails
     * @throws IllegalArgumentException If source file (param path) does not exist
     * @throws IllegalArgumentException If the path is not absolute or if the filename is shorter than three characters
     *                                  (restriction of {@link Files#createTempFile(java.lang.String, java.lang.String,
     *                                  java.nio.file.attribute.FileAttribute...)}).
     */
    public static void loadLibraryFromClasspath(String path) throws IOException {
        Path inputPath = Paths.get(path);

        if (!inputPath.isAbsolute()) {
            throw new IllegalArgumentException("The path has to be absolute, but found: " + inputPath);
        }

        String fileNameFull = inputPath.getFileName().toString();
        int dotIndex = fileNameFull.indexOf('.');
        if (dotIndex < 0 || dotIndex >= fileNameFull.length() - 1) {
            throw new IllegalArgumentException(
                    "The path has to end with a file name and extension, but found: " + fileNameFull);
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
