package com.pi4j.util;

/*
 * #%L
 * **********************************************************************
 * ORGANIZATION  :  Pi4J
 * PROJECT       :  Pi4J :: Java Library (Core)
 * FILENAME      :  StringUtilTests.java
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


import static org.junit.Assert.*;

import org.junit.Test;

public class StringUtilTests {

    @Test
    public void testIsNullOrEmpty() {
        assertTrue(StringUtil.isNullOrEmpty(null));
        assertTrue(StringUtil.isNullOrEmpty(""));
        assertTrue(StringUtil.isNullOrEmpty("   ", true));
        assertFalse(StringUtil.isNullOrEmpty("   ", false));
    }

    @Test
    public void testIsNotNullOrEmpty() {
        assertFalse(StringUtil.isNotNullOrEmpty(null));
        assertFalse(StringUtil.isNotNullOrEmpty(""));
        assertFalse(StringUtil.isNotNullOrEmpty("   ", true));
        assertTrue(StringUtil.isNotNullOrEmpty("   ", false));
    }

    @Test
    public void testSourceContainsTarget() {
        assertTrue(StringUtil.contains("source-data-source", "data"));
        assertFalse(StringUtil.contains("source-data-source", "****"));
    }

    @Test
    public void testSourcesContainsTarget() {
        String sources[]  = {"one", "two", "three", "four", "five"};
        assertTrue(StringUtil.contains(sources, "ou"));
        assertFalse(StringUtil.contains(sources, "+"));
    }

    @Test
    public void testSourceContainsTargets() {
        String targets[]  = {"one", "two", "three", "four", "five"};
        assertTrue(StringUtil.contains("This is one fine day!", targets));
        assertFalse(StringUtil.contains("This is not a fine day!", targets));
    }

    @Test
    public void testSourcesContainsTargets() {
        String sources[]  = {"one", "two", "three", "four", "five"};
        String targets1[]  = {"apple", "orange", "bananna", "peach", "pear"};
        String targets2[]  = {"qq", "zz", "tw", "uu", "yy"};
        assertTrue(StringUtil.contains(sources, targets2));
        assertFalse(StringUtil.contains(sources, targets1));
    }

    @Test
    public void testCreateSpace() {
        String test = StringUtil.create(5);
        assertEquals("     ", test);
    }

    @Test
    public void testCreateChar() {
        String test = StringUtil.create('-', 5);
        assertEquals("-----", test);
    }

    @Test
    public void testCreateString() {
        String test = StringUtil.create("+-", 5);
        assertEquals("+-+-+-+-+-", test);
    }

    @Test
    public void testPadLeftSpace() {
        String test = StringUtil.padLeft("test-data", 5);
        assertEquals("     test-data", test);
    }

    @Test
    public void testPadLeftChar() {
        String test = StringUtil.padLeft("test-data", '+', 5);
        assertEquals("+++++test-data", test);
    }

    @Test
    public void testPadLeftString() {
        String test = StringUtil.padLeft("test-data", "<>", 5);
        assertEquals("<><><><><>test-data", test);
    }

    @Test
    public void testPadRightSpace() {
        String test = StringUtil.padRight("test-data", 5);
        assertEquals("test-data     ", test);
    }

    @Test
    public void testPadRightChar() {
        String test = StringUtil.padRight("test-data", '+', 5);
        assertEquals("test-data+++++", test);
    }

    @Test
    public void testPadRightString() {
        String test = StringUtil.padRight("test-data", "<>", 5);
        assertEquals("test-data<><><><><>", test);
    }

    @Test
    public void testPadBothSpace() {
        String test = StringUtil.pad("test-data", 5);
        assertEquals("     test-data     ", test);
    }

    @Test
    public void testPadBothChar() {
        String test = StringUtil.pad("test-data", '+', 5);
        assertEquals("+++++test-data+++++", test);
    }

    @Test
    public void testPadBothString() {
        String test = StringUtil.pad("test-data", "<>", 5);
        assertEquals("<><><><><>test-data<><><><><>", test);
    }

    @Test
    public void testPadCenterSpace() {
        String test1 = StringUtil.padCenter("test", 10);
        assertEquals("   test   ", test1);

        String test2 = StringUtil.padCenter("tst", 10);
        assertEquals("   tst    ", test2);
    }

    @Test
    public void testPadCenterChar() {
        String test1 = StringUtil.padCenter("test", '+', 10);
        assertEquals("+++test+++", test1);

        String test2 = StringUtil.padCenter("tst", '+', 10);
        assertEquals("+++tst++++", test2);
    }

    @Test
    public void testTrimLeftSpace() {
        String test = StringUtil.trimLeft("  this is a test  ");
        assertEquals("this is a test  ", test);
    }

    @Test
    public void testTrimLeftChar() {
        String test = StringUtil.trimLeft("...this is a test...", '.');
        assertEquals("this is a test...", test);
    }

    @Test
    public void testTrimRightSpace() {
        String test = StringUtil.trimRight("  this is a test  ");
        assertEquals("  this is a test", test);
    }

    @Test
    public void testTrimRightChar() {
        String test = StringUtil.trimRight("...this is a test...", '.');
        assertEquals("...this is a test", test);
    }

    @Test
    public void testTrimSpace() {
        String test = StringUtil.trim("  this is a test  ");
        assertEquals("this is a test", test);
    }

    @Test
    public void testTrimChar() {
        String test = StringUtil.trim("...this is a test...", '.');
        assertEquals("this is a test", test);
    }

}
