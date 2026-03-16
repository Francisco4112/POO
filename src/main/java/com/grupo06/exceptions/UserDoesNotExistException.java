package com.grupo06.exceptions;

/**
 * Exception thrown when a requested user does not exist in the system.
 */
public class UserDoesNotExistException extends Exception {

  /**
   * Constructs a new UserDoesNotExistException with the specified detail message.
   *
   * @param msg the detail message explaining the exception.
   */
  public UserDoesNotExistException(String msg) {
    super(msg);
  }
}
