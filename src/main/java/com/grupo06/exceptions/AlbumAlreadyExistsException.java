package com.grupo06.exceptions;

/**
 * Exception thrown when attempting to create an album that already exists.
 */
public class AlbumAlreadyExistsException extends Exception {

    /**
     * Constructs a new AlbumAlreadyExistsException with the specified detail
     * message.
     *
     * @param msg the detail message explaining the exception.
     */
    public AlbumAlreadyExistsException(String msg) {
        super(msg);
    }
}
