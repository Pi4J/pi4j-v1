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
 * Copyright (C) 2012 - 2019 Pi4J
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


public class StringUtil {

    public static final String EMPTY = "";
    public static final char DEFAULT_PAD_CHAR = ' ';

    public static boolean isNullOrEmpty(String data, boolean trim){
        if(data == null)
            return true;

        // trim if requested
        String test = data;
        if(trim)
            test = data.trim();

        return (test.length() <= 0);
    }

    public static boolean isNullOrEmpty(String data){
        return isNullOrEmpty(data, false);
    }

    public static boolean isNotNullOrEmpty(String data){
        return isNotNullOrEmpty(data, false);
    }

    public static boolean isNotNullOrEmpty(String data, boolean trim){
        return !(isNullOrEmpty(data, trim));
    }

    public static boolean contains(String source, String target)  {
        return (null != source && null != target && source.contains(target));
    }

    public static boolean contains(String source, String[] targets)  {
        if (null != source && null != targets) {
            for(String target : targets) {
                if (source.contains(target)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean contains(String[] sources, String target)  {
        if (null != sources && null != target) {
            for (String source : sources) {
                if(contains(source, target))
                    return true;
            }
        }
        return false;
    }

    public static boolean contains(String[] sources, String[] targets)  {
        if (null != sources && null != targets) {
            for (String source : sources) {
                if(contains(source, targets))
                    return true;
            }
        }
        return false;
    }

    public static String create(int length)  {
        return create(DEFAULT_PAD_CHAR, length);
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

    public static String repeat(char c, int length)  {
        return create(c, length);
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

    public static String pad(String data, int length)  {
        return pad(data, DEFAULT_PAD_CHAR, length);
    }

    public static String pad(String data, char pad, int length)  {
        return create(pad, length) + data + create(pad, length);
    }

    public static String pad(String data, String pad, int length)  {
        return create(pad, length) + data + create(pad, length);
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
        return trimLeft(data, DEFAULT_PAD_CHAR);
    }

    public static String trimLeft(String data, char trim)  {
        for(int index = 0; index < data.length(); index++)
            if(!(data.charAt(index) == trim))
                return data.substring(index);
        return EMPTY;
    }

    public static String trimRight(String data)  {
        return trimRight(data, DEFAULT_PAD_CHAR);
    }

    public static String trimRight(String data, char trim)  {
        int count = 0;
        for(int index = data.length(); index > 0; index--)
            if(data.charAt(index-1) == trim)
                count++;
            else
                return data.substring(0, data.length() - count);
        return EMPTY;
    }

    public static String trim(String data)  {
        return trim(data, DEFAULT_PAD_CHAR);
    }

    public static String trim(String data, char trim)  {
        String result = trimLeft(data, trim);
        return trimRight(result, trim);
    }

    public static String center(String text, int length){
        String out = String.format("%"+length+"s%s%"+length+"s", "",text,"");
        float mid = (out.length()/2);
        float start = mid - (length/2);
        float end = start + length;
        return out.substring((int) start, (int) end);
    }

    public static String concat(String ... data)  {
        StringBuilder sb = new StringBuilder();
        for(String d : data){
            sb.append(d);
        }
        return sb.toString();
    }
}
