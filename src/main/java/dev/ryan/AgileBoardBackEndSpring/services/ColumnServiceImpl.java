package dev.ryan.AgileBoardBackEndSpring.services;

import dev.ryan.AgileBoardBackEndSpring.entities.Column;
import dev.ryan.AgileBoardBackEndSpring.exceptions.ColumnNotFoundException;
import dev.ryan.AgileBoardBackEndSpring.repositories.ColumnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColumnServiceImpl implements ColumnService {

    private final ColumnRepository columnRepository;

    @Autowired
    public ColumnServiceImpl(ColumnRepository columnRepository) {
        this.columnRepository = columnRepository;
    }

    @Override
    public Column createColumn(Column column) {
        return columnRepository.save(column);
    }

    @Override
    public Column findColumnById(Long id) {
        return columnRepository.findById(id).orElseThrow(() -> new ColumnNotFoundException("Column not found with id: " + id));
    }

    @Override
    public List<Column> findAllColumns() {
        return columnRepository.findAll();
    }

    @Override
    public Column updateColumn(Column column) {
        return columnRepository.save(column); // Assumes column has an ID set
    }

    @Override
    public void deleteColumnById(Long id) {
        columnRepository.deleteById(id);
    }
}
