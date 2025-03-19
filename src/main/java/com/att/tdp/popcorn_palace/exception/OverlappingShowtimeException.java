package com.att.tdp.popcorn_palace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OverlappingShowtimeException extends RuntimeException{
    public OverlappingShowtimeException(String message) {
        super(message);
    }
}
