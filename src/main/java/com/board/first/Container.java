package com.board.first;

import com.board.first.config.PostConstructor;
import com.board.first.controller.AccountController;
import com.board.first.controller.BoardController;
import com.board.first.controller.PostController;
import com.board.first.service.*;

import java.util.Scanner;

// 수동 의존성 주입(Manual Dependency Injection)의 역할을 수행
public class Container {
    public static final Scanner sc;
    public static Session session;

    public static final AccountService accountService;
    public static final PostService postService;
    public static final BoardService boardService;

    public static final AccountController accountController;
    public static final PostController postController;
    public static final BoardController boardController;

    public static PostConstructor postConstructor;

    static {
        sc = new Scanner(System.in);
        session = new Session();

        // Interface의 구현객체 주입
        accountService = new AccountServiceImpl();
        postService = new PostServiceImpl();
        boardService = new BoardServiceImpl(postService);

        accountController = new AccountController(sc, accountService);
        boardController = new BoardController(sc, boardService, postService, accountService);
        postController = new PostController(sc, postService, boardService, accountService);

        postConstructor = new PostConstructor();
    }

}
