package dev.ryan.AgileBoardBackEndSpring.services;

import dev.ryan.AgileBoardBackEndSpring.dtos.BoardDTO;
import dev.ryan.AgileBoardBackEndSpring.entities.Board;
import dev.ryan.AgileBoardBackEndSpring.entities.User;
import dev.ryan.AgileBoardBackEndSpring.entities.Workspace;
import dev.ryan.AgileBoardBackEndSpring.exceptions.BoardNotFoundException;
import dev.ryan.AgileBoardBackEndSpring.repositories.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    private BoardDTO toDto(Board board) {
        return new BoardDTO(board.getId(), board.getName(), board.getDescription(), board.getWorkspace().getId());
    }

    private Board fromDto(BoardDTO boardDto, User user) {
        Board board = new Board();
        board.setId(boardDto.getId());
        board.setName(boardDto.getName());
        board.setDescription(boardDto.getDescription());

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
        return toDto(boardRepository.save(board));
    }

    @Override
    public BoardDTO findBoardById(Long id, User user) {
        Board board = boardRepository.findById(id).orElseThrow(() -> new BoardNotFoundException("Board not found with id: " + id));
        if (!workspaceService.canAccess(board.getWorkspace().getId(), user)) {
            throw new AccessDeniedException("Not authorized to access this board");
        }
        return toDto(board);
    }

    @Override
    public List<BoardDTO> findAllBoards(User user) {
        return boardRepository.findAllByWorkspaceIn(user.getWorkspaces()).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public BoardDTO updateBoard(Long id, BoardDTO boardDto, User user) {
        Board board = fromDto(boardDto, user);
        board.setId(id); // Ensure ID is not changed
        if (!workspaceService.canAccess(board.getWorkspace().getId(), user)) {
            throw new AccessDeniedException("Not authorized to modify this board");
        }
        return toDto(boardRepository.save(board));
    }

    @Override
    public void deleteBoardById(Long id, User user) {
        findBoardById(id, user); // Ensure the board exists and user has access before deleting
        boardRepository.deleteById(id);
    }
}
