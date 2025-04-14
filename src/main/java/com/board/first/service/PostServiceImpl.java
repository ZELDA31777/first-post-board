package com.board.first.service;

import com.board.first.data.Account;
import com.board.first.data.ErrorCode;
import com.board.first.data.Post;
import com.board.first.exception.BoardAppException;

import java.util.ArrayList;
import java.util.List;

public class PostServiceImpl implements PostService {
    private static final List<Post> posts = new ArrayList<>();
    private static int postId = 1;

    @Override
    public void createPost(int boardId, String postName, Account account, String postContent) {
        validateAccountFields(postName, postContent);
        Post post = new Post(postId++, boardId, account, postName, postContent);
        posts.add(post);
    }

    @Override
    public Post getPostByPostId(int postId) {
        for (Post post : posts){
            if (post.getPostId() == postId) {
                return post;
            }
        }
        throw new BoardAppException(ErrorCode.POST_NOT_FOUND);
    }

    @Override
    public void updatePost(int postId, Account account, String postName, String postContent) {
        Post post = getPostByPostId(postId);
        if (account == null || !post.getAccount().equals(account)) {
            throw new BoardAppException(ErrorCode.ACCOUNT_UNAUTHORIZED);
        }
        validateAccountFields(postName, postContent);
        post.updatePost(postName,postContent);
    }

    @Override
    public void deletePostByPostId(int postId, Account account) {
        Post post = getPostByPostId(postId);
        if (account == null || !post.getAccount().equals(account)) {
            throw new BoardAppException(ErrorCode.ACCOUNT_UNAUTHORIZED);
        }
        posts.remove(post);
    }

    @Override
    public List<Post> getPostListByBoardId(int boardId) {
        List<Post> postList = new ArrayList<>();
        for (Post post : posts) {
            if (post.getBoardId() == boardId) {
                postList.add(post);
                System.out.println(post.getPostId() + "\t/\t" + post.getPostTitle() + "\t/\t" + post.getCreateTime());
            }
        }
        if(postList.isEmpty()){
            throw new BoardAppException(ErrorCode.BOARD_NOT_FOUND);
        }
        return postList;
    }

    @Override
    public void deletePostListByBoardId(int boardId) {
        // 함수형
        posts.removeIf(post -> post.getBoardId() == boardId);
    }

    private void validateAccountFields(String... fields) {
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) {
                throw new BoardAppException(ErrorCode.POST_VALIDATION_FAILED);
            }
        }
    }

}
