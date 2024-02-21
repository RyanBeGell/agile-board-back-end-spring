package dev.ryan.services;

import dev.ryan.entities.Column;
import java.util.List;
import java.util.Optional;

public interface ColumnService {
    Column createColumn(Column column);
    Column findColumnById(Long id);
    List<Column> findAllColumns();
    Column updateColumn(Column column);
    void deleteColumnById(Long id);
}