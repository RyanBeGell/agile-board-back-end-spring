package dev.ryan.AgileBoardBackEndSpring.repositories;

import dev.ryan.AgileBoardBackEndSpring.entities.Board;
import dev.ryan.AgileBoardBackEndSpring.entities.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByWorkspaceIn(Set<Workspace> workspaces);
    List<Board> findAllByWorkspaceId(Long workspaceId);

    @Query("SELECT b FROM Board b " +
            "LEFT JOIN FETCH b.columns c " +
            "LEFT JOIN FETCH c.cards " +
            "WHERE b.id = :id")
    Board findByIdWithColumnsAndCards(@Param("id") Long id);

}