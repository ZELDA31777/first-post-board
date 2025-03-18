package com.board.first;

import java.time.LocalDateTime;

public class Post {
    private int postId;
    private int boardId;
    private Account account;
    private String postTitle;
    private String postContent;
    private String authorName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Post() {
    }

    // 게시글 작성시
    public Post(int postId, int boardId, Account account, String postTitle, String postContent) {
        this.postId = postId;
        this.boardId = boardId;
        this.account = account;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.authorName = account != null ? account.getUsername() : "비회원";
        this.createTime = LocalDateTime.now();
        this.updateTime = this.createTime;
    }

    public int getPostId() {
        return postId;
    }

    public int getBoardId() {
        return boardId;
    }

    public Account getAccount() {
        return account;
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

    public String getAuthorName() {
        return authorName;
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

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", postTitle='" + postTitle + '\'' +
                ", postContent='" + postContent + '\'' +
                ", boardId='" + boardId + '\'' +
                ", postAuthor='" + account.getUsername() + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
