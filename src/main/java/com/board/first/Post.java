package com.board.first;

import java.time.LocalDateTime;

public class Post {
    private int postId;
    private String postTitle;
    private String postContent;
    private int boardId;
    private String postAuthor;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 게시글 작성시
    public Post(int postId, String postTitle, String postContent, int boardId) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.boardId = boardId;
        this.createTime = LocalDateTime.now();
        this.updateTime = this.createTime;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public String getPostAuthor() {
        return postAuthor;
    }

    public void setPostAuthor(String postAuthor) {
        this.postAuthor = postAuthor;
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

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", postTitle='" + postTitle + '\'' +
                ", postContent='" + postContent + '\'' +
                ", boardId='" + boardId + '\'' +
                ", postAuthor='" + postAuthor + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
