package com.board.first.service;

import com.board.first.data.Account;
import com.board.first.data.Post;

import java.util.List;

public interface PostService {
    void createPost (int boardId, String postName, Account account, String postContent);
    void updatePost(int postId, Account account, String postName, String postContent);
    Post getPostByPostId (int postId);
    void deletePostByPostId (int postId, Account account);
    List<Post> getPostListByBoardId(int boardId);
    void deletePostListByBoardId(int boardId);
}
