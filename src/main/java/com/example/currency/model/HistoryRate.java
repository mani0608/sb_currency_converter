package com.example.currency.model;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HistoryRate implements Serializable, Comparable<HistoryRate> {

    private LocalDate historyDate;

    private List<Rate> rateList;

    public HistoryRate() {
    }

    public HistoryRate(LocalDate historyDate) {
        this.historyDate = historyDate;
    }

    public Optional<LocalDate> getHistoryDate() {
        return Optional.ofNullable(historyDate);
    }

    public void setHistoryDate(LocalDate historyDate) {
        this.historyDate = historyDate;
    }

    public Optional<List<Rate>> getRateList() {
        return Optional.ofNullable(rateList);
    }

    public void setRateList(List<Rate> rateList) {
        this.rateList = rateList;
    }

    public void parseRateList(Object values) throws JsonProcessingException {
        Map<String, Double> rateValues = (Map<String, Double>) values;
        this.rateList = rateValues.entrySet().stream().map(stringDoubleEntry ->
                new Rate(stringDoubleEntry.getKey(), stringDoubleEntry.getValue())).collect(Collectors.<Rate>toList());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder()
                .append("\nhistoryDate: " + getHistoryDate().orElse(null) + ",");
        getRateList().orElse(new ArrayList<Rate>()).stream().forEach(rate -> builder.append(rate.toString() + ",\n"));
        builder.append("}");
        return builder.toString();
    }

    @Override
    public int compareTo(HistoryRate other) {
        if (this.getHistoryDate().isPresent() && other.getHistoryDate().isPresent()) {
            return this.getHistoryDate().get().compareTo(other.getHistoryDate().get());
        }

        return 0;
    }
}
