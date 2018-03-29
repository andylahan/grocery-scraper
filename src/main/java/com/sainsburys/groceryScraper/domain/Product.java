package com.sainsburys.groceryScraper.domain;

import java.math.BigDecimal;

public class Product {

    private String title;
    private Integer kcalPer100g;
    private BigDecimal unitPrice;
    private String description;

    public Product(String title, Integer kcalPer100g, BigDecimal unitPrice, String description) {
        this.title = title;
        this.kcalPer100g = kcalPer100g;
        this.unitPrice = unitPrice;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public Integer getKcalPer100g() {
        return kcalPer100g;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public String getDescription() {
        return description;
    }
}
