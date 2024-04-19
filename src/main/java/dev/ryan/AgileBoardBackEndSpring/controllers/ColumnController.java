package dev.ryan.AgileBoardBackEndSpring.controllers;


import dev.ryan.AgileBoardBackEndSpring.dtos.ColumnDTO;
import dev.ryan.AgileBoardBackEndSpring.entities.Column;
import dev.ryan.AgileBoardBackEndSpring.entities.User;
import dev.ryan.AgileBoardBackEndSpring.services.ColumnService;
import dev.ryan.AgileBoardBackEndSpring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/columns")
public class ColumnController {

    private final ColumnService columnService;
    private final UserService userService;

    @Autowired
    public ColumnController(ColumnService columnService, UserService userService) {
        this.columnService = columnService;
        this.userService = userService;
    }

    @PostMapping("/board/{boardId}")
    public ResponseEntity<ColumnDTO> createColumn(@RequestBody ColumnDTO columnDto, @PathVariable Long boardId, Authentication authentication) {
        User user = userService.findUserByUsername(authentication.getName());
        ColumnDTO savedColumn = columnService.createColumn(columnDto, boardId, user);
        return ResponseEntity.ok(savedColumn);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColumnDTO> getColumnById(@PathVariable Long id, Authentication authentication) {
        User user = userService.findUserByUsername(authentication.getName());
        ColumnDTO columnDto = columnService.findColumnById(id, user);
        return ResponseEntity.ok(columnDto);
    }

    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<ColumnDTO>> getAllColumnsByBoardId(@PathVariable Long boardId, Authentication authentication) {
        User user = userService.findUserByUsername(authentication.getName());
        List<ColumnDTO> columnDTOs = columnService.findAllColumnsByBoardId(boardId, user);
        return ResponseEntity.ok(columnDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColumnDTO> updateColumn(@PathVariable Long id, @RequestBody ColumnDTO columnDto, Authentication authentication) {
        User user = userService.findUserByUsername(authentication.getName());
        ColumnDTO updatedColumn = columnService.updateColumn(id, columnDto, user);
        return ResponseEntity.ok(updatedColumn);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColumnById(@PathVariable Long id, Authentication authentication) {
        User user = userService.findUserByUsername(authentication.getName());
        columnService.deleteColumnById(id, user);
        return ResponseEntity.ok().build();
    }
}