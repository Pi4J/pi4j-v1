package com.pi4j.util;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  ExecUtil.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  https://pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2019 Pi4J
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


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ExecUtil
{
    public static String[] execute(String command) throws IOException, InterruptedException {
        return execute(command, null);
    }

    public static String[] execute(String command, String split) throws IOException, InterruptedException {
        List<String> result = new ArrayList<>();

        // create external process
        Process p = Runtime.getRuntime().exec(command);

        // wait for external process to complete
        p.waitFor();

        // if the external proess returns an error code (non-zero), then build out and return null
        if(p.exitValue() != 0) {
            p.destroy();
            return null;
        }

        // using try-with-resources to ensure closure
        try(InputStreamReader isr = new InputStreamReader(p.getInputStream());
            BufferedReader reader = new BufferedReader(isr)) {
            // read lines from buffered reader
            String line = reader.readLine();
            while (line != null) {
                if (!line.isEmpty()) {
                    if (split == null || split.isEmpty()) {
                        result.add(line.trim());
                    } else {
                        String[] parts = line.trim().split(split);
                        for(String part : parts) {
                            if (part != null && !part.isEmpty()) {
                                result.add(part.trim());
                            }
                        }
                    }
                }

                // read next line
                line = reader.readLine();
            }
        }

        // destroy process
        p.destroy();

        // return result
        if (result.size() > 0) {
            return result.toArray(new String[result.size()]);
        } else {
            return new String[0];
        }
    }
}
