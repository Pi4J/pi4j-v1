package com.pi4j.io.file.test;

/*-
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  LinuxFileManualTest.java
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * This is a test program that is intended to be run inside a debugger
 * and not run as part of the JUint testing.  The reason for this is that
 * the JUnit version, @See LinuxFileTest, can only test that some value
 * comes back for the Posix File Descriptior number.  With a debugger,
 * the myFile variable can be examped to see what the private FD value is
 * and that number can be compared to the output of this program.
 */
public class LinuxFileManualTest {
    private static final String RW = "rw";

    public static void main(String[] args)
            throws IOException {
        // I had difficulty getting the program to find the native pi4j library
        // in Visual Studio Code.  The System.setProperty() can be used to force
        // the editor/IDE to look for the libpi4j-armhf.so in the directory where
        // it has been stored.

        //System.setProperty("pi4j.library.path", "/opt/pi4j/lib");

        ExposedLinuxFile myFile = null;
        String myFileName = null;
        int fd = -1;

        // Build Unique File Name

        // Generate a number that should be unique to the program
        long pid = ProcessHandle.current().pid();

        myFileName = "/tmp/pi4j-" + pid + "LinuxFileTest.txt";

        // Open and create the temporary file and get its POSIX file descriptor.
        // Note, exceptions can occur if unable to open the file.
        try {
            myFile = new ExposedLinuxFile(myFileName, RW);

            // Place a break point for the following statement.  Once the debugger
            // stops here, examine the value of myFile for the file descriptor number.
            // It may require examining fields within the object to find the file descriptor.
            fd = myFile.getMyFD();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();

            fd = -2;
        }

        System.out.printf("FileDescriptor for Random.test = %d\n", fd);

        // Cleanup
        myFile.close();

        File tempFile = new File(myFileName);

        if (tempFile.exists()) {
            tempFile.delete();
        }
    }
}
