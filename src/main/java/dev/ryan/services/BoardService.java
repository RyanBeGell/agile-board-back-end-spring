package dev.ryan.services;

import dev.ryan.entities.Board;
import java.util.List;
import java.util.Optional;

public interface BoardService {
    Board createBoard(Board board);
    Board findBoardById(Long id);
    List<Board> findAllBoards();
    Board updateBoard(Board board);
    void deleteBoardById(Long id);
}
