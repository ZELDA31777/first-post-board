package com.board.first;

import java.time.LocalDateTime;

public class Board {
    private int boardId;
    private String boardName;
    private Account account;
    private String boardAuthor;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Board() {

    }

    public Board(int boardId, String boardName, Account account) {
        this.boardId = boardId;
        this.boardName = boardName;
        this.account = account;
        this.boardAuthor = account != null ? account.getUsername() : "비회원";
        this.createTime = LocalDateTime.now();
        this.updateTime = createTime;
    }

    public int getBoardId() {
        return boardId;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public Account getAccount() {
        return account;
    }

    public String getBoardAuthor() {
        return boardAuthor;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
