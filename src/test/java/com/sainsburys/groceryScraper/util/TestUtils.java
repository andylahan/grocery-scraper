package com.sainsburys.groceryScraper.util;

public class TestUtils {

    public static String unifyLineEndings(String inputString) {
        return inputString.replaceAll("(\r\n|\r|\n)", "\n");
    }
}
