package com.sainsburys.groceryScraper.service;

import org.junit.After;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.nio.file.Paths;

import static java.nio.file.Files.readAllBytes;
import static org.junit.Assert.assertEquals;
import static com.sainsburys.groceryScraper.util.TestUtils.unifyLineEndings;

public class GroceryScraperIntegrationTest {

    private static final String TARGET_URL = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";

    private final ByteArrayOutputStream systemOut = new ByteArrayOutputStream();
    private final ByteArrayOutputStream systemErr = new ByteArrayOutputStream();

    @Test
    public void endToEndTest() {
        givenAConsoleOutputStream();
        whenMainMethodIsCalled();
        thenTheCorrectJSONIsOutputToTheConsole();
    }

    @After
    public void reset() {
        System.setOut(System.out);
        System.setErr(System.err);
    }

    private void givenAConsoleOutputStream() {
        System.setOut(new PrintStream(systemOut));
        System.setErr(new PrintStream(systemErr));
    }

    private void whenMainMethodIsCalled() {
        GroceryScraper.main(new String[]{TARGET_URL});
    }

    private void thenTheCorrectJSONIsOutputToTheConsole() {
        String expectedOutput;
        try {
            URI uri = getClass().getClassLoader().getResource("expected_console_output").toURI();
            expectedOutput = new String(readAllBytes(Paths.get(uri)));
        } catch(Exception e) {
            throw new RuntimeException(e);
        }

        assertEquals(unifyLineEndings(expectedOutput),
                unifyLineEndings(systemOut.toString()));
    }
}
