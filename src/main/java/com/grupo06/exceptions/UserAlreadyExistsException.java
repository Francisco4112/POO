package com.grupo06.exceptions;

/**
 * Exception thrown when attempting to create a user that already exists.
 */
public class UserAlreadyExistsException extends Exception {

    /**
     * Constructs a new UserAlreadyExistsException with the specified detail
     * message.
     *
     * @param msg the detail message explaining the exception.
     */
    public UserAlreadyExistsException(String msg) {
        super(msg);
    }
}
