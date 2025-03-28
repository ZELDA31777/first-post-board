package com.board.first.service;

import com.board.first.data.Account;
import com.board.first.data.Board;
import com.board.first.exception.account.AccountStatusException;
import com.board.first.exception.board.BoardNotFoundException;
import com.board.first.exception.board.BoardValidationException;

import java.util.ArrayList;
import java.util.List;

public class BoardServiceImpl implements BoardService {
    private final List<Board> boards = new ArrayList<>();
    private int boardId = 1;
    private final PostService postService;

    public BoardServiceImpl(PostService postService) {
        this.postService = postService;
    }

    @Override
    public void createBoard(String boardName, Account account) {
        validateAccountFields(boardName);
        Board board = new Board(boardId++, boardName.trim(), account);
        boards.add(board);
    }

    @Override
    public Board getBoardByBoardId(int boardId) throws BoardNotFoundException {
        for (Board board : boards) {
            if (board.getBoardId() == boardId) {
                return board;
            }
        }
        throw new BoardNotFoundException(boardId);
    }

    @Override
    public void updateBoard(int boardId, String boardName, Account account) throws BoardNotFoundException, AccountStatusException {
        Board board = getBoardByBoardId(boardId);
        // short-circuit으로 처리하였습니다.
        if (account == null || !board.getAccount().equals(account)) {
            throw new AccountStatusException("작성한 계정으로 로그인 해주세요");
        }
        validateAccountFields(boardName);
        board.updateBoard(boardName);
    }

    @Override
    public void deleteBoard(int boardId, Account account) throws BoardNotFoundException, AccountStatusException {
        Board board = getBoardByBoardId(boardId);
        // short-circuit으로 처리하였습니다.
        if (account == null || !board.getAccount().equals(account)) {
            throw new AccountStatusException("작성한 계정으로 로그인 해주세요.");
        }
        postService.deletePostListByBoardId(boardId);
        boards.remove(board);
    }

    @Override
    public int getBoardIdByBoardName(String boardName) throws BoardNotFoundException {
        for (Board board : boards) {
            if (board.getBoardName().equals(boardName)) {
                return board.getBoardId();
            }
        }
        throw new BoardNotFoundException(boardName);
    }

    private void validateAccountFields(String... fields) throws BoardValidationException {
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) {
                throw new BoardValidationException("게시판 정보를 모두 입력해주세요.");
            }
        }
    }
}
