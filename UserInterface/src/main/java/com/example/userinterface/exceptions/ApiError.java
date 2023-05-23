package com.example.userinterface.exceptions;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Builder
public class ApiError implements Serializable {

    private HttpStatus status;
    private String message;
    private List<String> errors;

    //

    public ApiError() {
        super();
    }

    public ApiError(final HttpStatus status, final String message, final List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(final HttpStatus status, final String message, final String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Collections.singletonList(error);
    }

    //

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(final HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(final List<String> errors) {
        this.errors = errors;
    }

    public void setError(final String error) {
        errors = Collections.singletonList(error);
    }

}