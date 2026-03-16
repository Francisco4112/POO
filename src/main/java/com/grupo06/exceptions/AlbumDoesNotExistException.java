package com.grupo06.exceptions;

/**
 * Exception thrown when an album is not found in the system.
 */
public class AlbumDoesNotExistException extends Exception {

  /**
   * Constructs a new AlbumDoesNotExistException with the specified detail
   * message.
   *
   * @param msg the detail message explaining the exception.
   */
  public AlbumDoesNotExistException(String msg) {
    super(msg);
  }
}
