package com.cec;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class FileProcessorTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public FileProcessorTest(String testName)
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( FileProcessorTest.class );
    }

    public void testFindByCountry() throws IOException {
        List<String> linesExpected = Arrays.asList(
                "Cristian,ARG,111",
                "Rosa,ARG,222",
                "Alfredo,ARG,444");
        List<String> linesObteined = FileProcessor.findByCountry(
                reader("C:\\Users\\cricia\\development\\git\\interviews\\src\\test\\resources\\java8\\processing-file.csv"),
                "ARG");
        assertTrue(linesExpected.containsAll(linesObteined));
    }

    public void testFindByCountry_withEmptyResult() throws IOException {
        List<String> linesObteined = FileProcessor.findByCountry(
                reader("C:\\Users\\cricia\\development\\git\\interviews\\src\\test\\resources\\java8\\processing-file.csv"),
                "Alfredo");
        assertEquals(0, linesObteined.size());
    }

    public void testFindByCountry_withEmptyFile() throws IOException {
        List<String> linesObteined = FileProcessor.findByCountry(
                reader("C:\\Users\\cricia\\development\\git\\interviews\\src\\test\\resources\\java8\\empty-processing-file.csv"),
                "ARG");
        assertEquals(0, linesObteined.size());
    }

    public void testFindByCountry_withOnlyHeaderFile() throws IOException {
        List<String> linesObteined = FileProcessor.findByCountry(
                reader("C:\\Users\\cricia\\development\\git\\interviews\\src\\test\\resources\\java8\\only-header-file.csv"),
                "ARG");
        assertEquals(0, linesObteined.size());
    }

    public void testFindByCountry_withCommaAtTheEndOfLine() throws IOException {
        List<String> linesObteined = FileProcessor.findByCountry(
                reader("C:\\Users\\cricia\\development\\git\\interviews\\src\\test\\resources\\java8\\comma-at-the-end-of-line.csv"),
                "ARG");
        assertEquals(3, linesObteined.size());
    }

    public void testFindByCountryAndResponseTimeAboutLimit() throws IOException {
        List<String> linesObteined = FileProcessor.findByCountryAndResponseTimeAboutLimit(
                reader("C:\\Users\\cricia\\development\\git\\interviews\\src\\test\\resources\\java8\\processing-file-invalid-response-time.csv"),
                "ARG", 400l);
        assertEquals(0, linesObteined.size());
    }

    public void testFindByCountryAndResponseTimeAboutLimit_withSomeInvalidResponseTimeLines() throws IOException {
        List<String> linesObteined = FileProcessor.findByCountryAndResponseTimeAboutLimit(
                reader("C:\\Users\\cricia\\development\\git\\interviews\\src\\test\\resources\\java8\\processing-file-invalid-response-time.csv"),
                "ARG", 200l);
        assertEquals(1, linesObteined.size());
    }

    public void testFindByCountryAndResponseTimeAboutLimit_withInvalidResponseTime() throws IOException {
        List<String> linesExpected = Arrays.asList("Alfredo,ARG,444");
        List<String> linesObteined = FileProcessor.findByCountryAndResponseTimeAboutLimit(
                reader("C:\\Users\\cricia\\development\\git\\interviews\\src\\test\\resources\\java8\\processing-file.csv"),
                "ARG", 400l);
        assertTrue(linesExpected.containsAll(linesObteined));
    }

    public void testFindAboveAverage() throws IOException {
        List<String> linesExpected = Arrays.asList(
                "Alfredo,ARG,444",
                "Emma,SPA,555");
        List<String> linesObteined = FileProcessor.findAboveAverage(
                reader("C:\\Users\\cricia\\development\\git\\interviews\\src\\test\\resources\\java8\\processing-file.csv"));
        assertTrue(linesObteined.containsAll(linesExpected));
    }

    private BufferedReader reader(String filename) throws IOException {
        return Files.newBufferedReader(Paths.get(filename));
    }
}
