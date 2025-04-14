package com.board.first.controller;

import com.board.first.RequestUtils;
import com.board.first.data.Account;
import com.board.first.data.ErrorCode;
import com.board.first.data.Post;
import com.board.first.Request;
import com.board.first.exception.BoardAppException;
import com.board.first.service.AccountService;
import com.board.first.service.BoardService;
import com.board.first.service.PostService;

import java.util.List;
import java.util.Scanner;

public class BoardController implements Controller {
    private final Scanner sc;
    private final BoardService boardService;
    private final PostService postService;
    private final AccountService accountService;

    public BoardController(Scanner sc, BoardService boardService, PostService postService, AccountService accountService) {
        this.boardService = boardService;
        this.postService = postService;
        this.accountService = accountService;
        this.sc = sc;
    }

    @Override
    public void requestHandler(Request request) {
        switch (request.getFunction()){
            case "edit":
                boardEdit(request);
                break;
            case "remove":
                boardRemove(request);
                break;
            case "view":
                boardView(request);
                break;
            case "add":
                boardAdd(request);
                break;
            default:
                throw new BoardAppException(ErrorCode.COMMAND_NOT_FOUND_FUNCTION);
        }
    }

    private void boardAdd(Request request) {
        String boardName = RequestUtils.parameterForInput(sc, "게시판 제목");
        Account account = accountService.getAccountByUserId(request.getLoginUserId());
        boardService.createBoard(boardName, account);
        System.out.println("게시판이 작성되었습니다.");
    }

    private void boardView(Request request) {

        requireParam(request, "boardName");
        String boardName = request.getParamMap().get("boardName");
        int boardId = boardService.getBoardIdByBoardName(boardName);
        System.out.println("글 번호\t/\t글 제목\t/\t작성일");
        List<Post> posts = postService.getPostListByBoardId(boardId);
        for (Post post : posts) {
            post.toString();
        }
    }

    private void boardRemove(Request request) {
        int boardId = RequestUtils.getIntParameterFromRequest(request, "boardId");
        Account account = accountService.getAccountByUserId(request.getLoginUserId());
        boardService.deleteBoard(boardId, account);
        System.out.printf("%d번 게시판이 성공적으로 삭제되었습니다!\n", boardId);
    }

    private void boardEdit(Request request) {
        int boardId = RequestUtils.getIntParameterFromRequest(request, "boardId");
        String boardName = RequestUtils.parameterForInput(sc, "게시판 제목");
        if (boardName.isEmpty()) {
            throw new BoardAppException(ErrorCode.POST_VALIDATION_FAILED);
        }
        Account account = accountService.getAccountByUserId(request.getLoginUserId());
        boardService.updateBoard(boardId, boardName, account);
        System.out.printf("%d번 게시판이 성공적으로 수정되었습니다!\n", boardId);
    }

    private static void requireParam(Request request, String paramName) {
        if (!request.getParamMap().containsKey(paramName)) {
            throw new BoardAppException(ErrorCode.INVALID_PARAMETER, paramName + " 파라미터를 입력해주세요.");
        }
    }
}
