package com.board.first.exception.post;

public class PostsNotFoundException extends PostValidationException{
    public PostsNotFoundException(int boardId) {
        super(boardId + "인 게시판에는 게시글이 존재하지 않습니다.");
    }
}
