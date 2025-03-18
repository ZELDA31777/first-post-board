package com.board.first.exception.post;

import com.board.first.exception.BoardAppException;

public class PostValidationException extends BoardAppException {
    public PostValidationException(String message){
        super(message);
    }

    public PostValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
