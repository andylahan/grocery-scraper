package com.sainsburys.groceryScraper.service;

import com.sainsburys.groceryScraper.domain.Product;
import com.sainsburys.groceryScraper.exception.ProductDetailsFormatException;
import com.sainsburys.groceryScraper.exception.ProductDetailsRetrievalException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
            Integer kcals = kcalFromV1NutritionTable(nutritionTable);

            return kcals == null ? kcalfromV2NutritionTable(nutritionTable) : kcals;

        } catch(Exception e) {
            return null;
        }
    }

    private static Integer kcalFromV1NutritionTable(Element nutritionTable) {
        try{
            return Integer.parseInt(nutritionTable.select("td:contains(kcal)").first().text().replaceAll("[^\\d]", ""));
        } catch(Exception e) {
            return null;
        }
    }

    private static Integer kcalfromV2NutritionTable(Element nutritionTable) {
        return Integer.parseInt(nutritionTable.select("tr:contains(kcal)").select("td").first().text());
    }
}
