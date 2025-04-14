package com.board.first.controller;

import com.board.first.data.Account;
import com.board.first.data.ErrorCode;
import com.board.first.data.Post;
import com.board.first.Request;
import com.board.first.exception.BoardAppException;
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
                    String boardIdString = request.getParamMap().get("boardId");
                    int boardId;
                    try {
                        boardId = Integer.parseInt(boardIdString);
                    } catch (NumberFormatException e) {
                        throw new BoardAppException(ErrorCode.INVALID_PARAMETER);
                    }
                    boardService.getBoardByBoardId(boardId);
                    System.out.print("제목: ");
                    String postName = sc.nextLine();
                    System.out.print("내용: ");
                    String postContent = sc.nextLine();
                    Account account = accountService.getAccountByUserId(request.getLoginUserId());
                    postService.createPost(boardId, postName, account, postContent);
                    System.out.println("게시글이 작성되었습니다.");
                break;
            case "view":
                postView(request);
                break;
            case "remove":
                postRemove(request);
                break;
            case "edit":
                postEdit(request);
                break;
            default:
                throw new BoardAppException(ErrorCode.COMMAND_NOT_FOUND_FUNCTION);
        }
    }

    private void postView(Request request) {
        int postId = getPostIdFromRequest(request);
        Post post = postService.getPostByPostId(postId);
        System.out.printf("\n%d번 게시물\n", postId);
        System.out.println("작성일 : " + post.getCreateTime());
        System.out.println("수정일 : " + post.getUpdateTime());
        System.out.println("작성자 : " + post.getAuthorName());
        System.out.println("제목 : " + post.getPostTitle());
        System.out.println("내용 : " + post.getPostContent());
    }

    private void postRemove(Request request) {
        int postId = getPostIdFromRequest(request);
        Account account = accountService.getAccountByUserId(request.getLoginUserId());
        postService.deletePostByPostId(postId, account);
        System.out.printf("%d번 게시물이 성공적으로 삭제되었습니다.\n", postId);
    }

    private void postEdit(Request request) {
        int postId = getPostIdFromRequest(request);
        String postName = parameterForInput("제목");
        String postContent = parameterForInput("내용");
        Account account = accountService.getAccountByUserId(request.getLoginUserId());
        postService.updatePost(postId, account, postName, postContent);
        System.out.printf("%d번 게시물이 성공적으로 수정되었습니다!\n", postId);
    }

    private int getPostIdFromRequest(Request request) {
        requireParam(request, "postId");
        String postIdString = request.getParamMap().get("postId");
        if (postIdString == null || postIdString.isBlank()) {
            throw new BoardAppException(ErrorCode.INVALID_PARAMETER);
        }
        try {
            return Integer.parseInt(postIdString);
        } catch (NumberFormatException e) {
            throw new BoardAppException(ErrorCode.INVALID_PARAMETER);
        }
    }

    private String parameterForInput(String fieldName) {
        System.out.println(fieldName + ": ");
        return sc.nextLine().trim();
    }

    private static void requireParam(Request request, String paramName) {
        if (!request.getParamMap().containsKey(paramName)) {
            throw new BoardAppException(ErrorCode.INVALID_PARAMETER, paramName + " 파라미터를 입력해주세요.");
        }
    }
}
