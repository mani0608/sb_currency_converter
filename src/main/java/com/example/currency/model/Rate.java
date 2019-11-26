package com.example.currency.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Rate implements Serializable, Comparable<Rate> {
    private String currencyCode;
    private Double currencyValue;

    public Rate(String code, Double value) {
        this.currencyCode = code;
        this.currencyValue = value;
    }

    public Optional<String> getCurrencyCode() {
        return Optional.ofNullable(currencyCode);
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Optional<Double> getCurrencyValue() {
        return Optional.ofNullable(currencyValue);
    }

    public void setCurrencyValue(Double currencyValue) {
        this.currencyValue = currencyValue;
    }

    @Override
    public String toString() {
        return "{\ncurrencyCode: " + getCurrencyCode().orElse("") + "\ncurrencyValue: " + getCurrencyValue().orElse(0.00) + "\n}";
    }

    @Override
    public int compareTo(Rate other) {

        if (this.getCurrencyCode().isPresent() && other.getCurrencyCode().isPresent()) {
            return this.getCurrencyCode().get().compareTo(other.getCurrencyCode().get());
        }

        return 0;
    }
}
