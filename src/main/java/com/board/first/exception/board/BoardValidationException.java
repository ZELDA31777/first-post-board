package com.board.first.exception.board;

import com.board.first.exception.BoardAppException;

// Unchecked Exception 으로 처리할 예정.
public class BoardValidationException extends BoardAppException {
    public BoardValidationException(String message){
        super(message);
    }

    public BoardValidationException(String message, Throwable cause){
        super(message, cause);
    }

}
