package com.grupo06.exceptions;

/**
 * Exception thrown when attempting to create a playlist that already exists.
 */
public class PlaylistAlreadyExistsException extends Exception {

  /**
   * Constructs a new PlaylistAlreadyExistsException with the specified detail
   * message.
   *
   * @param msg the detail message explaining the exception.
   */
  public PlaylistAlreadyExistsException(String msg) {
    super(msg);
  }
}
