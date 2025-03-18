package com.board.first.service;

import com.board.first.Account;
import com.board.first.Post;
import com.board.first.exception.account.AccountStatusException;
import com.board.first.exception.account.AccountValidationException;
import com.board.first.exception.board.BoardNotFoundException;
import com.board.first.exception.post.PostNotFoundException;
import com.board.first.exception.post.PostValidationException;
import com.board.first.exception.post.PostsNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostServiceImpl implements PostService {
    private static final List<Post> posts = new ArrayList<>();
    private static int postId = 1;

    @Override
    public void createPost(int boardId, String postName, Account account, String postContent) throws BoardNotFoundException {
        validateAccountFields(postName, postContent);
        Post post = new Post(postId++, boardId, account, postName, postContent);
        posts.add(post);
    }

    @Override
    public Post getPostByPostId(int postId) throws PostNotFoundException {
        for (Post post : posts){
            if (post.getPostId() == postId) {
                return post;
            }
        }
        throw new PostNotFoundException(postId);
    }

    @Override
    public void updatePost(int postId, Account account, String postName, String postContent) throws PostNotFoundException , AccountStatusException {
        Post post = getPostByPostId(postId);
        if (!post.getAccount().equals(account)) {
            throw new AccountValidationException("작성한 계정으로 로그인 해주세요.");
        }
        validateAccountFields(postName, postContent);
        post.setPostTitle(postName);
        post.setPostContent(postContent);
        post.setUpdateTime(LocalDateTime.now());
    }

    @Override
    public void deletePostByPostId(int postId, Account account) throws PostNotFoundException, AccountStatusException {
        Post post = getPostByPostId(postId);
        if (!post.getAccount().equals(account)) {
            throw new AccountStatusException("작성한 계정으로 로그인 해주세요.");
        }
        posts.remove(post);
    }

    @Override
    public List<Post> getPostListByBoardId(int boardId) throws PostNotFoundException {
        List<Post> postList = new ArrayList<>();
        for (Post post : posts) {
            if (post.getBoardId() == boardId) {
                postList.add(post);
                System.out.println(post.getPostId() + "\t/\t" + post.getPostTitle() + "\t/\t" + post.getCreateTime());
            }
        }
        if(postList.isEmpty()){
            throw new PostsNotFoundException(boardId);
        }
        return postList;
    }

    private void validateAccountFields(String... fields) throws PostValidationException {
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) {
                throw new PostValidationException("계정 정보를 모두 입력해주세요.");
            }
        }
    }

}
