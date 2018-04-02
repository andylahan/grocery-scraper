package com.sainsburys.groceryScraper.domain;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class Product {

    private String title;

    @SerializedName("kcal_per_100g")
    private Integer kcalPer100g;

    @SerializedName("unit_price")
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
