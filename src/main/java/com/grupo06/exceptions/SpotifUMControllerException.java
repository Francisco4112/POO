package com.grupo06.exceptions;

/**
 * Exception thrown when an error occurs in the SpotifUM controller layer.
 */
public class SpotifUMControllerException extends Exception {

  /**
   * Constructs a new SpotifUMControllerException with the specified detail
   * message.
   *
   * @param msg the detail message describing the controller error.
   */
  public SpotifUMControllerException(String msg) {
    super(msg);
  }
}
