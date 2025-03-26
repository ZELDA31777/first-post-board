package com.board.first.service;

import com.board.first.Request;
import com.board.first.data.Account;
import com.board.first.exception.account.AccountNotFoundException;
import com.board.first.exception.account.AccountValidationException;

public interface AccountService {
    void signUpAccount(String userId, String password, String username, String email) throws AccountValidationException;
    Account signInAccount(Request request, String userId, String password) throws AccountValidationException;
    void logoutAccount(Request request);

    Account getAccountByUserId(String userId);
    Account getAccountByAccountId(int accountId) throws AccountNotFoundException;
    String deleteAccount(int accountId) throws AccountNotFoundException;
    void updateAccount(int accountId, String password, String email, Request request) throws AccountNotFoundException;
}
