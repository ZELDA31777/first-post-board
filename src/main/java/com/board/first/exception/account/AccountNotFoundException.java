package com.board.first.exception.account;

import com.board.first.exception.BoardAppException;

public class AccountNotFoundException extends AccountValidationException {
    public AccountNotFoundException(int accountId) {
        super(accountId + "번 계정은 존재하지 않습니다.");
    }
}
