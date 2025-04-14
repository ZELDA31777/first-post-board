package com.board.first.config;

import com.board.first.Container;
import com.board.first.data.Account;
import com.board.first.data.Board;
import com.board.first.service.AccountService;
import com.board.first.service.BoardService;
import com.board.first.service.PostService;

import java.util.ArrayList;
import java.util.List;

public class PostConstructor {
    private final AccountService accountService;
    private final BoardService boardService;
    private final PostService postService;

    public PostConstructor() {
        this.accountService = Container.accountService;
        this.boardService = Container.boardService;
        this.postService = Container.postService;
        dataInitialize(5, 4, 20);
    }

    protected void dataInitialize(int accountAmount, int boardAmount, int postAmount) {
        List<Account> accounts = new ArrayList<>();
        List<Board> boards = new ArrayList<>();

        Account admin = accountService.signUpAdminAccount("zelda", "zelda", "젤다", "zelda@admin.com");
        boardService.createBoard("공지사항", admin);
        for (int i = 0; i < accountAmount; i++) {
            Account account = accountService.signUpAccount("user" + i, "user" + i, "일반유저 " + i, "user" + i + "@member.com");
            accounts.add(account);
        }

        for (int i = 0; i < boardAmount; i++) {
            Board board = boardService.createBoard("게시판" + i, admin);
            boards.add(board);
        }
        for (int i = 0; i < postAmount; i++) {
            postService.createPost(
                            boards.get(i % boardAmount).getBoardId(),
                            "제목 " + i,
                            accounts.get(i % accountAmount),
                            "내용 " + i
                    );
        }

    }
}
