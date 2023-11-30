package com.bci.users.exceptions;

import java.time.ZonedDateTime;

public class BCIException extends Exception {

  private static final long serialVersionUID = -5157622261482152458L;
  protected final ExceptionDetail detail;

  public BCIException(int code, String detail, ZonedDateTime timestamp) {
    this.detail = new ExceptionDetail(code, detail, ZonedDateTime.now());
  }
}
