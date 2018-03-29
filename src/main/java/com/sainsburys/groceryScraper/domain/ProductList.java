package com.sainsburys.groceryScraper.domain;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ProductList {

    private ArrayList<Product> products;
    private BigDecimal totalPrice;

    public ProductList(ArrayList<Product> product, BigDecimal totalPrice) {
        this.products = products;
        this.totalPrice = totalPrice;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
}
