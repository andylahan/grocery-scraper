package com.sainsburys.groceryScraper.exception;

public class ProductDetailsFormatException extends RuntimeException {

    public ProductDetailsFormatException(String uri, Exception e) {
        super("Exception parsing product details page "+uri,e);
    }
}
