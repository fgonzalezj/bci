package com.bci.users.exceptions;

public class ConflictException extends Exception {
  private static final long serialVersionUID = -2817630579152808748L;

  public ConflictException(String message) {
    super(message);
  }
}
