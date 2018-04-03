package com.sainsburys.groceryScraper.service;

import com.sainsburys.groceryScraper.domain.Product;
import com.sainsburys.groceryScraper.domain.ProductList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class GroceryScraper {

    public void scrape(String URI) {
        try {
            Document document = Jsoup.connect(URI).get();

            Elements elements = document.select("div.product");
            List<Product> products = new ArrayList<>();
            BigDecimal totalPrice = BigDecimal.ZERO;
            for(Element element : elements) {
                Element infoLink = element.selectFirst("a[href]");
                String productInfoURL = getProductInfoUri(infoLink);
                Product product = ProductScraper.scrapeProductInformation(productInfoURL);
                totalPrice = totalPrice.add(product.getUnitPrice());
                products.add(product);
            }
            totalPrice = totalPrice.setScale(2, RoundingMode.HALF_UP);
            ProductList productList = new ProductList(products,totalPrice);

            ProductListConsoleWriter productListConsoleWriter = new ProductListConsoleWriter();
            productListConsoleWriter.write(productList);

        } catch(Exception e) {
            System.out.println("Could not complete GroceryScraper due to the following...");
            e.printStackTrace();
        }
    }

    private String getProductInfoUri(Element element) {
        String productInfoUri = element.attr("href");
        return productInfoUri.replace("../../../../../../","https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/");
    }

    public static void main(String args[]) {
        if(args.length != 1) {
            printUsage();
        } else {
            new GroceryScraper().scrape(args[0]);
        }
    }

    private static void printUsage() {
        System.out.println("GroceryScraper requires a URL as an argument");
    }
}