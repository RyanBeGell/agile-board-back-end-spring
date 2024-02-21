package dev.ryan.controllers;


import dev.ryan.entities.Column;
import dev.ryan.services.ColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/columns")
public class ColumnController {

    private final ColumnService columnService;

    @Autowired
    public ColumnController(ColumnService columnService) {
        this.columnService = columnService;
    }

    @PostMapping
    public ResponseEntity<Column> createColumn(@RequestBody Column column) {
        Column savedColumn = columnService.createColumn(column);
        return ResponseEntity.ok(savedColumn);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Column> getColumnById(@PathVariable Long id) {
        Column column = columnService.findColumnById(id);
        return ResponseEntity.ok(column);
    }

    @GetMapping
    public ResponseEntity<List<Column>> getAllColumns() {
        List<Column> columns = columnService.findAllColumns();
        return ResponseEntity.ok(columns);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Column> updateColumn(@PathVariable Long id, @RequestBody Column column) {
        column.setId(id); // Ensure the ID is set to the path variable
        Column updatedColumn = columnService.updateColumn(column);
        return ResponseEntity.ok(updatedColumn);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColumn(@PathVariable Long id) {
        columnService.deleteColumnById(id);
        return ResponseEntity.ok().build();
    }
}
