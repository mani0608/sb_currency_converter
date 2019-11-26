package com.example.currency.core;

import com.example.currency.Constants;
import com.example.currency.api.CacheManagerService;
import com.example.currency.api.CurrencyService;
import com.example.currency.exceptions.CurrencyConverterException;
import com.example.currency.model.*;
import com.example.currency.util.CurrencyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class CurrencyServiceImpl implements CurrencyService {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyServiceImpl.class);

    @Value("${app.api.currency.server}")
    private String currencyServerUrl;

    @Value("${app.api.currency.default}")
    private String defaultCurrencyBase;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    CacheManagerService cacheManagerService;

    @Override
    @Cacheable(value = "currencyCache", key = "'latestRates'")
    public CurrencyRate getLatestRates() {
        logger.info("CurrencyServiceImpl.getLatestRates");
        String requestUrl = currencyServerUrl + "/latest";
        CurrencyRate currencyRate = restTemplate.getForObject(requestUrl, CurrencyRate.class);

        if (currencyRate == null || !currencyRate.ratesAvailable()) {
            logger.error("Unknown error while fetching rates");
            throw new CurrencyConverterException(HttpStatus.INTERNAL_SERVER_ERROR, new Message("error.api.NO_CURRENCY_RATES"));
        }

        if (!cacheManagerService.hasKey(Constants.CURRENT_CACHE.getKey(),
                Constants.VALID_CODES.getKey())) {
            extractAndCacheValidCodes(currencyRate);
        }

        Collections.sort(currencyRate.getRates().get().getRateList().get());

        logger.info("CurrencyServiceImpl.getLatestRates - response: " + currencyRate);
        return currencyRate;
    }

    public List<String> getValidCodes() {
        List<String> validCodes = (List<String>) cacheManagerService.getCacheForKey(Constants.CURRENT_CACHE.getKey(),
                Constants.VALID_CODES.getKey());

        if (validCodes == null) {
            String requestUrl = currencyServerUrl + "/latest";
            CurrencyRate currencyRate = restTemplate.getForObject(requestUrl, CurrencyRate.class);
            if (currencyRate == null || !currencyRate.ratesAvailable()) {
                logger.error("Unknown error while fetching rates");
                throw new CurrencyConverterException(HttpStatus.INTERNAL_SERVER_ERROR, new Message("error.api.NO_CURRENCY_RATES"));
            }
            validCodes = extractAndCacheValidCodes(currencyRate);
        }

        return validCodes;

    }

    public List<String> extractAndCacheValidCodes(CurrencyRate currencyRate) {
        if (currencyRate.getRates().isPresent() && currencyRate.getRates().get().getRateList().isPresent()) {
            return cacheCurrencyCodes(currencyRate.getRates().get().getRateList().get(), currencyRate.getBase().get());
        }
        return null;
    }

    private List<String> cacheCurrencyCodes(List<Rate> rates, String base) {
        List<String> validCodes = rates.stream().map(rate -> rate.getCurrencyCode().get().toUpperCase()).collect(Collectors.toList());
        validCodes.add(base);
        Collections.sort(validCodes);
        cacheManagerService.addToCache(Constants.CURRENT_CACHE.getKey(),
                Constants.VALID_CODES.getKey(), validCodes);
        return validCodes;
    }

    @Override
    @Cacheable(value = "currencyCache", key = "#base")
    public CurrencyRate getLatestWithBase(String base) {
        logger.info("CurrencyServiceImpl.getLatestWithBase - base: " + base);
        String requestUrl = currencyServerUrl + "/latest?base=" + base;

        //Trigger API call to fet rates
        CurrencyRate currencyRate = fetch(requestUrl, base);

        if (currencyRate == null || !currencyRate.ratesAvailable()) {
            logger.error("Invalid criteria: " + base);
            throw new CurrencyConverterException(HttpStatus.NOT_FOUND, new Message("error.api.NO_CURRENCY_RATES", base));
        }
        Collections.sort(currencyRate.getRates().get().getRateList().get());
        logger.info("CurrencyServiceImpl.getLatestWithBase - response: " + currencyRate);
        return currencyRate;
    }

    @Override
    public CurrencyRate getHistoricalRate(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        logger.info("CurrencyServiceImpl.getHistoricalRate - date: " + date);
        String requestUrl = currencyServerUrl + "/" + date;

        //Trigger API call to fet rates
        CurrencyRate currencyRate = fetch(requestUrl, date);

        if (currencyRate == null || !currencyRate.ratesAvailable()) {
            logger.error("Invalid criteria: " + date);
            throw new CurrencyConverterException(HttpStatus.NOT_FOUND, new Message("error.api.NO_CURRENCY_RATES", date));
        }
        Collections.sort(currencyRate.getRates().get().getRateList().get());
        logger.info("CurrencyServiceImpl.getHistoricalRate - response: " + currencyRate);
        return currencyRate;
    }

    @Override
    public CurrencyRate getRateForSymbols(String symbols) {
        logger.info("CurrencyServiceImpl.getRateForSymbols - symbols: " + symbols);
        String requestUrl = currencyServerUrl + "/latest?symbols=" + symbols;

        //Trigger API call to fet rates
        CurrencyRate currencyRate = fetch(requestUrl, symbols);

        if (currencyRate == null || !currencyRate.ratesAvailable()) {
            logger.error("Invalid criteria: " + symbols);
            throw new CurrencyConverterException(HttpStatus.NOT_FOUND, new Message("error.api.NO_CURRENCY_RATES", symbols));
        }
        Collections.sort(currencyRate.getRates().get().getRateList().get());
        logger.info("CurrencyServiceImpl.getRateForSymbols - response: " + currencyRate);
        return currencyRate;
    }

    @Override
    public CurrencyRate getRateForTimePeriod(LocalDate startAt, LocalDate endAt) {
        logger.info("CurrencyServiceImpl.getRateForTimePeriod - startAt: " + startAt + " endAt: " + endAt);
        String requestUrl = currencyServerUrl + "/history?start_at=" + startAt + "&end_at=" + endAt;
        TimePeriod period = new TimePeriod(startAt, endAt);

        //Trigger API call to fet rates
        CurrencyRate currencyRate = fetch(requestUrl, period);

        if (currencyRate == null || !currencyRate.ratesAvailable()) {
            logger.error("Invalid criteria: " + period);
            throw new CurrencyConverterException(HttpStatus.NOT_FOUND, new Message("error.api.NO_CURRENCY_RATES", period));
        }
        Collections.sort(currencyRate.getRates().get().getHistoryRateList().get());
        logger.info("CurrencyServiceImpl.getRateForTimePeriod - response: " + currencyRate);
        return currencyRate;
    }

    @Override
    public CurrencyRate getRates(Query query) {
        logger.info("CurrencyServiceImpl.getRates - query: " + query);

        if (!query.hasValue()) {
            logger.error("query doesn't contain any value: " + query);
            throw new CurrencyConverterException(HttpStatus.NOT_FOUND, new Message("error.api.NO_CURRENCY_RATES", query));
        }

        String requestUrl = null;

        if (query.getPeriod().isPresent()) {
            query.setHistoryQuery(true);
            requestUrl = currencyServerUrl + "/history" + CurrencyHelper.createQuery(query);
        } else  {
            query.setHistoryQuery(false);
            requestUrl = currencyServerUrl + "/latest" + CurrencyHelper.createQuery(query);
        }

        //Trigger API call to fet rates
        CurrencyRate currencyRate = fetch(requestUrl, query);

        if (currencyRate == null || !currencyRate.ratesAvailable()) {
            logger.error("Invalid criteria: " + query);
            throw new CurrencyConverterException(HttpStatus.NOT_FOUND, new Message("error.api.NO_CURRENCY_RATES", query));
        }

        if (query.isHistoryQuery()) {
            Collections.sort(currencyRate.getRates().get().getHistoryRateList().get());
        } else {
            Collections.sort(currencyRate.getRates().get().getRateList().get());
        }

        logger.info("CurrencyServiceImpl.getRateForTimePeriod - response: " + currencyRate);
        return currencyRate;
    }

    @Override
    public List<CurrencyRate> getConversionConfig(String symbols) {
        logger.info("CurrencyServiceImpl.getConversionConfig - symbols: ", symbols);

        List<String> symbolList = Arrays.asList(symbols.split("\\s*,\\s*"));

        if (symbolList.size() < 2) {
            logger.error("getConversionConfig - two symbols required for conversion",  symbols);
            throw new CurrencyConverterException(HttpStatus.BAD_REQUEST, new Message("error.api.NO_CURRENCY_RATES", symbols));
        }

        List<CurrencyRate> currencyRates = new ArrayList<>();
        Query query = new Query();
        query.setBase(symbolList.get(0));
        query.setSymbols(symbolList.get(1));

        currencyRates.add(getRates(query));

        query.setBase(symbolList.get(1));
        query.setSymbols(symbolList.get(0));

        currencyRates.add(getRates(query));
        return currencyRates;
    }

    private CurrencyRate fetch (String requestUrl, Object param) {
        CurrencyRate currencyRate = null;
        try {
            currencyRate = restTemplate.getForObject(requestUrl, CurrencyRate.class);
        }catch (Exception ex) {
            logger.error("getLatestWithBase - Error while fetching rates for base: " + param);
            logger.error("Exception: " , ex);
            throw new CurrencyConverterException(HttpStatus.BAD_REQUEST, new Message("error.api.NO_CURRENCY_RATES", param), ex);
        }

        return currencyRate;
    }
}
