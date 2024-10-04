package dev.ryan.AgileBoardBackEndSpring.services;

import dev.ryan.AgileBoardBackEndSpring.dtos.CardDTO;
import dev.ryan.AgileBoardBackEndSpring.dtos.ColumnDTO;
import dev.ryan.AgileBoardBackEndSpring.entities.Board;
import dev.ryan.AgileBoardBackEndSpring.entities.Card;
import dev.ryan.AgileBoardBackEndSpring.entities.Column;
import dev.ryan.AgileBoardBackEndSpring.entities.User;
import dev.ryan.AgileBoardBackEndSpring.exceptions.ColumnNotFoundException;
import dev.ryan.AgileBoardBackEndSpring.repositories.BoardRepository;
import dev.ryan.AgileBoardBackEndSpring.repositories.ColumnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColumnServiceImpl implements ColumnService {
    private final ColumnRepository columnRepository;
    private final BoardRepository boardRepository;
    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    public ColumnServiceImpl(ColumnRepository columnRepository, BoardRepository boardRepository) {
        this.columnRepository = columnRepository;
        this.boardRepository = boardRepository;
    }

    private ColumnDTO toColumnDto(Column column) {
        return new ColumnDTO(
                column.getId(),
                column.getName(),
                column.getPosition(),
                column.getBoard().getId(),
                column.getCards().stream().map(this::toCardDto).collect(Collectors.toList())
        );
    }

    private CardDTO toCardDto(Card card) {
        return new CardDTO(
                card.getId(),
                card.getTitle(),
                card.getDescription(),
                card.getColumn().getId(),
                card.getPosition(),
                card.getDueDate(),
                card.getAssignees().stream().map(User::getId).collect(Collectors.toSet())
        );
    }

    private Column fromDto(ColumnDTO columnDto, Board board) {
        Column column = new Column();
        column.setId(columnDto.getId());
        column.setName(columnDto.getName());
        column.setPosition(columnDto.getPosition());
        column.setBoard(board);
        return column;
    }

    @Override
    @Transactional
    public ColumnDTO createColumn(ColumnDTO columnDto, Long boardId, User user) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found with id: " + boardId));

        // Access control check
        if (!workspaceService.canAccess(board.getWorkspace().getId(), user)) {
            throw new AccessDeniedException("Not authorized to access this board");
        }

        int newPosition = getMaxPosition(boardId) + 1;
        Column column = new Column();
        column.setName(columnDto.getName());
        column.setBoard(board);
        column.setPosition(newPosition);  // Set the next position
        column = columnRepository.save(column);
        return toColumnDto(column); // Convert the saved entity to DTO before returning
    }

    @Override
    public ColumnDTO findColumnById(Long id, User user) {
        Column column = columnRepository.findById(id)
                .orElseThrow(() -> new ColumnNotFoundException("Column not found with id: " + id));
        if (!workspaceService.canAccess(column.getBoard().getWorkspace().getId(), user)) {
            throw new AccessDeniedException("Not authorized to access this board");
        }
        return toColumnDto(column);
    }

    @Override
    public List<ColumnDTO> findAllColumnsByBoardId(Long boardId, User user) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("Board not found with id: " + boardId));
        if (!workspaceService.canAccess(board.getWorkspace().getId(), user)) {
            throw new AccessDeniedException("Not authorized to access this board");
        }
        List<Column> columns = columnRepository.findByBoardIdOrderByPosition(boardId);
        return columns.stream().map(this::toColumnDto).collect(Collectors.toList());
    }

    @Override
    public ColumnDTO updateColumn(Long id, ColumnDTO columnDto, User user) {
        Column existingColumn = columnRepository.findById(id)
                .orElseThrow(() -> new ColumnNotFoundException("Column not found with id: " + id));
        if (!workspaceService.canAccess(existingColumn.getBoard().getWorkspace().getId(), user)) {
            throw new AccessDeniedException("Not authorized to access this board");
        }
        existingColumn.setName(columnDto.getName());
        existingColumn.setPosition(columnDto.getPosition());
        columnRepository.save(existingColumn);
        return toColumnDto(existingColumn);
    }

    @Override
    @Transactional
    public void deleteColumnById(Long id, User user) {
        Column column = columnRepository.findById(id)
                .orElseThrow(() -> new ColumnNotFoundException("Column not found with id: " + id));

        columnRepository.delete(column); // Delete the column
        reorderRemainingColumns(column.getBoard().getId()); // Reorder the columns after deletion
    }

    @Override
    @Transactional
    public void moveColumnToPosition(Long columnId, int newPosition, User user) {
        Column columnToMove = columnRepository.findById(columnId)
                .orElseThrow(() -> new IllegalArgumentException("Column not found with id: " + columnId));

        if (!workspaceService.canAccess(columnToMove.getBoard().getWorkspace().getId(), user)) {
            throw new AccessDeniedException("Not authorized to access this board");
        }

        List<Column> columns = columnRepository.findByBoardIdOrderByPosition(columnToMove.getBoard().getId());
        columns.removeIf(c -> c.getId().equals(columnId)); // Remove the moving column from list

        // Insert column in the new position (1-based index adjustment)
        columns.add(newPosition - 1, columnToMove);

        // Reassign positions
        for (int i = 0; i < columns.size(); i++) {
            columns.get(i).setPosition(i + 1); // Set positions starting from 1
        }

        columnRepository.saveAll(columns); // Batch save all columns with updated positions
    }

    //Utility methods
    private int getMaxPosition(Long boardId) {
        List<Column> columns = columnRepository.findByBoardIdOrderByPosition(boardId);
        return columns.isEmpty() ? 0 : columns.get(columns.size() - 1).getPosition(); // Return 0 if no columns
    }

    private void reorderRemainingColumns(Long boardId) {
        List<Column> remainingColumns = columnRepository.findByBoardIdOrderByPosition(boardId);
        int position = 1; // Start positions from 1
        for (Column column : remainingColumns) {
            column.setPosition(position++);
            columnRepository.save(column);
        }
    }

}
