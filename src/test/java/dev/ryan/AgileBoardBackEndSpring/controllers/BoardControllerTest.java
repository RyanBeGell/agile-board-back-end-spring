package dev.ryan.AgileBoardBackEndSpring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ryan.AgileBoardBackEndSpring.entities.Board;
import dev.ryan.AgileBoardBackEndSpring.services.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BoardControllerTest {

    private MockMvc mockMvc;
    private BoardService boardService;
    private BoardController boardController;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        boardService = mock(BoardService.class);
        boardController = new BoardController(boardService);
        mockMvc = MockMvcBuilders.standaloneSetup(boardController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void createBoard_ValidBoard_ReturnsBoard() throws Exception {
        Board board = new Board(); // Setup your board object
        board.setId(1L);
        // Assume Board has a name or other properties to set up

        when(boardService.createBoard(any(Board.class))).thenReturn(board);

        mockMvc.perform(post("/api/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(board)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(board.getId()));
    }

    @Test
    public void getBoardById_ExistingBoardId_ReturnsBoard() throws Exception {
        Long boardId = 1L;
        Board board = new Board();
        board.setId(boardId);

        when(boardService.findBoardById(boardId)).thenReturn(board);

        mockMvc.perform(get("/api/boards/{id}", boardId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(board.getId()));
    }

    @Test
    public void getAllBoards_BoardsExist_ReturnsBoardList() throws Exception {
        List<Board> boards = Arrays.asList(new Board()); // Set up boards list
        when(boardService.findAllBoards()).thenReturn(boards);

        mockMvc.perform(get("/api/boards"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(boards.get(0).getId()));
    }

    @Test
    public void updateBoard_ValidBoard_ReturnsUpdatedBoard() throws Exception {
        Long boardId = 1L;
        Board board = new Board();
        board.setId(boardId);

        when(boardService.updateBoard(any(Board.class))).thenReturn(board);

        mockMvc.perform(put("/api/boards/{id}", boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(board)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(board.getId()));
    }

    @Test
    public void deleteBoard_ExistingBoardId_ReturnsOk() throws Exception {
        Long boardId = 1L;

        mockMvc.perform(delete("/api/boards/{id}", boardId))
                .andExpect(status().isOk());
    }
}