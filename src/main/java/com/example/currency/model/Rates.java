package com.example.currency.model;

import com.example.currency.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@JsonIgnoreProperties (ignoreUnknown = true)
public class Rates implements Serializable {

    List<Rate> rateList;

    List<HistoryRate> historyRateList;

    public Rates() {}

    public Rates(String code, Double value) {
        Rate rate = new Rate(code, value);
        this.rateList.add(rate);
    }

    public Optional<List<Rate>> getRateList() {
        return Optional.ofNullable(rateList);
    }

    public Optional<List<HistoryRate>> getHistoryRateList() {
        return Optional.ofNullable(historyRateList);
    }

    @JsonAnySetter
    public void setRateModel(String key, Object value) throws JsonProcessingException {
        if (key.length() == 3) createRateList(key, value);
        else createRateHistoryList(key, value);
    }

    private void createRateList(String key, Object value) {
        if (this.rateList == null) this.rateList = new ArrayList<>();
        Rate rate = new Rate(key, (Double) value);
        this.rateList.add(rate);
    }

    private void createRateHistoryList(String key, Object value) throws JsonProcessingException {
        if (this.historyRateList == null) this.historyRateList = new ArrayList<>();
        HistoryRate historyRate = new HistoryRate(DateUtil.convertToLocalDate(key));
        historyRate.parseRateList(value);
        this.historyRateList.add(historyRate);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("\n{");
        getRateList().orElse(new ArrayList<Rate>()).stream().forEach(rate -> builder.append(rate.toString() + ",\n"));
        builder.append("}");
        return builder.toString();
    }
}
