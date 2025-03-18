package com.board.first.exception.account;

public class AccountDuplicatedException extends AccountValidationException {
    public AccountDuplicatedException(String userId) {
        super(userId + "는 이미 사용 중인 아이디입니다.");
    }
}
