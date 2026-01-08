package com.bridgelabz.fundoo.exception;

public class InvalidRequestException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 8516981278525649631L;

    public InvalidRequestException(String message) {
        super(message);
    }
}
