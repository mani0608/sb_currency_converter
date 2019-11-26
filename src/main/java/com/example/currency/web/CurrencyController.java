package com.example.currency.web;

import com.example.currency.annotations.CurrencyCodeConstraint;
import com.example.currency.api.CurrencyService;
import com.example.currency.model.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api")
public class CurrencyController {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyController.class);

    @Autowired
    CurrencyService currencyService;

    @GetMapping("/latest")
    public ResponseEntity<?> getLatestRates() {
        return ResponseEntity.ok(currencyService.getLatestRates());
    }

    @GetMapping("/codes")
    public ResponseEntity<?> getAllCodes() {
        return ResponseEntity.ok(currencyService.getValidCodes());
    }

    @GetMapping("/base")
    public ResponseEntity<?> getLatestWithBase(@RequestParam("base") @CurrencyCodeConstraint String base) {
        return ResponseEntity.ok(currencyService.getLatestWithBase(base));
    }

    @GetMapping("/historical")
    public  ResponseEntity<?> getHistoricalRate(@RequestParam("date") LocalDate date) {
        return ResponseEntity.ok(currencyService.getHistoricalRate(date));
    }

    @GetMapping("/symbols")
    public ResponseEntity<?> getRateForSymbols(@RequestParam("symbols") @CurrencyCodeConstraint String symbols) {
        return ResponseEntity.ok(currencyService.getRateForSymbols(symbols));
    }

    @GetMapping("/time-period")
    public ResponseEntity<?> getRateForTimePeriod(@RequestParam("startAt") LocalDate startAt, @RequestParam("endAt") LocalDate endAt) {
        return ResponseEntity.ok(currencyService.getRateForTimePeriod(startAt, endAt));
    }

    @GetMapping("/search")
    public ResponseEntity<?> getRateForTimePeriod(@Valid Query query) {
        return ResponseEntity.ok(currencyService.getRates(query));
    }

    @GetMapping("/config")
    public ResponseEntity<?> config(@RequestParam("symbols") @CurrencyCodeConstraint String symbols) {
        return ResponseEntity.ok(currencyService.getConversionConfig(symbols));
    }

    @GetMapping("/test")
    public ResponseEntity<?> csrf(@RequestBody String content) {
        return ResponseEntity.ok("Request has csrf token: " + content);
    }

}