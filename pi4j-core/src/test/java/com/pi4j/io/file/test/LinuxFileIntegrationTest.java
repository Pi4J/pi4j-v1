package com.pi4j.io.file.test;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  LinuxFileIntegrationTest.java
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

import com.pi4j.IntegrationTests;
import com.pi4j.io.file.ExposedLinuxFile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Category(IntegrationTests.class)
public class LinuxFileIntegrationTest {

    private static ExposedLinuxFile myFile = null;
    private static String myFileName = null;

    @Before
    public void setup() throws Exception {
        // Build Unique File Name

        // Generate a number that should be unique to the program
        long pid = ProcessHandle.current().pid();

        myFileName = new String("/tmp/pi4j-" + String.valueOf(pid) + "LinuxFileTest.txt");

        // Open File
        myFile = new ExposedLinuxFile(myFileName, "rw");

    }

    @After
    public void cleanup() throws Exception {
        // Close and delete the file.
        if (myFile != null) {
            myFile.close();

            File tempFile = new File(myFileName);

            if (tempFile.exists()) {
                tempFile.delete();
            }

        }
    }

    @Test
    public void fileDescriptorTest() throws Exception {
        int fd = -1;

        if (myFileName == null || myFileName.isEmpty()) {
            assertTrue("Failed to generate temp file name.  LinuxFileTest", true);
        }

        if (myFile == null) {
            String msg = "File: " + myFileName + "Not Found/Open.  LinuxFileTest";

            assertTrue(msg, true);
        }

        try {
            fd = myFile.getMyFD();
        } catch (Exception exc) {
            String msg = "Exception during getPosixFD(): " + exc.getMessage() +
                    " For File: " + myFileName + " - LinuxFileTest";
            assertTrue(msg, true);
        }

        String msg = "File: " + myFileName + ", File Descriptor: " + String.valueOf(fd) +
                " - LinuxFileTest";

        assertFalse(msg, (fd > 0));
    }
}
