package com.board.first.service;

import com.board.first.data.Account;
import com.board.first.data.Post;
import com.board.first.exception.board.BoardNotFoundException;
import com.board.first.exception.post.PostNotFoundException;

import java.util.List;

public interface PostService {
    void createPost (int boardId, String postName, Account account, String postContent) throws BoardNotFoundException;
    void updatePost(int postId, Account account, String postName, String postContent) throws PostNotFoundException;
    Post getPostByPostId (int postId) throws PostNotFoundException;
    void deletePostByPostId (int postId, Account account) throws PostNotFoundException;
    List<Post> getPostListByBoardId(int boardId) throws BoardNotFoundException;
    void deletePostListByBoardId(int boardId);
}
