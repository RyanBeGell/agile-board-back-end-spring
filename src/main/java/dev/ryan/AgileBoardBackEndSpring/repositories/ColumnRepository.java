package dev.ryan.AgileBoardBackEndSpring.repositories;

import dev.ryan.AgileBoardBackEndSpring.entities.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColumnRepository extends JpaRepository<Column, Long> {

    List<Column> findAllByBoardId(Long boardId);
    List<Column> findByBoardIdOrderByPosition(Long boardId);
}
