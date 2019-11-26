package com.example.currency.exceptions;

import com.example.currency.model.ApiError;
import com.example.currency.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;
import java.util.Optional;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CurrencyConverterExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyConverterExceptionHandler.class);

    @Autowired
    MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = "UnSupported HTTP Method";
        logger.error("handleHttpRequestMethodNotSupported - Exception: " , ex);
        return buildResponseEntity(new ApiError(status, message, ex));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = "UnSupported Media Type";
        logger.error("handleHttpMediaTypeNotSupported - Exception: " , ex);
        return buildResponseEntity(new ApiError(status, message, ex));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = "UnSupported not acceptable";
        logger.error("handleHttpMediaTypeNotAcceptable - Exception: " , ex);
        return buildResponseEntity(new ApiError(status, message, ex));
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = "Your request is missing a required path variable";
        logger.error("handleMissingPathVariable - Exception: " , ex);
        return buildResponseEntity(new ApiError(status, message, ex));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = "JSON Malformed in your request";
        logger.error("handleHttpMessageNotReadable - Exception: " , ex);
        return buildResponseEntity(new ApiError(status, message, ex));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = "JSON Malformed in your request";
        logger.error("handleHttpMessageNotWritable - Exception: " , ex);
        return buildResponseEntity(new ApiError(status, message, ex));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = "Method Argument is not valid";
        logger.error("handleMethodArgumentNotValid - Exception: " , ex);
        return buildResponseEntity(new ApiError(status, message, ex));
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = "Error while binding request parameter";
        logger.error("handleBindException - Exception: " , ex);
        return buildResponseEntity(new ApiError(status, message, ex));
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = "Handler not found for request";
        logger.error("handleNoHandlerFoundException - Exception: " , ex);
        return buildResponseEntity(new ApiError(status, message, ex));
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        String message = "Async request has timed out";
        logger.error("handleAsyncRequestTimeoutException - Exception: " , ex);
        return buildResponseEntity(new ApiError(status, message, ex));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = "Internal Server Error";
        logger.error("handleExceptionInternal - Exception: " , ex);
        return buildResponseEntity(new ApiError(status, message, ex));
    }

    @ExceptionHandler(JsonProcessingException.class)
    protected ResponseEntity<Object> handleJsonProcessingException(JsonProcessingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = "Error while trying to map json to bean";
        logger.error("handleJsonProcessingException - Exception: " , ex);
        return buildResponseEntity(new ApiError(status, message, ex));
    }

    @ExceptionHandler(CurrencyConverterException.class)
    protected ResponseEntity<Object> handleCurrencyConverterException(CurrencyConverterException ex, WebRequest request) {
        String message = "Bad Request";
        return buildResponseEntity(new ApiError(ex.getStatus().orElse(HttpStatus.BAD_REQUEST), getMessage(ex.getMessageModel()), ex));
    }

    private String getMessage(Optional<Message> optionalMessage) {
        Message message = optionalMessage.get();
        Object[] content = (message.getContent().isPresent()) ? new Object[] { message.getContent().get() } : new Object[] {};
        String key = message.getKey().orElse("error.api.UNEXPECTED_ERROR");
        String fetchedMessage = messageSource.getMessage(key, content, "Unknown error", Locale.getDefault());
        return fetchedMessage;
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus().orElse(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
