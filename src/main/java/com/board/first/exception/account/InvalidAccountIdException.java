package com.board.first.exception.account;

public class InvalidAccountIdException extends AccountValidationException {
    public InvalidAccountIdException(String accountId) {
        super(accountId + "은/는 정수가 아닙니다.");
    }
}
