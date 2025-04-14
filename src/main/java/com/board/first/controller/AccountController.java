package com.board.first.controller;

import com.board.first.RequestUtils;
import com.board.first.data.Account;
import com.board.first.Request;
import com.board.first.data.ErrorCode;
import com.board.first.exception.BoardAppException;
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
                accountSignUp();
                break;
            case "signin":
                accountSignIn(request);
                break;
            case "signout":
                accountSignOut(request);
                break;
            case "detail":
                accountDetail(request);
                break;
            case "edit":
                accountEdit(request);
                break;
            case "remove":
                accountRemove(request);
                break;
            default:
                throw new BoardAppException(ErrorCode.COMMAND_NOT_FOUND_FUNCTION);
        }
    }

    private void accountDetail(Request request) {
        int accountId = RequestUtils.getIntParameterFromRequest(request, "accountId");
        Account account = accountService.getAccountByAccountId(accountId);
        System.out.printf("%d번 회원\n", accountId);
        System.out.println("계정 : " + account.getUsername());
        System.out.println("이메일 : " + account.getEmail());
        System.out.println("가입일 : " + account.getCreateTime());
    }

    private void accountEdit(Request request) {
        int accountId = RequestUtils.getIntParameterFromRequest(request, "accountId");
        String password = RequestUtils.parameterForInput(sc, "비밀번호");
        String email = RequestUtils.parameterForInput(sc, "이메일");
        accountService.updateAccount(accountId, password, email, request);
    }


    private void accountRemove(Request request) {
        int accountId = RequestUtils.getIntParameterFromRequest(request, "accountId");
        if (request.isLogin()) {
            accountSignOut(request);
        }
        String username = accountService.deleteAccount(accountId);
        System.out.printf("%s의 회원 탈퇴에 성공하였습니다!\n", username);
    }

    private void accountSignOut(Request request) {
        accountService.logoutAccount(request);
    }

    private void accountSignIn(Request request) {
        if (request.isLogin()) {
            throw new BoardAppException(ErrorCode.ACCOUNT_NEED_LOGOUT);
        }
        String userId = RequestUtils.parameterForInput(sc,"계정");
        String password = RequestUtils.parameterForInput(sc, "비밀번호");
        // TODO : Session
        Account result = accountService.signInAccount(request, userId, password);
        System.out.printf("%s의 로그인에 성공하였습니다!\n", result.getUsername());
    }

    private void accountSignUp() {
        String userId = RequestUtils.parameterForInput(sc,"계정");
        String password = RequestUtils.parameterForInput(sc,"비밀번호");
        String username = RequestUtils.parameterForInput(sc,"닉네임");
        String email = RequestUtils.parameterForInput(sc,"이메일");
        accountService.signUpAccount(userId, password, username, email);
        System.out.println("회원 가입이 성공적으로 완료되었습니다.");
    }
}
