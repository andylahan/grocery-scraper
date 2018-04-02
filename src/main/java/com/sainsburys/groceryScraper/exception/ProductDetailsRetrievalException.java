package com.sainsburys.groceryScraper.exception;

public class ProductDetailsRetrievalException extends RuntimeException {

    public ProductDetailsRetrievalException(String uri, Exception e) {
        super("Could not retrieve product details from "+uri, e);
    }
}
