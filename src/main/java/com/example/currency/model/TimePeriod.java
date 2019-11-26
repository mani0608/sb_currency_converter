package com.example.currency.model;

import com.example.currency.util.DateUtil;
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
import java.util.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TimePeriod implements Serializable {

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("start_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startAt;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("end_at")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endAt;

    public TimePeriod() {}

    public TimePeriod(LocalDate startAt, LocalDate endAt) {
        this.startAt = startAt;
        this.endAt = endAt;
    }

    @JsonIgnore
    public Optional<LocalDate> getStartAt() {
        return Optional.ofNullable(startAt);
    }

    public void setStartAt(LocalDate startAt) {
        this.startAt = startAt;
    }

    @JsonIgnore
    public Optional<LocalDate> getEndAt() {
        return Optional.ofNullable(endAt);
    }

    public void setEndAt(LocalDate endAt) {
        this.endAt = endAt;
    }

    @JsonIgnore
    public void setKeyValue (String key, Object value) {
        if (key.equals("start_at")) setStartAt(DateUtil.convertToLocalDate(value));
        if (key.equals("end_at")) setEndAt(DateUtil.convertToLocalDate(value));
    }

    @Override
    public String toString() {
        return "{\nstartAt: " + getStartAt().orElse(null) + " \nendAt: " + getEndAt().orElse(null) + "\n}";
    }
}
