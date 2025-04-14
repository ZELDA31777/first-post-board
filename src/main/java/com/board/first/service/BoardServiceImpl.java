package com.board.first.service;

import com.board.first.data.Account;
import com.board.first.data.AuthType;
import com.board.first.data.Board;
import com.board.first.data.ErrorCode;
import com.board.first.exception.BoardAppException;

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
    public Board createBoard(String boardName, Account account) {
        validateFields(boardName);
        if (account == null || account.getAuthType() != AuthType.ADMIN) {
            throw new BoardAppException(ErrorCode.ACCOUNT_ADMIN_ONLY);
        }
        Board board = new Board(boardId++, boardName.trim(), account);
        boards.add(board);
        return board;
    }

    @Override
    public Board getBoardByBoardId(int boardId) {
        for (Board board : boards) {
            if (board.getBoardId() == boardId) {
                return board;
            }
        }
        throw new BoardAppException(ErrorCode.BOARD_NOT_FOUND);
    }

    @Override
    public void updateBoard(int boardId, String boardName, Account account) {
        Board board = getBoardByBoardId(boardId);
        // short-circuit으로 처리하였습니다.
        if (account == null || !board.getAccount().equals(account)) {
            throw new BoardAppException(ErrorCode.ACCOUNT_UNAUTHORIZED);
        }
        validateFields(boardName);
        board.updateBoard(boardName);
    }

    @Override
    public void deleteBoard(int boardId, Account account) {
        Board board = getBoardByBoardId(boardId);
        // short-circuit으로 처리하였습니다.
        if (account == null || !board.getAccount().equals(account)) {
            throw new BoardAppException(ErrorCode.ACCOUNT_UNAUTHORIZED);
        }
        postService.deletePostListByBoardId(boardId);
        boards.remove(board);
    }

    @Override
    public int getBoardIdByBoardName(String boardName) {
        for (Board board : boards) {
            if (board.getBoardName().equals(boardName)) {
                return board.getBoardId();
            }
        }
        throw new BoardAppException(ErrorCode.BOARD_NOT_FOUND);
    }

    private void validateFields(String... fields) {
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) {
                throw new BoardAppException(ErrorCode.BOARD_VALIDATION_FAILED);
            }
        }
    }
}
