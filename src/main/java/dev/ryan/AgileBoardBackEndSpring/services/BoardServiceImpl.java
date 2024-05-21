package dev.ryan.AgileBoardBackEndSpring.services;

import dev.ryan.AgileBoardBackEndSpring.dtos.BoardDTO;
import dev.ryan.AgileBoardBackEndSpring.dtos.CardDTO;
import dev.ryan.AgileBoardBackEndSpring.dtos.ColumnDTO;
import dev.ryan.AgileBoardBackEndSpring.entities.*;
import dev.ryan.AgileBoardBackEndSpring.exceptions.BoardNotFoundException;
import dev.ryan.AgileBoardBackEndSpring.repositories.BoardRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    @Autowired
    private WorkspaceService workspaceService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }


    private Board fromDto(BoardDTO boardDto, User user) {
        Board board = new Board();
        board.setId(boardDto.getId());
        board.setName(boardDto.getName());
        board.setDescription(boardDto.getDescription());
        board.setGradient(boardDto.getGradient());  // Set gradient from DTO

        // Fetch the workspace by ID and check if the user has access
        Workspace workspace = workspaceService.getWorkspaceById(boardDto.getWorkspaceId(), user);
        if (workspace == null) {
            throw new IllegalArgumentException("Workspace not found for ID: " + boardDto.getWorkspaceId());
        }
        if (!workspaceService.canAccess(workspace.getId(), user)) {
            throw new AccessDeniedException("User does not have access to the workspace with ID: " + workspace.getId());
        }
        board.setWorkspace(workspace);

        return board;
    }

    @Override
    public BoardDTO createBoard(BoardDTO boardDto, User user) {
        Board board = fromDto(boardDto, user);
        if (!workspaceService.canAccess(board.getWorkspace().getId(), user)) {
            throw new AccessDeniedException("Not authorized to access this workspace");
        }
        board = boardRepository.save(board);
        return board.toDto();
    }

    @Override
    public BoardDTO findBoardById(Long id, User user) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new BoardNotFoundException("Board not found with id: " + id));
        if (!workspaceService.canAccess(board.getWorkspace().getId(), user)) {
            throw new AccessDeniedException("Not authorized to access this board");
        }
        return board.toDto();
    }

    @Override
    public List<BoardDTO> findAllBoards(User user) {
        return boardRepository.findAllByWorkspaceIn(user.getWorkspaces()).stream().map(Board::toDto).collect(Collectors.toList());
    }

    @Override
    public BoardDTO updateBoard(Long id, BoardDTO boardDto, User user) {
        Board board = fromDto(boardDto, user);
        board.setId(id); // Ensure ID is not changed
        if (!workspaceService.canAccess(board.getWorkspace().getId(), user)) {
            throw new AccessDeniedException("Not authorized to modify this board");
        }
        board = boardRepository.save(board);
        return board.toDto();
    }

    @Override
    public void deleteBoardById(Long id, User user) {
        findBoardById(id, user); // Ensure the board exists and user has access before deleting
        boardRepository.deleteById(id);
    }

    @Override
    public List<BoardDTO> findBoardsByWorkspace(Long workspaceId, User user) {
        // First check if the user has access to this workspace
        Workspace workspace = workspaceService.getWorkspaceById(workspaceId, user);
        if (!workspaceService.canAccess(workspaceId, user)) {
            throw new AccessDeniedException("User does not have access to the workspace with ID: " + workspaceId);
        }

        // Fetch boards for the workspace if the user has access
        List<Board> boards = boardRepository.findAllByWorkspaceId(workspaceId);
        return boards.stream().map(Board::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BoardDTO getBoardWithColumnsAndCards(Long id, User user) {
        // Fetch the board by its ID with columns and cards
        Board board = boardRepository.findByIdWithColumnsAndCards(id);

        // Check if the board exists
        if (board == null) {
            throw new BoardNotFoundException("Board not found with id: " + id);
        }

        // Check if the user has access to the workspace of the board
        if (!workspaceService.canAccess(board.getWorkspace().getId(), user)) {
            throw new AccessDeniedException("User does not have access to the workspace with ID: " + board.getWorkspace().getId());
        }

        // Convert the board to its DTO and return it
        return board.toDto();
    }

}