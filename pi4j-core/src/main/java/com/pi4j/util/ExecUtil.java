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
 * Copyright (C) 2012 - 2016 Pi4J
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
        Process p = Runtime.getRuntime().exec(command);
        p.waitFor();
                
        if(p.exitValue() != 0)
            return null;
        
        InputStreamReader isr = new InputStreamReader(p.getInputStream());
        BufferedReader reader = new BufferedReader(isr);
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
            line = reader.readLine();
        }
        
        // close readers and stream
        reader.close();
        isr.close();
        p.getInputStream().close();

        if (result.size() > 0) {
            return result.toArray(new String[result.size()]);
        } else {
            return new String[0];
        }
    }
}
