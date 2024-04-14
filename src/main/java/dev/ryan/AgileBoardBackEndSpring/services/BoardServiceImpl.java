package dev.ryan.AgileBoardBackEndSpring.services;

import dev.ryan.AgileBoardBackEndSpring.entities.Board;
import dev.ryan.AgileBoardBackEndSpring.exceptions.BoardNotFoundException;
import dev.ryan.AgileBoardBackEndSpring.repositories.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public Board createBoard(Board board) {
        return boardRepository.save(board);
    }

    @Override
    public Board findBoardById(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new BoardNotFoundException("Board not found with id: " + id));
    }

    @Override
    public List<Board> findAllBoards() {
        return boardRepository.findAll();
    }

    @Override
    public Board updateBoard(Board board) {
        return boardRepository.save(board); // Assumes board has an ID set
    }

    @Override
    public void deleteBoardById(Long id) {
        boardRepository.deleteById(id);
    }
}