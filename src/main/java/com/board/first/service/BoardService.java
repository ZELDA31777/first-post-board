package com.board.first.service;

import com.board.first.data.Account;
import com.board.first.data.Board;

public interface BoardService {
    Board createBoard (String boardName, Account account);
    void updateBoard (int boardId, String boardName, Account account);
    void deleteBoard (int boardId, Account account);
    Board getBoardByBoardId(int boardId);
    int getBoardIdByBoardName(String boardName);
}
