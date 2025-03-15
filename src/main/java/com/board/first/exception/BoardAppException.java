package com.board.first.exception;

public class BoardAppException extends RuntimeException{

    public BoardAppException() {
        super();
    }

    public BoardAppException(String message) {
        super(message);
    }

    public BoardAppException(String message, Throwable cause) {
        super(message, cause);
    }

    public BoardAppException(Throwable cause){
        super(cause);
    }
}
