package com.att.tdp.popcorn_palace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class MovieAlreadyExistsException extends RuntimeException {
  public MovieAlreadyExistsException(String title) {
    super("Movie titled '" + title + "' already exists.");
  }
}

