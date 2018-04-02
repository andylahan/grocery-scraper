package com.sainsburys.groceryScraper.service;

import com.sainsburys.groceryScraper.domain.Product;
import com.sainsburys.groceryScraper.exception.ProductDetailsFormatException;
import com.sainsburys.groceryScraper.exception.ProductDetailsRetrievalException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ProductScraper {

    public static Product scrapeProductInformation(String productURI) {

        try {
            Document productInfoDocument = Jsoup.connect(productURI).get();

            String fullDescription = getDescription(productInfoDocument);
            BigDecimal pricePerUnit = getPricePerUnit(productInfoDocument);
            String title = getTitle(productInfoDocument);
            Integer kcals = getKCals(productInfoDocument);

            return new Product(title, kcals, pricePerUnit, fullDescription);
        } catch(IOException ioe) {
            throw new ProductDetailsRetrievalException(productURI, ioe);
        } catch (Exception e) {
            throw new ProductDetailsFormatException(productURI, e);
        }
    }

    private static String getDescription(Document document) {
        return document.select("div.productText").get(0).select("p").get(0).text();
    }

    private static BigDecimal getPricePerUnit(Document document) {
        String pricePerUnit = document.select("div.pricingAndTrolleyOptions").select("div.pricing")
                .select("p.pricePerUnit").text();
        return new BigDecimal(pricePerUnit.replaceAll("[^\\d.]","")).setScale(2, RoundingMode.HALF_UP);
    }

    private static String getTitle(Document document) {
        return document.getElementsByClass("productTitleDescriptionContainer").select("h1").text();
    }

    private static Integer getKCals(Document document) {
        try {
            Element nutritionTable = document.selectFirst("table.nutritionTable");
            Elements rows = nutritionTable.select("tr");

            for (Element row : rows) {
                Elements columns = row.select("td");
                if (columns.size() > 0 && columns.get(0).text().contains("kcal")) {
                    String kcals = columns.get(0).text();
                    return Integer.parseInt(kcals.replaceAll("[^\\d]", ""));
                }
            }
            return null;
        } catch(Exception e) {
            return null;
        }
    }
}
