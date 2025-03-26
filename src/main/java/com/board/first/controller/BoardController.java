package com.board.first.controller;

import com.board.first.data.Post;
import com.board.first.Request;
import com.board.first.exception.account.AccountValidationException;
import com.board.first.exception.board.BoardValidationException;
import com.board.first.exception.board.InvalidBoardIdException;
import com.board.first.exception.command.CommandValidationException;
import com.board.first.service.BoardService;
import com.board.first.service.PostService;

import java.util.List;
import java.util.Scanner;

public class BoardController implements Controller {
    private final Scanner sc;
    private final BoardService boardService;
    private final PostService postService;

    public BoardController(Scanner sc, BoardService boardService, PostService postService) {
        this.boardService = boardService;
        this.postService = postService;
        this.sc = sc;
    }

    @Override
    public void requestHandler(Request request) {
        switch (request.getFunction()){
            case "edit":
                requireParam(request, "boardId");
                try {
                    String boardIdString = request.getParamMap().get("boardId");
                    int boardId;
                    try {
                        boardId = Integer.parseInt(boardIdString);
                    } catch (NumberFormatException e){
                        // RuntimeException의 하위 예외를 포함하고 있기 때문에, cause를 포함.
                        throw new InvalidBoardIdException(boardIdString, e);
                    }
                    System.out.print("게시판 제목: ");
                    String boardName = sc.nextLine().trim();
                    if (boardName.isEmpty()) {
                        throw new BoardValidationException("게시판 제목을 입력해주세요.");
                    }
                    boardService.updateBoard(boardId, boardName, request.getCurrentAccount());
                    System.out.printf("%d번 게시판이 성공적으로 수정되었습니다!\n", boardId);
                } catch (BoardValidationException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "remove":
                requireParam(request, "boardId");
                try {
                    String boardIdString = request.getParamMap().get("boardId");
                    int boardId;
                    try {
                        boardId = Integer.parseInt(boardIdString);
                    } catch (NumberFormatException e){
                        throw new InvalidBoardIdException(boardIdString, e);
                    }
                    boardService.deleteBoard(boardId, request.getCurrentAccount());
                    System.out.printf("%d번 게시판이 성공적으로 삭제되었습니다!\n", boardId);
                } catch (BoardValidationException | AccountValidationException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "view":
                requireParam(request, "boardName");
                try {
                    String boardName = request.getParamMap().get("boardName");
                    int boardId = boardService.getBoardIdByBoardName(boardName);
                    System.out.println("글 번호\t/\t글 제목\t/\t작성일");
                    List<Post> posts = postService.getPostListByBoardId(boardId);
                } catch (BoardValidationException e){
                    System.out.println(e.getMessage());
                }
                break;
            case "add":
                System.out.print("게시판 제목: ");
                String boardName = sc.nextLine().trim();
                boardService.createBoard(boardName, request.getCurrentAccount());
                System.out.println("게시판이 작성되었습니다.");
                break;
        }
    }

    private static void requireParam(Request request, String paramName) {
        if (!request.getParamMap().containsKey(paramName)) {
            throw new CommandValidationException(paramName + " 파라미터를 입력해주세요.");
        }
    }
}
