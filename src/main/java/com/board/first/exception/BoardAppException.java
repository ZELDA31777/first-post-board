package com.board.first.exception;

import com.board.first.data.ErrorCode;

public class BoardAppException extends RuntimeException {
    private final ErrorCode errorCode;

    // ErrorCode에 할당한 ErrorMessage로 대체
    public BoardAppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BoardAppException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
