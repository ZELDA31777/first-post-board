package com.board.first.exception.command;

import com.board.first.exception.BoardAppException;

// 명령어 예외
public class CommandValidationException extends BoardAppException {
    public CommandValidationException(String message) {
        super(message);
    }
}
