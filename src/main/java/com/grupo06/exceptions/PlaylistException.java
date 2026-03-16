package com.grupo06.exceptions;

/**
 * General exception related to playlist operations.
 */
public class PlaylistException extends Exception {

  /**
   * Constructs a new PlaylistException with the specified detail message.
   *
   * @param msg the detail message describing the playlist error.
   */
  public PlaylistException(String msg) {
    super(msg);
  }
}
