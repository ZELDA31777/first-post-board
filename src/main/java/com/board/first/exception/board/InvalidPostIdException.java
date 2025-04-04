package com.board.first.exception.board;

import com.board.first.exception.post.PostValidationException;

public class InvalidPostIdException extends PostValidationException {
    public InvalidPostIdException(String postIdString){
        super(postIdString + "은/는 정수가 아닙니다.");
    }

    public InvalidPostIdException(String postIdString, Throwable cause) {
        super(postIdString, cause);
    }

}
