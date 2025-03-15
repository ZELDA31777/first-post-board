package com.board.first;

import java.time.LocalDateTime;

public class Board {
    private int boardId;
    private String boardName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Board(int boardId, String boardName) {
        this.boardId = boardId;
        this.boardName = boardName;
        this.createTime = LocalDateTime.now();
        this.updateTime = createTime;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
