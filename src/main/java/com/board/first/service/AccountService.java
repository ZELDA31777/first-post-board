package com.board.first.service;

import com.board.first.Request;
import com.board.first.data.Account;

public interface AccountService {
    Account signUpAccount(String userId, String password, String username, String email);
    Account signInAccount(Request request, String userId, String password);
    void logoutAccount(Request request);

    Account signUpAdminAccount(String userId, String password, String username, String email);

    Account getAccountByUserId(String userId);
    Account getAccountByAccountId(int accountId);
    String deleteAccount(int accountId);
    void updateAccount(int accountId, String password, String email, Request request);
}
