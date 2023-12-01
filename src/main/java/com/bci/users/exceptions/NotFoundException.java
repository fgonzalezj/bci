package com.bci.users.exceptions;

public class NotFoundException extends Exception {

  private static final long serialVersionUID = -188570092937212784L;

  public NotFoundException(String message) {
    super(message);
  }
}
