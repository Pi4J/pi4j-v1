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
 * Copyright (C) 2012 Pi4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * #L%
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pi4j.system.SystemInfo;

public class NativeLibraryLoader {

    private static List<String> loadedLibraries = null;
    private static Logger logger = Logger.getLogger("com.pi4j.util.NativeLibraryLoader");
    private static FileHandler fileHandler;
    private static ConsoleHandler consoleHandler;
    private static boolean initialized  = false;

    // private constructor 
    private NativeLibraryLoader() {
        // forbid object construction 
    }
    
    public static synchronized void load(String libraryName) {
        load(libraryName, null);
    }

    public static synchronized void load(String libraryName, String fileName) {
        // check for debug property; if found enabled all logging levels
        if (initialized == false) {
            initialized = true;
            String debug = System.getProperty("pi4j.debug");
            if (debug != null) {
                logger.setLevel(Level.ALL);
                try {
                    // create an appending file handler
                    fileHandler = new FileHandler("pi4j.log");
                    fileHandler.setLevel(Level.ALL);
                    consoleHandler = new ConsoleHandler();
                    consoleHandler.setLevel(Level.ALL);
    
                    // add to the desired loggers
                    logger.addHandler(fileHandler);
                    logger.addHandler(consoleHandler);
                } 
                catch (IOException e) {} 
            }
        }

        // debug
        if (fileName == null || fileName.length() == 0) {
            logger.fine("Load library [" + libraryName + "] (no alternate embedded file provided)");
        } else {
            logger.fine("Load library [" + libraryName + "] (alternate embedded file: " + fileName + ")");
        }
        // create instance if null
        if (loadedLibraries == null) {
            loadedLibraries = Collections.synchronizedList(new ArrayList<String>());
        }
        // first, make sure that this library has not already been previously loaded
        if (loadedLibraries.contains(libraryName)) {
            // debug
            logger.fine("Library [" + libraryName + "] has already been loaded; no need to load again.");            
        } else {
            // ---------------------------------------------
            // ATTEMPT LOAD FROM SYSTEM LIBS
            // ---------------------------------------------
            
            // assume library loaded successfully, add to tracking collection
            loadedLibraries.add(libraryName);
            
            try {
                // debug
                logger.fine("Attempting to load library [" + libraryName + "] using the System.loadLibrary(name) method.");            
                
                // attempt to load the native library from the system classpath loader
                System.loadLibrary(libraryName);
                
                // debug
                logger.fine("Library [" + libraryName + "] loaded successfully using the System.loadLibrary(name) method.");                            
            } catch (UnsatisfiedLinkError e) {
                // if a filename was not provided, then throw exception
                if (fileName == null) {
                    // debug
                    logger.severe("Library [" + libraryName + "] could not be using the System.loadLibrary(name) method and no embedded file path was provided as an auxillary lookup.");                            

                    // library load failed, remove from tracking collection
                    loadedLibraries.remove(libraryName);
                    throw e;
                }

                // debug
                logger.fine("Library [" + libraryName + "] could not be using the System.loadLibrary(name) method; attempting to resolve the library using embedded resources in the JAR file.");                            

                
                // ---------------------------------------------
                // ATTEMPT LOAD BASED ON EDUCATED GUESS OF ABI
                // ---------------------------------------------
                
                URL resourceUrl;
                
                // first attempt to determine if we are running on a hard float (armhf) based system  
                if(SystemInfo.isHardFloatAbi()) {
                    // attempt to get the native library from the JAR file in the 'lib/hard-float' directory
                    resourceUrl = NativeLibraryLoader.class.getResource("/lib/hard-float/" + fileName);
                } else {
                    // attempt to get the native library from the JAR file in the 'lib/soft-float' directory
                    resourceUrl = NativeLibraryLoader.class.getResource("/lib/soft-float/" + fileName);
                }
                
                try {               
                    // load library file from embedded resource
                    loadLibraryFromResource(resourceUrl, libraryName, fileName);
                    
                    // debug
                    logger.info("Library [" + libraryName + "] loaded successfully using embedded resource file: [" + resourceUrl.toString() + "]");
                } catch(Exception ex) {
                    // ---------------------------------------------
                    // ATTEMPT LOAD BASED USING HARD-FLOAT (armhf)
                    // ---------------------------------------------
                    
                    // attempt to get the native library from the JAR file in the 'lib/hard-float' directory
                    URL resourceUrlHardFloat = NativeLibraryLoader.class.getResource("/lib/hard-float/" + fileName);
                    
                    try {
                        // load library file from embedded resource
                        loadLibraryFromResource(resourceUrlHardFloat, libraryName, fileName);
                        
                        // debug
                        logger.info("Library [" + libraryName + "] loaded successfully using embedded resource file: [" + resourceUrlHardFloat.toString() + "] (ARMHF)");                                                        
                    } catch(UnsatisfiedLinkError ule_hard_float) {
                        // debug
                        logger.fine("Failed to load library [" + libraryName + "] using the System.load(file) method using embedded resource file: [" + resourceUrlHardFloat.toString() + "]");            

                        // ---------------------------------------------
                        // ATTEMPT LOAD BASED USING SOFT-FLOAT (armel)
                        // ---------------------------------------------
                        
                        // attempt to get the native library from the JAR file in the 'lib/soft-float' directory
                        URL resourceUrlSoftFloat = NativeLibraryLoader.class.getResource("/lib/soft-float/" + fileName);
                        
                        try {
                            // load library file from embedded resource
                            loadLibraryFromResource(resourceUrlSoftFloat, libraryName, fileName);
                            
                            // debug
                            logger.info("Library [" + libraryName + "] loaded successfully using embedded resource file: [" + resourceUrlSoftFloat.toString() + "] (ARMEL)");                                                        
                        } catch (Throwable err) {
                            // debug
                            logger.severe("Failed to load library [" + libraryName + "] using the System.load(file) method using embedded resource file: [" + resourceUrlSoftFloat.toString() + "]");            
                            logger.throwing(logger.getName(), "load", err);
    
                            // library load failed, remove from tracking collection
                            loadedLibraries.remove(libraryName);
    
                            logger.severe("ERROR:  The native library ["
                                        + libraryName
                                        + " : "
                                        + fileName
                                        + "] could not be found in the JVM library path nor could it be loaded from the embedded JAR resource file; you may need to explicitly define the library path '-Djava.library.path' where this native library can be found.");
                        }
                    } catch (Exception ex_hard_float) {
                        // debug
                        logger.severe("Failed to load library [" + libraryName + "] using the System.load(file) method using embedded resource file: [" + resourceUrlHardFloat.toString() + "]");            
                        logger.throwing(logger.getName(), "load", ex_hard_float);
                        
                        // library load failed, remove from tracking collection
                        loadedLibraries.remove(libraryName);
    
                        logger.severe("ERROR:  The native library ["
                                    + libraryName
                                    + " : "
                                    + fileName
                                    + "] could not be found in the JVM library path nor could it be loaded from the embedded JAR resource file; you may need to explicitly define the library path '-Djava.library.path' where this native library can be found.");
                    }
                }
            }
        }
    }

    private static void loadLibraryFromResource(URL resourceUrl, String libraryName, String fileName) throws UnsatisfiedLinkError, Exception {
        // create a 1Kb read buffer
        byte[] buffer = new byte[1024];
        int byteCount = 0;
        
        // debug
        logger.fine("Attempting to load library [" + libraryName + "] using the System.load(file) method using embedded resource file: [" + resourceUrl.toString() + "]");            
        
        // open the resource file stream
        InputStream inputStream = resourceUrl.openStream();

        // get the system temporary directory path
        File tempDirectory = new File(System.getProperty("java.io.tmpdir"));
        
        // check to see if the temporary path exists
        if (!tempDirectory.exists()) {
            // debug
            logger.warning("The Java system temporary path [" + tempDirectory.getAbsolutePath() + "] does not exist.");            
            
            // instead of the system defined temporary path, let just use the application path
            tempDirectory = new File("");
        }
        
        // create a temporary file to copy the native library content to
        File tempFile = new File(tempDirectory.getAbsolutePath() + "/" + fileName);
        
        // make sure that this temporary file does not exist; if it does then delete it
        if (tempFile.exists()) {
            // debug
            logger.warning("The temporary file already exists [" + tempFile.getAbsolutePath() + "]; attempting to delete it now.");            
            
            // delete file immediately 
            tempFile.delete();
        }
        
        // create output stream object
        OutputStream outputStream = null;
        
        try {
            // create the new file
            outputStream = new FileOutputStream(tempFile);
        } catch(FileNotFoundException fnfe) {
            // error
            logger.severe("The temporary file [" + tempFile.getAbsolutePath() + "] cannot be created; it is a directory, not a file.");            
            throw(fnfe);
        } catch(SecurityException se) {
            // error
            logger.severe("The temporary file [" + tempFile.getAbsolutePath() + "] cannot be created; a security exception was detected. " + se.getMessage());            
            throw(se);
        }
        
        try {
            // copy the library file content
            while ((byteCount = inputStream.read(buffer)) >= 0) {
                outputStream.write(buffer, 0, byteCount);
            }
            
            // flush all write data from stream 
            outputStream.flush();
            
            // close the output stream
            outputStream.close();                            
        } catch(IOException ioe) {
            // error
            logger.severe("The temporary file [" + tempFile.getAbsolutePath() + "] could not be written to; an IO exception was detected. " + ioe.getMessage());            
            throw(ioe);                            
        }

        // close the input stream
        inputStream.close();
        
        try {
            // attempt to load the new temporary library file
            System.load(tempFile.getAbsolutePath());
            
            try {
                // ensure that this temporary file is removed when the program exits
                tempFile.deleteOnExit();
            } catch(SecurityException dse) {
                // warning
                logger.warning("The temporary file [" + tempFile.getAbsolutePath() + "] cannot be flagged for removal on program termination; a security exception was detected. " + dse.getMessage());            
            }
        } catch(UnsatisfiedLinkError ule) {
            // if unable to load the library and the temporary file
            // exists; then delete the temporary file immediately
            if(tempFile.exists())
                tempFile.delete();
            
            throw(ule);
        } catch(Exception ex) {
            // if unable to load the library and the temporary file
            // exists; then delete the temporary file immediately
            if (tempFile.exists()) {
                tempFile.delete();
            }
            
            throw(ex);
        }
    }
}
