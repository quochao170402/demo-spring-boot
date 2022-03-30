package com.quochao.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    //    Custom Resource not found exception
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
