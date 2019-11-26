package com.example.currency.api;

import com.example.currency.model.CurrencyRate;
import com.example.currency.model.Query;

import java.time.LocalDate;
import java.util.List;

public interface CurrencyService {
    public CurrencyRate getLatestRates();
    public List<String> getValidCodes();
    public CurrencyRate getLatestWithBase(String base);
    public CurrencyRate getHistoricalRate(LocalDate date);
    public CurrencyRate getRateForSymbols(String symbols);
    public CurrencyRate getRateForTimePeriod(LocalDate startAt, LocalDate endAt);
    public CurrencyRate getRates(Query query);
    public List<CurrencyRate> getConversionConfig(String symbols);
}
