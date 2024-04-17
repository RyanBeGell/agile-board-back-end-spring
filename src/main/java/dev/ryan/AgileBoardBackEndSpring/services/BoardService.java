package dev.ryan.AgileBoardBackEndSpring.services;

import dev.ryan.AgileBoardBackEndSpring.dtos.BoardDTO;
import dev.ryan.AgileBoardBackEndSpring.entities.User;

import java.util.List;

public interface BoardService {
    BoardDTO createBoard(BoardDTO boardDto, User user);
    BoardDTO findBoardById(Long id, User user);
    List<BoardDTO> findAllBoards(User user);
    BoardDTO updateBoard(Long id, BoardDTO boardDto, User user);
    void deleteBoardById(Long id, User user);
}
