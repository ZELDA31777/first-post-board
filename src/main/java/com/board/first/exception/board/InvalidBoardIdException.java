package com.board.first.exception.board;

public class InvalidBoardIdException extends BoardValidationException {
    public InvalidBoardIdException(String boardIdString){
        super(boardIdString + "은/는 정수가 아닙니다.");
    }

    public InvalidBoardIdException(String boardIdString, Throwable cause) {
        super(boardIdString, cause);
    }
}
