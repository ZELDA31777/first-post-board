package com.board.first.controller;

import com.board.first.Request;
import com.board.first.service.BoardService;
import com.board.first.service.PostService;

public class PostController implements Controller {


    private final PostService postService;
    private final BoardService boardService;

    public PostController(PostService postService, BoardService boardService) {
        this.postService = postService;
        this.boardService = boardService;
    }

    @Override
    public void requestHandler(Request request) {

    }
}
