package com.bci.users.exceptions;

import java.time.ZonedDateTime;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BCIExceptionHandler {

  @ExceptionHandler(BindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ExceptionDetail> handleValidationException(BindException e) {
    var detail =
        new ExceptionDetail(
            HttpStatus.BAD_REQUEST.value(),
            e.getFieldErrors().get(0).getDefaultMessage(),
            ZonedDateTime.now());
    return new ResponseEntity<>(detail, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ExceptionDetail> handleMissingRequestParameterException(
      MissingServletRequestParameterException e) {
    var detail =
        new ExceptionDetail(HttpStatus.BAD_REQUEST.value(), e.getMessage(), ZonedDateTime.now());
    return new ResponseEntity<>(detail, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ExceptionDetail> handleDataIntegrityViolationException(
      DataIntegrityViolationException e) {
    var detail =
        new ExceptionDetail(HttpStatus.CONFLICT.value(), e.getMessage(), ZonedDateTime.now());
    return new ResponseEntity<>(detail, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(ConflictException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponseEntity<ExceptionDetail> handleConflictException(ConflictException e) {
    var detail =
        new ExceptionDetail(HttpStatus.CONFLICT.value(), e.getMessage(), ZonedDateTime.now());
    return new ResponseEntity<>(detail, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(NotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ExceptionDetail> handleNotFoundException(NotFoundException e) {
    var detail =
        new ExceptionDetail(HttpStatus.NOT_FOUND.value(), e.getMessage(), ZonedDateTime.now());
    return new ResponseEntity<>(detail, HttpStatus.NOT_FOUND);
  }
}
