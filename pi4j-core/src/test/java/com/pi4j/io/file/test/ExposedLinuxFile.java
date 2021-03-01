package com.pi4j.io.file.test;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  ExposedLinuxFile.java
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

 import com.pi4j.io.file.LinuxFile;

 import  java.io.FileNotFoundException;
 import  java.io.IOException;

 /**
  * Extends the com.pi4j.io.file.LinuxFile call for JUNIT test.
  * This child class allows the test programs to access the POSIX File Descriptor.
  *
  * THIS CLASS SHOULD NEVER BE USED in actual code.  It is for testing only.
  */
public class ExposedLinuxFile  extends LinuxFile {

    /**
     * Only constructor for this class.
     *
     * @param name  Name of file to open.
     * @param mode  Mode to open file with.  See JavaDoc for java.io.RandomFileAccess
     *              constructor for list of legal values for <strong>mode>/strong>
     * @throws FileNotFoundException
     */
    public ExposedLinuxFile(String name, String mode)
           throws FileNotFoundException
    {
      super(name, mode);
    }

    /**
     * Exposes the POSIX File Descriptor associated with the open filed.
     *
     * @return Returns the POSIX file descriptor integer assigned to the file.
     *
     * @throws IOException
     */
    public int getMyFD() throws IOException
    {
        return getPosixFD( getFD() );
    }

}
