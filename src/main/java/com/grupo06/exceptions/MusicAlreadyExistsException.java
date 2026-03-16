package com.grupo06.exceptions;

/**
 * Exception thrown when attempting to add a music that already exists.
 */
public class MusicAlreadyExistsException extends Exception {

  /**
   * Constructs a new MusicAlreadyExistsException with the specified detail
   * message.
   *
   * @param msg the detail message explaining the exception.
   */
  public MusicAlreadyExistsException(String msg) {
    super(msg);
  }
}
