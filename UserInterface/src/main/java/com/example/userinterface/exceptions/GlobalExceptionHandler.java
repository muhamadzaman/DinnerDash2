package com.example.userinterface.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public String handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex, RedirectAttributes attributes) {
        final String error = ex.getName() + " should be of type " + Objects.requireNonNull(ex.getRequiredType()).getName();
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        attributes.addFlashAttribute("error", apiError);
        return "redirect:/";
    }
}
