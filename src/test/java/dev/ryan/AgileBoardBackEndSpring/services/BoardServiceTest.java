package dev.ryan.AgileBoardBackEndSpring.services;

import dev.ryan.entities.Board;
import dev.ryan.repositories.BoardRepository;
import dev.ryan.services.BoardServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardServiceImpl boardService;

    @Test
    void createBoard_ShouldReturnSavedBoard() {
        Board board = new Board();
        board.setName("Test Board");
        when(boardRepository.save(any(Board.class))).thenReturn(board);

        Board savedBoard = boardService.createBoard(new Board());

        assertThat(savedBoard.getName()).isEqualTo("Test Board");
        verify(boardRepository).save(any(Board.class));
    }

    @Test
    void findBoardById_ShouldReturnBoard() {
        Long boardId = 1L;
        Board expectedBoard = new Board();
        expectedBoard.setId(boardId);
        expectedBoard.setName("Test Board");

        // Simulate the repository behavior
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(expectedBoard));

        // Call the service method
        Board actualBoard = boardService.findBoardById(boardId);

        // Assertions to verify the behavior
        assertThat(actualBoard).isNotNull();
        assertThat(actualBoard.getId()).isEqualTo(expectedBoard.getId());
        assertThat(actualBoard.getName()).isEqualTo(expectedBoard.getName());

        // Verify the repository was called
        verify(boardRepository).findById(boardId);
    }

    @Test
    void updateBoard_ShouldReturnUpdatedBoard() {
        Board existingBoard = new Board();
        existingBoard.setId(1L);
        existingBoard.setName("Old Name");

        Board updatedInfo = new Board();
        updatedInfo.setId(1L);
        updatedInfo.setName("Updated Name");

        when(boardRepository.save(any(Board.class))).thenReturn(updatedInfo);

        Board updatedBoard = boardService.updateBoard(updatedInfo);

        assertThat(updatedBoard.getName()).isEqualTo("Updated Name");
        verify(boardRepository).save(any(Board.class));
    }

    @Test
    void deleteBoardById_ShouldInvokeDeletion() {
        Long boardId = 1L;
        doNothing().when(boardRepository).deleteById(boardId);

        boardService.deleteBoardById(boardId);

        verify(boardRepository).deleteById(boardId);
    }
}
