package com.pi4j.util;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  ExecUtil.java
 *
 * This file is part of the Pi4J project. More information about
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2018 Pi4J
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
