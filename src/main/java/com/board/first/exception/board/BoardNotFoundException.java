package com.board.first.exception.board;

public class BoardNotFoundException extends BoardValidationException {
    public BoardNotFoundException(int boardId) {
        super(boardId + "번 게시판은 존재하지 않습니다.");
    }

    public BoardNotFoundException(String boardName) {
        super("제목이 " + boardName + "인 게시판은 존재하지 않습니다.");
    }
}
