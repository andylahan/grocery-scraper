package com.sainsburys.groceryScraper.service;

import com.sainsburys.groceryScraper.domain.Product;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.VerificationModeFactory;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;

import static com.sainsburys.groceryScraper.util.TestUtils.unifyLineEndings;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Jsoup.class, ProductScraper.class, GroceryScraper.class})
public class GroceryScraperTest {

    private Connection connection = mock(Connection.class);
    private final ByteArrayOutputStream systemOut = new ByteArrayOutputStream();
    private final ByteArrayOutputStream systemErr = new ByteArrayOutputStream();

    private final static String TEST_URL = "http://some-url.html";
    private final static String EXPECTED_URL = "https://jsainsburyplc.github.io/../shop/gb/groceries/berries-cherries-currants/sainsburys-british-strawberries-400g.html";
    private final static String EXPECTED_LOG_MESSAGE = "Could not complete GroceryScraper due to the following...\n";
    private final static String EXPECTED_IO_EXCEPTION = "java.io.IOException";
    private final static String EXPECTED_RUNTIME_EXCEPTION = "java.lang.RuntimeException";
    private final static String EXPECTED_USAGE = "GroceryScraper requires a URL as an argument";

    @Test
    public void callsProductURL() {
        givenAValidSummaryPage();
        givenAProductScraper();
        whenThePageIsScraped();
        thenTheCorrectProductURIIsUsed();
    }

    @Test
    public void exceptionCallingURL() {
        givenConnectingThrowsIOException();
        givenAConsoleOutputStream();
        whenThePageIsScraped();
        thenTheErrorIsReportedToTheConsole(EXPECTED_IO_EXCEPTION);
    }

    @Test
    public void productScraperThrowsException() {
        givenAValidSummaryPage();
        givenAConsoleOutputStream();
        givenProductScraperThrowsException();
        whenThePageIsScraped();
        thenTheErrorIsReportedToTheConsole(EXPECTED_RUNTIME_EXCEPTION);
    }

    @Test
    public void mainPrintsUaageForIncorrectArguments() {
        givenAConsoleOutputStream();
        whenICallMainWithNoArguments();
        thenUsageIsPrintedToTheConsole();
    }

    @After
    public void reset() {
        System.setOut(System.out);
        System.setErr(System.err);
    }

    private void givenAValidSummaryPage() {
        try {
            File file = new File(getClass().getClassLoader().getResource("valid_summary_page.html").getFile());
            Document doc = Jsoup.parse(file, "UTF-8", "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/");

            PowerMockito.mockStatic(Jsoup.class);
            PowerMockito.when(Jsoup.connect(anyString())).thenReturn(connection);
            when(connection.get()).thenReturn(doc);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void givenAProductScraper() {
        PowerMockito.mockStatic(ProductScraper.class);
        Product product = new Product("blah",1, BigDecimal.TEN,"hello");
        PowerMockito.when(ProductScraper.scrapeProductInformation(anyString())).thenReturn(product);
    }

    private void givenProductScraperThrowsException() {
        PowerMockito.mockStatic(ProductScraper.class);
        PowerMockito.when(ProductScraper.scrapeProductInformation(anyString())).thenThrow(new RuntimeException());
    }

    private void givenConnectingThrowsIOException() {
        PowerMockito.mockStatic(Jsoup.class);
        PowerMockito.when(Jsoup.connect(anyString())).thenReturn(connection);
        try {
            when(connection.get()).thenThrow(new IOException());
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void givenAConsoleOutputStream() {
        System.setOut(new PrintStream(systemOut));
        System.setErr(new PrintStream(systemErr));
    }

    private void whenThePageIsScraped() {
        new GroceryScraper().scrape(TEST_URL);
    }

    private void whenICallMainWithNoArguments() {
        GroceryScraper.main(new String[0]);
    }

    private void thenTheCorrectProductURIIsUsed() {
        PowerMockito.verifyStatic(VerificationModeFactory.times(1));
        ProductScraper.scrapeProductInformation(EXPECTED_URL);
    }

    private void thenTheErrorIsReportedToTheConsole(String expectedException) {
        assertEquals(EXPECTED_LOG_MESSAGE, unifyLineEndings(systemOut.toString()));
        assertTrue(systemErr.toString().contains(expectedException));
    }

    private void thenUsageIsPrintedToTheConsole() {
        assertTrue(systemOut.toString().contains(EXPECTED_USAGE));
    }

}
