package com.board.first;

import com.board.first.controller.BoardController;
import com.board.first.controller.Controller;
import com.board.first.exception.command.InvalidCommandException;

import java.util.Scanner;

public class Application {
    private final Scanner sc = Container.sc;
    // 프로그램 동작 제어용 boolean
    private boolean programStatus = true;
    // console 입력 전에 표시되는 도메인 설정용 String
    private final String domain;

    public Application(String domain) {
        this.domain = domain;
    }

    public void run() {
        while (programStatus) {
            String line = "https://" + domain;
            System.out.print(line);
            String command = sc.nextLine().trim();
            if (command.equals(".종료")) {
                programStatus = false;
                System.out.println("프로그램을 종료합니다.");
                continue;
            }
            Request request = new Request();
            request.updateUrl(command);
            Controller controller = null;
            switch (request.getCategory()) {
                case "boards":
                    controller =  Container.boardController;
                    break;
                case "posts":
                    controller = Container.postController;
                    break;
                case "accounts":
                    controller = Container.accountController;
                    break;
                default:
                    throw new InvalidCommandException(request.getCategory());
            }
            if (controller != null) {
                controller.requestHandler(request);
            } else {
                throw new InvalidCommandException(command);
            }
        }
    }
}
