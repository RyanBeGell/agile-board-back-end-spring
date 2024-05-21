package dev.ryan.AgileBoardBackEndSpring.controllers;

import dev.ryan.AgileBoardBackEndSpring.dtos.BoardDTO;
import dev.ryan.AgileBoardBackEndSpring.entities.User;
import dev.ryan.AgileBoardBackEndSpring.services.BoardService;
import dev.ryan.AgileBoardBackEndSpring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;

    @Autowired
    public BoardController(BoardService boardService, UserService userService) {
        this.boardService = boardService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<BoardDTO> createBoard(@RequestBody BoardDTO boardDto, Authentication authentication) {
        User user = userService.findUserByUsername(authentication.getName());
        BoardDTO savedBoardDto = boardService.createBoard(boardDto, user);
        return ResponseEntity.ok(savedBoardDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO> getBoardById(@PathVariable Long id, Authentication authentication) {
        User user = userService.findUserByUsername(authentication.getName());
        return ResponseEntity.ok(boardService.findBoardById(id, user));
    }

    @GetMapping("/workspace/{workspaceId}")
    public ResponseEntity<List<BoardDTO>> getBoardsByWorkspace(@PathVariable Long workspaceId, Authentication authentication) {
        User user = userService.findUserByUsername(authentication.getName());
        List<BoardDTO> boards = boardService.findBoardsByWorkspace(workspaceId, user);
        return ResponseEntity.ok(boards);
    }

    @GetMapping
    public ResponseEntity<List<BoardDTO>> getAllBoards(Authentication authentication) {
        User user = userService.findUserByUsername(authentication.getName());
        return ResponseEntity.ok(boardService.findAllBoards(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardDTO> updateBoard(@PathVariable Long id, @RequestBody BoardDTO boardDto, Authentication authentication) {
        User user = userService.findUserByUsername(authentication.getName());
        return ResponseEntity.ok(boardService.updateBoard(id, boardDto, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id, Authentication authentication) {
        User user = userService.findUserByUsername(authentication.getName());
        boardService.deleteBoardById(id, user);
        return ResponseEntity.ok().build();
    }

}
