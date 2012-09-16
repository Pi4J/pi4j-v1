package com.pi4j.util;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library
 * FILENAME      :  NativeLibraryLoader.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 Pi4J
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


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NativeLibraryLoader
{
    private static List<String> loadedLibraries = null;

    public static synchronized void load(String libraryName)
    {
        load(libraryName, null);
    }

    public static synchronized void load(String libraryName, String fileName)
    {
        // create instance if null
        if (loadedLibraries == null)
            loadedLibraries = Collections.synchronizedList(new ArrayList<String>());

        // first, make sure that this library has not already been previously loaded
        if (!loadedLibraries.contains(libraryName))
        {
            // assume library loaded successfully, add to tracking collection
            loadedLibraries.add(libraryName);

            try
            {
                // attempt to load the native library from the system classpath loader
                System.loadLibrary(libraryName);
            }
            catch (UnsatisfiedLinkError e)
            {
                // if a filename was not provided, then throw exception
                if (fileName == null)
                {
                    // library load failed, remove from tracking collection
                    loadedLibraries.remove(libraryName);

                    throw e;
                }

                try
                {
                    // attempt to get the native library from the JAR file in the 'lib'
                    // directory
                    URL resourceUrl = NativeLibraryLoader.class.getResource("/lib/" + fileName);

                    // create a 1Kb read buffer
                    byte[] buffer = new byte[1024];
                    int byteCount = 0;

                    // open the resource file stream
                    InputStream inputStream = resourceUrl.openStream();

                    // create a temporary file to copy the native library content to
                    File tempFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName);
                    OutputStream outputStream = new FileOutputStream(tempFile);

                    // ensure that this temporary file is removed when the program exits
                    tempFile.deleteOnExit();

                    // copy the library file content
                    while ((byteCount = inputStream.read(buffer)) >= 0)
                        outputStream.write(buffer, 0, byteCount);

                    // close the streams
                    outputStream.close();
                    inputStream.close();

                    // load the new temporary library file
                    System.load(tempFile.getAbsolutePath());
                }
                catch (Exception ex)
                {
                    // library load failed, remove from tracking collection
                    loadedLibraries.remove(libraryName);

                    throw (new UnsatisfiedLinkError(
                            "["
                                    + libraryName
                                    + ":"
                                    + fileName
                                    + "] Library could not be found on library path or embedded in JAR.\r\n"
                                    + ex.getMessage()));
                }
            }
        }
    }
}
