package com.grupo06.exceptions;

/**
 * Thrown when a specified playlist does not exist in the system.
 */
public class PlaylistDoesNotExistException extends Exception {

  /**
   * Constructs a new PlaylistDoesNotExistException with the specified detail
   * message.
   *
   * @param msg the detail message explaining which playlist was not found.
   */
  public PlaylistDoesNotExistException(String msg) {
    super(msg);
  }
}
