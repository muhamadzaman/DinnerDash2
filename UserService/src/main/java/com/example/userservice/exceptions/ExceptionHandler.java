package com.example.userservice.exceptions;

import com.example.centralrepository.exceptions.GlobalExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler extends GlobalExceptionHandler {
}
