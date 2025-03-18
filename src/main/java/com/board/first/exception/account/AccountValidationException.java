package com.board.first.exception.account;

import com.board.first.exception.BoardAppException;

public class AccountValidationException extends BoardAppException {
    public AccountValidationException(String message) {
        super(message);
    }
}
