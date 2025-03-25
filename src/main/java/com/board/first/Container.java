package com.board.first;

import com.board.first.controller.AccountController;
import com.board.first.controller.BoardController;
import com.board.first.controller.PostController;
import com.board.first.service.*;

import java.util.Scanner;

public class Container {
    public static final Scanner sc;
    private static final AccountService accountService;
    private static final PostService postService;
    private static final BoardService boardService;

    public static final AccountController accountController;
    public static final PostController postController;
    public static final BoardController boardController;

    static {
        sc = new Scanner(System.in);
        accountService = new AccountServiceImpl();
        postService = new PostServiceImpl();
        boardService = new BoardServiceImpl(postService);

        accountController = new AccountController(accountService);
        boardController = new BoardController(sc, boardService, postService);
        postController = new PostController(postService, boardService);
    }


}
