package com.board.first.service;

import com.board.first.Request;
import com.board.first.data.Account;
import com.board.first.data.AuthType;
import com.board.first.data.ErrorCode;
import com.board.first.exception.BoardAppException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AccountServiceImpl implements AccountService {
    private static final List<Account> accounts = new ArrayList<>();
    private static int accountId = 1;

    @Override
    public Account signUpAccount(String userId, String password, String username, String email) {
        validateAccountFields(userId, password, username, email);
        validateDuplicateUserId(userId);
        validateEmailFormat(email);
        Account account = new Account(accountId++, userId, password, username, email, AuthType.MEMBER);
        accounts.add(account);
        return account;
    }

    @Override
    public Account signUpAdminAccount(String userId, String password, String username, String email) {
        Account admin = new Account(accountId++, userId, password, username, email, AuthType.ADMIN);
        accounts.add(admin);
        return admin;
    }

    @Override
    public Account signInAccount(Request request, String userId, String password) {
        validateAccountFields(userId, password);
        for (Account account : accounts) {
            if (account.getUserId().equals(userId) && account.getPassword().equals(password)) {
                request.signIn(userId);
                return account;
            }
        }
        throw new BoardAppException(ErrorCode.AUTHENTICATION_FAILED);
    }

    @Override
    public void logoutAccount(Request request) {
        if (!request.isLogin()) {
            throw new BoardAppException(ErrorCode.ALREADY_LOGGED_OUT);
        }
        System.out.printf("%s의 로그아웃에 성공하였습니다!\n", request.getLoginUserId());
        request.signOut();
    }

    @Override
    public Account getAccountByUserId(String userId) {
        for (Account account : accounts) {
            if (account.getUserId().equals(userId)) {
                return account;
            }
        }
        throw new BoardAppException(ErrorCode.ACCOUNT_NOT_FOUND);
    }

    @Override
    public Account getAccountByAccountId(int accountId) {
        for (Account account : accounts) {
            if (account.getAccountId() == accountId) {
                return account;
            }
        }
        throw new BoardAppException(ErrorCode.ACCOUNT_NOT_FOUND);
    }

    @Override
    public String deleteAccount(int accountId) {
        Account account = getAccountByAccountId(accountId);
        String username = account.getUsername();
        accounts.remove(account);
        return username;
    }

    @Override
    public void updateAccount(int accountId, String password, String email, Request request) {
        validateAccountFields(password, email);
        validateEmailFormat(email);
        Account updatedAccount = getAccountByAccountId(accountId);
        if(!updatedAccount.getUserId().equals(request.getLoginUserId())){
            throw new BoardAppException(ErrorCode.ACCOUNT_UNAUTHORIZED);
        }
        updatedAccount.setPassword(password);
        updatedAccount.setEmail(email);
        updatedAccount.setUpdateTime(LocalDateTime.now());

    }

    // 계정 정보 검증 (입력 정보가 늘어날 수 있어서 가변 인자 사용)
    private void validateAccountFields(String... fields){
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) {
                throw new BoardAppException(ErrorCode.INVALID_PARAMETER);
            }
        }
    }

    // 중복 유저 검증
    private void validateDuplicateUserId(String userId){
        for (Account account : accounts) {
            if (account.getUserId().equals(userId)) {
                throw new BoardAppException(ErrorCode.ACCOUNT_DUPLICATED);
            }
        }
    }

    private void validateEmailFormat(String email){
        if (!email.contains("@")) {
            throw new BoardAppException(ErrorCode.INVALID_PARAMETER);
        }
    }
}
