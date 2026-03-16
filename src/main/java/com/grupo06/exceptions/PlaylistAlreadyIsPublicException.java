package com.grupo06.exceptions;

/**
 * Exception thrown when attempting to make a playlist public that is already
 * public.
 */
public class PlaylistAlreadyIsPublicException extends Exception {

    /**
     * Constructs a new PlaylistAlreadyIsPublicException with the specified detail
     * message.
     *
     * @param msg the detail message explaining the exception.
     */
    public PlaylistAlreadyIsPublicException(String msg) {
        super(msg);
    }
}
