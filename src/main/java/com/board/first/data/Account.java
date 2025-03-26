package com.board.first.data;

import java.time.LocalDateTime;

public class Account {
    private int accountId;
    private String userId;
    private String password;
    private String username;
    private String email;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Account() {
    }

    public Account(int accountId, String userId, String password, String username, String email) {
        this.accountId = accountId;
        this.userId = userId;
        this.password = password;
        this.username = username;
        this.email = email;
        this.createTime = LocalDateTime.now();
        this.updateTime = createTime;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
