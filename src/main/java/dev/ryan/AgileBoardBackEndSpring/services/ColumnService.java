package dev.ryan.AgileBoardBackEndSpring.services;

import dev.ryan.AgileBoardBackEndSpring.entities.Column;
import java.util.List;

public interface ColumnService {
    Column createColumn(Column column);
    Column findColumnById(Long id);
    List<Column> findAllColumns();
    Column updateColumn(Column column);
    void deleteColumnById(Long id);
}