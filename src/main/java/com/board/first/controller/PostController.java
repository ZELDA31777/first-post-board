package com.board.first.controller;

import com.board.first.data.Account;
import com.board.first.data.Post;
import com.board.first.Request;
import com.board.first.exception.account.AccountValidationException;
import com.board.first.exception.board.BoardNotFoundException;
import com.board.first.exception.board.InvalidBoardIdException;
import com.board.first.exception.board.InvalidPostIdException;
import com.board.first.exception.command.CommandValidationException;
import com.board.first.exception.post.PostValidationException;
import com.board.first.service.AccountService;
import com.board.first.service.BoardService;
import com.board.first.service.PostService;

import java.util.Scanner;

public class PostController implements Controller {

    private final Scanner sc;
    private final PostService postService;
    private final BoardService boardService;
    private final AccountService accountService;

    public PostController(Scanner sc, PostService postService, BoardService boardService, AccountService accountService) {
        this.postService = postService;
        this.boardService = boardService;
        this.accountService = accountService;
        this.sc = sc;
    }

    @Override
    public void requestHandler(Request request) {
        switch (request.getFunction()) {
            case "add":
                requireParam(request, "boardId");
                try {
                    String boardIdString = request.getParamMap().get("boardId");
                    int boardId;
                    try {
                        boardId = Integer.parseInt(boardIdString);
                    } catch (NumberFormatException e) {
                        throw new InvalidBoardIdException(boardIdString, e);
                    }
                    try {
                        boardService.getBoardByBoardId(boardId);
                    } catch (BoardNotFoundException e) {
                        throw new BoardNotFoundException(boardId);
                    }
                    System.out.print("제목: ");
                    String postName = sc.nextLine();
                    System.out.print("내용: ");
                    String postContent = sc.nextLine();
                    Account account = accountService.getAccountByUserId(request.getLoginUserId());
                    postService.createPost(boardId, postName, account, postContent);
                    System.out.println("게시글이 작성되었습니다.");
                } catch (PostValidationException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "view":
                requireParam(request, "postId");
                try {
                    String postIdString = request.getParamMap().get("postId");
                    int postId;
                    try {
                        postId = Integer.parseInt(request.getParamMap().get("postId"));
                    } catch (NumberFormatException e) {
                        throw new InvalidPostIdException(postIdString, e);
                    }
                    Post post = postService.getPostByPostId(postId);
                    System.out.printf("\n%d번 게시물\n", postId);
                    System.out.println("작성일 : " + post.getCreateTime());
                    System.out.println("수정일 : " + post.getUpdateTime());
                    System.out.println("작성자 : " + post.getAuthorName());
                    System.out.println("제목 : " + post.getPostTitle());
                    System.out.println("내용 : " + post.getPostContent());
                } catch (PostValidationException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "remove":
                requireParam(request, "postId");
                try {
                    String postIdString = request.getParamMap().get("postId");
                    int postId;
                    try {
                        postId = Integer.parseInt(postIdString);
                    } catch (NumberFormatException e) {
                        throw new InvalidPostIdException(postIdString, e);
                    }
                    Account account = accountService.getAccountByUserId(request.getLoginUserId());
                    postService.deletePostByPostId(postId, account);
                    System.out.printf("%d번 게시물이 성공적으로 삭제되었습니다.\n", postId);
                } catch (PostValidationException | AccountValidationException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "edit":
                requireParam(request, "postId");
                try {
                    String postIdString = request.getParamMap().get("postId");
                    int postId;
                    try {
                        postId = Integer.parseInt(postIdString);
                    } catch (NumberFormatException e) {
                        throw new InvalidPostIdException(postIdString);
                    }
                    System.out.print("제목: ");
                    String postName = sc.nextLine();
                    System.out.print("내용: ");
                    String postContent = sc.nextLine();
                    Account account = accountService.getAccountByUserId(request.getLoginUserId());
                    postService.updatePost(postId, account, postName, postContent);
                    System.out.printf("%d번 게시물이 성공적으로 수정되었습니다!\n", postId);
                } catch (PostValidationException e) {
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
