package com.board.first.controller;

import com.board.first.data.Account;
import com.board.first.Request;
import com.board.first.exception.account.AccountStatusException;
import com.board.first.exception.account.AccountValidationException;
import com.board.first.exception.account.InvalidAccountIdException;
import com.board.first.exception.command.CommandValidationException;
import com.board.first.service.AccountService;

import java.util.Scanner;

public class AccountController implements Controller {
    private final Scanner sc;
    private final AccountService accountService;

    public AccountController(Scanner sc, AccountService accountService) {
        this.accountService = accountService;
        this.sc = sc;
    }

    @Override
    public void requestHandler(Request request) {
        switch(request.getFunction()) {
            case "signup":
                try {
                    System.out.print("계정: ");
                    String userId = sc.nextLine().trim();
                    System.out.print("비밀번호: ");
                    String password = sc.nextLine().trim();
                    System.out.print("닉네임: ");
                    String username = sc.nextLine().trim();
                    System.out.print("이메일: ");
                    String email = sc.nextLine().trim();
                    // 내부 로직을 서비스로 분리
                    accountService.signUpAccount(userId, password, username, email);
                    System.out.println("회원 가입이 성공적으로 완료되었습니다.");
                } catch (AccountValidationException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "signin":
                try {
                    if (request.isLogin()) {
                        throw new AccountStatusException("다른 아이디로 로그인 시에는 로그아웃 후에 로그인 해주세요.");
                    }
                    System.out.print("계정: ");
                    String userId = sc.nextLine().trim();
                    System.out.print("비밀번호: ");
                    String password = sc.nextLine().trim();
                    // TODO : Session
                    Account result = accountService.signInAccount(request, userId, password);
                    System.out.printf("%s의 로그인에 성공하였습니다!\n", result.getUsername());
                } catch (AccountValidationException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "signout":
                try {

                    accountService.logoutAccount(request);

                } catch (AccountValidationException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "detail":
                requireParam(request, "accountId");
                try {
                    String accountIdString = request.getParamMap().get("accountId");
                    int accountId;
                    try {
                        accountId = Integer.parseInt(accountIdString);
                    } catch (NumberFormatException e) {
                        throw new InvalidAccountIdException(accountIdString);
                    }
                    Account account = accountService.getAccountByAccountId(accountId);
                    System.out.printf("%d번 회원\n", accountId);
                    System.out.println("계정 : " + account.getUsername());
                    System.out.println("이메일 : " + account.getEmail());
                    System.out.println("가입일 : " + account.getCreateTime());
                } catch (AccountValidationException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "edit":
                requireParam(request, "accountId");
                try {
                    String accountIdString = request.getParamMap().get("accountId");
                    int accountId;
                    try {
                        accountId = Integer.parseInt(accountIdString);
                    } catch (NumberFormatException e) {
                        throw new InvalidAccountIdException(accountIdString);
                    }
                    System.out.print("비밀번호: ");
                    String password = sc.nextLine().trim();
                    System.out.print("이메일: ");
                    String email = sc.nextLine().trim();
                    accountService.updateAccount(accountId, password, email, request);
                } catch (AccountValidationException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "remove":
                requireParam(request, "accountId");
                try {
                    String accountIdString = request.getParamMap().get("accountId");
                    int accountId;
                    try {
                        accountId = Integer.parseInt(accountIdString);
                    } catch (NumberFormatException e) {
                        throw new InvalidAccountIdException(accountIdString);
                    }
                    if (request.isLogin()) {
                        accountService.logoutAccount(request);
                    }
                    String username = accountService.deleteAccount(accountId);
                    System.out.printf("%s의 회원 탈퇴에 성공하였습니다!\n", username);
                } catch (AccountValidationException e) {
                    System.out.println(e.getMessage());
                }
                break;
        }
    }

    private static void requireParam(Request request, String paramName) {
        if (!request.getParamMap().containsKey(paramName)) {
            throw new CommandValidationException(paramName + " 파라미터를 입력해주세요.");
        }
    }
}
