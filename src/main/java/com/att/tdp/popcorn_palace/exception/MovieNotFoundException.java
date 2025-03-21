package com.att.tdp.popcorn_palace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MovieNotFoundException extends RuntimeException {
  public MovieNotFoundException(Long id) {
    super("Movie not found with ID = " + id);
  }

  public MovieNotFoundException(String title) {
    super("Movie not found with title = '" + title + "'");
  }
}
