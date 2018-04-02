package com.sainsburys.groceryScraper.domain;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.List;

public class ProductList {

    @SerializedName("results")
    private List<Product> productList;

    @SerializedName("total")
    private BigDecimal totalPrice;

    public ProductList(List<Product> productList, BigDecimal totalPrice) {
        this.productList = productList;
        this.totalPrice = totalPrice;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
}
