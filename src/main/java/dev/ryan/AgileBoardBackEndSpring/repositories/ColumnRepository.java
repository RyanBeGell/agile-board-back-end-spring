package dev.ryan.AgileBoardBackEndSpring.repositories;

import dev.ryan.AgileBoardBackEndSpring.entities.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColumnRepository extends JpaRepository<Column, Long> {

}