package com.example.currency.model;

import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

public class ApiError implements Serializable {
    private String details;
    private String message;
    private LocalDateTime timestamp;
    private HttpStatus status;

    public ApiError(HttpStatus status) {
        this.status = status;
    }

    public ApiError(HttpStatus status, Throwable ex) {
        this.message = "Unexpected Error";
        this.details = ex.getLocalizedMessage();
        this.timestamp = LocalDateTime.now();
        this.status = status;
    }

    public ApiError(HttpStatus status, String message, Throwable ex) {
        this.message = message;
        this.details = ex.getLocalizedMessage();
        this.timestamp = LocalDateTime.now();
        this.status = status;
    }

    public Optional<String> getDetails() {
        return Optional.ofNullable(details);
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Optional<String> getMessage() {
        return Optional.ofNullable(message);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Optional<LocalDateTime> getTimestamp() {
        return Optional.ofNullable(timestamp);
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Optional<HttpStatus> getStatus() {
        return Optional.ofNullable(status);
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Error: {\nmessage: " + getMessage().orElse("")).append(",")
                .append("\ndetails: " + getDetails().orElse("")).append(",")
                .append("\ntimestamp: " + getTimestamp().orElse(null))
                .append("\nstatus: " + getStatus().orElse(HttpStatus.INTERNAL_SERVER_ERROR))
                .append("\n}");
        return builder.toString();
    }
}
