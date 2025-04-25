package com.floo.order_service.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.floo.order_service.model.OrderStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException cause = (InvalidFormatException) ex.getCause();
            if (cause.getTargetType() != null && cause.getTargetType().isEnum()) {
                Class<?> enumClass = cause.getTargetType();
                if (enumClass == OrderStatus.class) {
                    String validValues = Arrays.stream(OrderStatus.values())
                            .map(Enum::name)
                            .collect(Collectors.joining(", "));

                    return new ResponseEntity<>(
                            "Invalid order status: '" + cause.getValue() + "'. Valid values are: " + validValues,
                            HttpStatus.BAD_REQUEST);
                }
            }
        }

        return new ResponseEntity<>("Invalid request format: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}