package com.sainsburys.groceryScraper.service;

import com.sainsburys.groceryScraper.domain.Product;
import com.sainsburys.groceryScraper.exception.ProductDetailsFormatException;
import com.sainsburys.groceryScraper.exception.ProductDetailsRetrievalException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Jsoup.class})
public class ProductScraperTest {

    private static final String TEST_URL = "http://some-test-url";
    private static final String EXPECTED_TITLE = "Sainsbury's Strawberries 400g";
    private static final Integer EXPECTED_CALORIES = 33;
    private static final Integer EXPECTED_V2_KCALS = 52;
    private static final BigDecimal EXPECTED_VALUE = new BigDecimal(1.75);
    private static final String EXPECTED_DESCRIPTION = "by Sainsbury's strawberries";

    private Connection connection = mock(Connection.class);
    private Product product;

    @Test
    public void scrapeProductFromURL() {
        givenAValidProductPage();
        whenTheProductDataIsScraped();
        thenTheCorrectProductIsRetrieved();
    }

    @Test(expected = ProductDetailsRetrievalException.class)
    public void connectingThrowsIOException() throws Exception {
        givenConnectingThrowsIOException();
        whenTheProductDataIsScraped();
    }

    @Test
    public void scrapeProductWithNoNutritionTable() {
        givenAValidProductPageWithNoNutritionTable();
        whenTheProductDataIsScraped();
        thenTheCorrectProductDataWithNoKCalIsRetrieved();
    }

    @Test
    public void scrapeProductWithNoKCalValue() {
        givenAValidProductPageWithNoKcalValue();
        whenTheProductDataIsScraped();
        thenTheCorrectProductDataWithNoKCalIsRetrieved();
    }

    @Test(expected = ProductDetailsFormatException.class)
    public void scrapeProductWithUnexpectedFormatting() {
        givenAnInvalidProductPage();
        whenTheProductDataIsScraped();
        thenTheCorrectKCalValueIsRetrieved();
    }

    @Test
    public void scrapeProductWithV2NutritionTable() {
        givenAValidProductPageWithV2NutritionTable();
        whenTheProductDataIsScraped();
        thenTheCorrectKCalValueIsRetrieved();
    }

    private void givenAValidProductPage() {
        setupResponse("valid_product_page.html");
    }

    private void givenAValidProductPageWithNoNutritionTable() {
        setupResponse("valid_product_page_no_nutrition.html");
    }

    private void givenAValidProductPageWithNoKcalValue() {
        setupResponse("valid_product_page_no_kcal.html");
    }

    private void givenAnInvalidProductPage() {
        setupResponse("invalid_product_page.html");
    }

    private void givenAValidProductPageWithV2NutritionTable() {
        setupResponse("valid_product_page_nutrition_table_v2.html");
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

    private void whenTheProductDataIsScraped() {
        product = ProductScraper.scrapeProductInformation(TEST_URL);
    }

    private void thenTheCorrectProductIsRetrieved() {
        assertEquals(EXPECTED_TITLE, product.getTitle());
        assertEquals(EXPECTED_CALORIES, product.getKcalPer100g());
        assertEquals(EXPECTED_VALUE, product.getUnitPrice());
        assertEquals(EXPECTED_DESCRIPTION, product.getDescription());
    }

    private void thenTheCorrectProductDataWithNoKCalIsRetrieved() {
        assertEquals(EXPECTED_TITLE, product.getTitle());
        assertNull(product.getKcalPer100g());
        assertEquals(EXPECTED_VALUE, product.getUnitPrice());
        assertEquals(EXPECTED_DESCRIPTION, product.getDescription());
    }

    private void thenTheCorrectKCalValueIsRetrieved() {
        assertEquals(EXPECTED_V2_KCALS,product.getKcalPer100g());
    }

    private void setupResponse(String filename)  {
        try {
            File file = new File(getClass().getClassLoader().getResource(filename).getFile());
            Document doc = Jsoup.parse(file, "UTF-8");

            PowerMockito.mockStatic(Jsoup.class);
            PowerMockito.when(Jsoup.connect(anyString())).thenReturn(connection);
            when(connection.get()).thenReturn(doc);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
