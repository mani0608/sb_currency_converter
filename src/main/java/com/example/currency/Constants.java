package com.example.currency;

public enum Constants {

    //Cache Constants
    LATEST_CODES("latestCodes"),
    VALID_CODES("validCodes"),
    CURRENT_CACHE("currencyCache");

    private String key;

    Constants(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
