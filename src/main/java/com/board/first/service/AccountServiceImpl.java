package com.board.first.service;

import com.board.first.Request;
import com.board.first.data.Account;
import com.board.first.exception.account.AccountNotFoundException;
import com.board.first.exception.account.AccountStatusException;
import com.board.first.exception.account.AccountValidationException;
import com.board.first.exception.account.AccountDuplicatedException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AccountServiceImpl implements AccountService {
    private static final List<Account> accounts = new ArrayList<>();
    private static int accountId = 1;

    @Override
    public void signUpAccount(String userId, String password, String username, String email) throws AccountValidationException {
        validateAccountFields(userId, password, username, email);
        validateDuplicateUserId(userId);
        validateEmailFormat(email);
        accounts.add(new Account(accountId++, userId, password, username, email));
    }

    @Override
    public Account signInAccount(Request request, String userId, String password) throws AccountValidationException {
        validateAccountFields(userId, password);
        for (Account account : accounts) {
            if (account.getUserId().equals(userId) && account.getPassword().equals(password)) {
                request.signIn(userId);
                return account;
            }
        }
        throw new AccountValidationException("로그인 정보가 올바르지 않습니다.");
    }

    @Override
    public void logoutAccount(Request request) {
        if (!request.isLogin()) {
            throw new AccountStatusException("이미 로그아웃 상태입니다.");
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
        throw new AccountNotFoundException(userId);
    }

    @Override
    public Account getAccountByAccountId(int accountId) throws AccountNotFoundException {
        for (Account account : accounts) {
            if (account.getAccountId() == accountId) {
                return account;
            }
        }
        throw new AccountNotFoundException(accountId);
    }

    @Override
    public String deleteAccount(int accountId) throws AccountNotFoundException {
        Account account = getAccountByAccountId(accountId);
        String username = account.getUsername();
        accounts.remove(account);
        return username;
    }

    @Override
    public void updateAccount(int accountId, String password, String email, Request request) throws AccountNotFoundException {
        validateAccountFields(password, email);
        validateEmailFormat(email);
        Account updatedAccount = getAccountByAccountId(accountId);
        if(!updatedAccount.getUserId().equals(request.getLoginUserId())){
            throw new AccountStatusException("로그인 중인 계정만 계정 정보를 수정할 수 있습니다.");
        }
        updatedAccount.setPassword(password);
        updatedAccount.setEmail(email);
        updatedAccount.setUpdateTime(LocalDateTime.now());

    }

    // 계정 정보 검증 (입력 정보가 늘어날 수 있어서 가변 인자 사용)
    private void validateAccountFields(String... fields) throws AccountValidationException {
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) {
                throw new AccountValidationException("계정 정보를 모두 입력해주세요.");
            }
        }
    }

    // 중복 유저 검증
    private void validateDuplicateUserId(String userId) throws AccountDuplicatedException {
        for (Account account : accounts) {
            if (account.getUserId().equals(userId)) {
                throw new AccountDuplicatedException(userId);
            }
        }
    }

    private void validateEmailFormat(String email) throws AccountValidationException {
        if (!email.contains("@")) {
            throw new AccountValidationException("유효하지 않은 이메일입니다.");
        }
    }
}
