package com.example.grpc;

import java.util.*;
import java.util.stream.Collectors;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TestTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(TestTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        Set<String> s = new HashSet<String>();
        s.add("Geeks");
        s.add("for");
        List<String> aList = s.stream().collect(Collectors.toList());

        for (String x : aList)
            System.out.println(x);

        assertTrue(true);
    }
}