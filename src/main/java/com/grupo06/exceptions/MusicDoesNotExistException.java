package com.grupo06.exceptions;

/**
 * Exception thrown when a specified music is not found.
 */
public class MusicDoesNotExistException extends Exception {

  /**
   * Constructs a new MusicDoesNotExistException with the specified detail
   * message.
   *
   * @param msg the detail message explaining the exception.
   */
  public MusicDoesNotExistException(String msg) {
    super(msg);
  }
}
