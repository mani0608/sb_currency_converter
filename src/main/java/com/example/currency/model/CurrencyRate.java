package com.example.currency.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyRate implements Serializable {
    private String base;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @JsonProperty("rates")
    private Rates rates;

    private TimePeriod period;

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

    public Optional<Rates> getRates() {
        return Optional.ofNullable(rates);
    }

    public Optional<TimePeriod> getPeriod() {
        return Optional.ofNullable(period);
    }

    @JsonAnySetter
    public void setPeriod(String key, Object value) {
        if(key.equals("start_at") || key.equals("end_at")) {
            if (period == null) period = new TimePeriod();
            period.setKeyValue(key, value);
        }
    }

    public boolean ratesAvailable() {
         if (!getRates().isPresent()) return false;
        Rates rates = getRates().get();
        return ((rates.getRateList().isPresent() && rates.getRateList().get().size() > 0) ||
                (rates.getHistoryRateList().isPresent() && rates.getHistoryRateList().get().size() > 0));
    }

    public void setRates(Rates rates) {
        this.rates = rates;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("currencyConverter: {\nbase:" + getBase().orElse("") + "\ndate: " + getDate().orElse(null) + "\nRates: [")
                .append(getRates().orElse(new Rates()).toString())
                .append(getPeriod().orElse(new TimePeriod()).toString())
                .append("\n]}");
        return builder.toString();
    }
}
