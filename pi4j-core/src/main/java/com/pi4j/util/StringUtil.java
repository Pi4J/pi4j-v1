package com.pi4j.util;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  StringUtil.java  
 * 
 * This file is part of the Pi4J project. More information about 
 * this project can be found here:  http://www.pi4j.com/
 * **********************************************************************
 * %%
 * Copyright (C) 2012 - 2013 Pi4J
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


public class StringUtil {
    
    public static final String EMPTY = "";
    public static final char DEFAULT_PAD_CHAR = ' ';
    
    public static boolean isNullOrEmpty(String data){
        return (data == null || data.length() <= 0);        
    }

    public static boolean isNotNullOrEmpty(String data){
        return !isNullOrEmpty(data);        
    }
    
    public static boolean contains(String data, String[] search)  {
        if (null != data && null != search) { 
            for (int i=0; i<search.length; i++) {
                if (data.indexOf(search[i]) >= 0) {
                    return true;
                }
            }
        }
        return false;
    }     
    
    public static String create(char c, int length)  {
        StringBuilder sb = new StringBuilder(length);
        for(int index = 0; index < length; index++)
            sb.append(c);
        return sb.toString();
    }     
    
    public static String create(String s, int length)  {
        StringBuilder sb = new StringBuilder(length * s.length());
        for(int index = 0; index < length; index++)
            sb.append(s);
        return sb.toString();
    }     

    public static String padLeft(String data, int length)  {
        return padLeft(data, DEFAULT_PAD_CHAR, length);
    }
    
    public static String padLeft(String data, char pad, int length)  {
        StringBuilder sb = new StringBuilder(data.length() + length);
        for(int index = 0; index < length; index++)
            sb.append(pad);
        sb.append(data);
        return sb.toString();
    }     

    public static String padLeft(String data, String pad, int length)  {
        StringBuilder sb = new StringBuilder(data.length() + (length * pad.length()));
        for(int index = 0; index < length; index++)
            sb.append(pad);
        sb.append(data);
        return sb.toString();
    }     

    public static String padRight(String data, int length)  {
        return padRight(data, DEFAULT_PAD_CHAR, length);
    }
    
    public static String padRight(String data, char pad, int length)  {
        StringBuilder sb = new StringBuilder(data.length() + length);
        sb.append(data);
        for(int index = 0; index < length; index++)
            sb.append(pad);        
        return sb.toString();
    }     

    public static String padRight(String data, String pad, int length)  {
        StringBuilder sb = new StringBuilder(data.length() + (length * pad.length()));
        sb.append(data);
        for(int index = 0; index < length; index++)
            sb.append(pad);
        return sb.toString();
    }     

    public static String padCenter(String data, int length) {
        return padCenter(data, DEFAULT_PAD_CHAR, length);
    }
    
    public static String padCenter(String data, char pad, int length) {
        if(data.length() < length) {
            int needed = length - data.length();
            int padNeeded = needed / 2;
            StringBuilder result = new StringBuilder();
            result.append(create(pad, padNeeded));
            result.append(data);
            result.append(create(pad, padNeeded));
            int remaining = length - result.length();
            result.append(create(pad, remaining));
            return result.toString();
        }
        return data;
    }
    
    public static String trimLeft(String data)  {
        for(int index = 0; index < data.length(); index++)
            if(!data.substring(index, 1).equals(" "))
                return data.substring(index);
        return EMPTY;
    }     

    public static String trimRight(String data)  {
        int count = 0;
        for(int index = data.length(); index > 0; index++)
            if(data.substring(index-1, 1).equals(" "))
                count++;
            else
                return data.substring(0, data.length() - count);
        return EMPTY;
    }     
    
}
