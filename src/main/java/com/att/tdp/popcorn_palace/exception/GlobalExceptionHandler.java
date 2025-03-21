package com.att.tdp.popcorn_palace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("timestamp", LocalDateTime.now());
    errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
    errorResponse.put("error", "Validation Error");
    errorResponse.put("message", "Invalid input provided");

    Map<String, String> validationErrors = new HashMap<>();
    for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
      validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
    }
    errorResponse.put("errors", validationErrors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<Map<String, Object>> handleBadRequestException(BadRequestException ex) {
    return buildErrorResponse(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
  }

  @ExceptionHandler(MovieNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleMovieNotFound(MovieNotFoundException ex) {
    return buildErrorResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
  }

  @ExceptionHandler(ShowtimeNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleShowtimeNotFound(ShowtimeNotFoundException ex) {
    return buildErrorResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
  }

  @ExceptionHandler(SeatAlreadyBookedException.class)
  public ResponseEntity<Map<String, Object>> handleSeatAlreadyBookedException(SeatAlreadyBookedException ex) {
    return buildErrorResponse(HttpStatus.CONFLICT, "Conflict", ex.getMessage());
  }

  @ExceptionHandler(OverlappingShowtimeException.class)
  public ResponseEntity<Map<String, Object>> handleOverlappingShowtimeException(OverlappingShowtimeException ex) {
    return buildErrorResponse(HttpStatus.CONFLICT, "Conflict", ex.getMessage());
  }

  @ExceptionHandler(MovieAlreadyExistsException.class)
  public ResponseEntity<Map<String, Object>> handleMovieAlreadyExistsException(MovieAlreadyExistsException ex) {
    return buildErrorResponse(HttpStatus.CONFLICT, "Conflict", ex.getMessage());
  }

  // --------------------------------------------------------

  private ResponseEntity<Map<String, Object>> buildErrorResponse(
      HttpStatus status, String error, String message) {

    Map<String, Object> errorResponse = Map.of(
        "timestamp", LocalDateTime.now(),
        "status", status.value(),
        "error", error,
        "message", message
    );

    return ResponseEntity.status(status).body(errorResponse);
  }
}
