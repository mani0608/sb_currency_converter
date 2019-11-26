package com.example.currency.exceptions;

import com.example.currency.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Optional;

public class CurrencyConverterException extends RuntimeException {

    @Autowired
    MessageSource messageSource;

    private String details;
    private Message messageModel;
    private LocalDateTime timestamp;
    private HttpStatus status;

    public CurrencyConverterException(HttpStatus status) {
        this.status = status;
    }

    public CurrencyConverterException(HttpStatus status, Throwable ex) {
        this.messageModel = new Message("error.api.UNEXPECTED_ERROR");
        this.details = ex.getLocalizedMessage();
        this.timestamp = LocalDateTime.now();
        this.status = status;
    }

    public CurrencyConverterException(HttpStatus status, Message message) {
        this.messageModel = message;
        this.timestamp = LocalDateTime.now();
        this.status = status;
    }

    public CurrencyConverterException(HttpStatus status, Message message, Throwable ex) {
        this.messageModel = message;
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

    public Optional<Message> getMessageModel() {
        return Optional.ofNullable(messageModel);
    }

    public void setMessageModel (Message messageModel) {
        this.messageModel = messageModel;
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
    public String getLocalizedMessage() {
        return getDetails().orElse(null);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Error: {\nmessage: " + getMessageModel().orElse(new Message())).append(",")
                .append("\ndetails: " + getDetails().orElse("")).append(",")
                .append("\ntimestamp: " + getTimestamp().orElse(null))
                .append("\nstatus: " + getStatus().orElse(HttpStatus.BAD_REQUEST))
                .append("\n}");
        return builder.toString();
    }

}
