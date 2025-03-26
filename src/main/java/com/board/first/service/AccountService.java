package com.board.first.service;

import com.board.first.data.Account;
import com.board.first.exception.account.AccountNotFoundException;
import com.board.first.exception.account.AccountValidationException;

public interface AccountService {
    void signUpAccount(String userId, String password, String username, String email) throws AccountValidationException;
    Account signInAccount(String userId, String password) throws AccountValidationException;
    void logoutAccount(Account account);
    Account getAccountByAccountId(int accountId) throws AccountNotFoundException;
    String deleteAccount(int accountId) throws AccountNotFoundException;
    void updateAccount(int accountId, String password, String email, Account account) throws AccountNotFoundException;
}
