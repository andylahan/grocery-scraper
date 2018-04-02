package com.sainsburys.groceryScraper.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sainsburys.groceryScraper.domain.ProductList;

public class ProductListConsoleWriter {

    public void write(ProductList productList) {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        String json = gson.toJson(productList);
        System.out.println(json);
    }
}
