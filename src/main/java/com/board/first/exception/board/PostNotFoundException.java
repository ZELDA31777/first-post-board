package com.board.first.exception.board;

public class PostNotFoundException extends PostValidationException {
    public PostNotFoundException(int postId) {
        super(postId + "번 게시글은 존재하지 않습니다.");
    }
}
