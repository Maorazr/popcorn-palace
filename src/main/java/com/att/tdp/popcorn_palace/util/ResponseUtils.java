package com.att.tdp.popcorn_palace.util;

import org.springframework.http.ResponseEntity;

public class ResponseUtils {

  private ResponseUtils() {}

  public static ResponseEntity<Void> ok() {
    return ResponseEntity.ok().build();
  }
}
