package com.board.first.exception.board;

public class PostValidationException extends BoardValidationException {
    public PostValidationException(String message){
        super(message);
    }

    public PostValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
