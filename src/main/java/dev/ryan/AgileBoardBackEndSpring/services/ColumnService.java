package dev.ryan.AgileBoardBackEndSpring.services;

import dev.ryan.AgileBoardBackEndSpring.dtos.ColumnDTO;
import dev.ryan.AgileBoardBackEndSpring.entities.Column;
import dev.ryan.AgileBoardBackEndSpring.entities.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ColumnService {
    ColumnDTO createColumn(ColumnDTO columnDto, Long boardId, User user);
    ColumnDTO findColumnById(Long id, User user);
    List<ColumnDTO> findAllColumnsByBoardId(Long boardId, User user);
    ColumnDTO updateColumn(Long id, ColumnDTO columnDto, User user);
    void deleteColumnById(Long id, User user);
    void moveColumnToPosition(Long columnId, int newPosition, User user);
}