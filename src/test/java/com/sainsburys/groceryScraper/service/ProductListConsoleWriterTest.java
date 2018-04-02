package com.sainsburys.groceryScraper.service;

import com.sainsburys.groceryScraper.domain.Product;
import com.sainsburys.groceryScraper.domain.ProductList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductListConsoleWriterTest {

    private final static String TITLE = "test title'&";
    private final static Integer KCAL = 37;
    private final static BigDecimal PRICE = new BigDecimal(2.99).setScale(2, RoundingMode.HALF_UP);
    private final static String DESCRIPTION = "test description";

    private final ByteArrayOutputStream systemOut = new ByteArrayOutputStream();
    private Product product;
    private ProductList productList;
    private ProductListConsoleWriter productListConsoleWriter;

    private final static String EXPECTED_OUTPUT = "{\n" +
            "  \"results\": [\n" +
            "    {\n" +
            "      \"title\": \"test title'&\",\n" +
            "      \"kcal_per_100g\": 37,\n" +
            "      \"unit_price\": 2.99,\n" +
            "      \"description\": \"test description\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"total\": 2.99\n" +
            "}\r\n";

    @Before
    public void setup() {
        productListConsoleWriter = new ProductListConsoleWriter();
        System.setOut(new PrintStream(systemOut));
    }

    @After
    public void reset() {
        System.setOut(System.out);
    }

    @Test
    public void writesOutputInCorrectFormat() {
        givenAProductList();
        whenTheProductListIsWritten();
        thentheCorrectOutputWasWritten(EXPECTED_OUTPUT);
    }

    private void givenAProductList() {
        Product product = new Product(TITLE, KCAL, PRICE, DESCRIPTION);
        final List<Product> products = new ArrayList<>();
        products.add(product);
        productList = new ProductList(products, PRICE);
    }

    private void whenTheProductListIsWritten() {
        productListConsoleWriter.write(productList);
    }

    private void thentheCorrectOutputWasWritten(String expectedOutput) {
        String result = systemOut.toString();
        assertEquals(expectedOutput,result);
    }
}
