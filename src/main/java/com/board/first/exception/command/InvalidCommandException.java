package com.board.first.exception.command;

public class InvalidCommandException extends CommandValidationException {
    public InvalidCommandException(String command) {
        super(command + "은/는 존재하지 않는 명령어입니다.");
    }
}
