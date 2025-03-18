package com.board.first.service;

import com.board.first.Account;
import com.board.first.Board;
import com.board.first.exception.board.BoardNotFoundException;

public interface BoardService {
    void createBoard (String boardName, Account account);
    void updateBoard (int boardId, String boardName, Account account) throws BoardNotFoundException;
    void deleteBoard (int boardId, Account account) throws BoardNotFoundException;
    Board getBoardByBoardId(int boardId) throws BoardNotFoundException;
    int getBoardIdByBoardName(String boardName) throws BoardNotFoundException;
}
