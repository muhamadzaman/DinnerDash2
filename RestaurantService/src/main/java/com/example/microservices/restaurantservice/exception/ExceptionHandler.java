package com.example.microservices.restaurantservice.exception;

import com.example.centralrepository.exceptions.GlobalExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler extends GlobalExceptionHandler {
}
