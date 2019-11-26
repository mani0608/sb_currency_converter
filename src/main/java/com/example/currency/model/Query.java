package com.example.currency.model;

import com.example.currency.annotations.CurrencyCodeConstraint;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@JsonIgnoreProperties (ignoreUnknown = true)
public class Query implements Serializable {
    @CurrencyCodeConstraint
    private String base;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @JsonProperty("symbols")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @CurrencyCodeConstraint
    private String symbols;

    private TimePeriod period;

    @JsonIgnore
    private boolean isHistoryQuery;

    public Optional<String> getBase() {
        return Optional.ofNullable(base);
    }

    public void setBase(String base) {
        this.base = base;
    }

    @JsonIgnore
    public Optional<LocalDate> getDate() {
        return Optional.ofNullable(date);
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @JsonIgnore
    public Optional<String> getSymbols() {
        return Optional.ofNullable(symbols);
    }

    public void setSymbols(String symbols) {
        this.symbols = symbols;
    }

    public Optional<TimePeriod> getPeriod() {
        return Optional.ofNullable(period);
    }

    public void setPeriod(TimePeriod period) {
        this.period = period;
    }

    public boolean hasValue() {
        return (getBase().isPresent() || getDate().isPresent() || getPeriod().isPresent() || getSymbols().isPresent());
    }

    public boolean isHistoryQuery() {
        return isHistoryQuery;
    }

    public void setHistoryQuery(boolean historyQuery) {
        isHistoryQuery = historyQuery;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("query: {\nbase: " + getBase().orElse("") + "\ndate: " + getDate().orElse(null) + "\nsymbols: ")
                .append(String.join(",", getSymbols().orElse("")) + "\ntimePeriod: {\n" + getPeriod().orElse(new TimePeriod()).toString() + "\n}");
        return builder.toString();

    }
}
