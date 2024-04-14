package dev.ryan.AgileBoardBackEndSpring.services;

import dev.ryan.AgileBoardBackEndSpring.entities.Board;
import java.util.List;

public interface BoardService {
    Board createBoard(Board board);
    Board findBoardById(Long id);
    List<Board> findAllBoards();
    Board updateBoard(Board board);
    void deleteBoardById(Long id);
}
