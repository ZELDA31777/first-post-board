package com.board.first.controller;

import com.board.first.Request;
import com.board.first.service.AccountService;

public class AccountController implements Controller {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void requestHandler(Request request) {

    }
}
