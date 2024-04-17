package dev.ryan.AgileBoardBackEndSpring.repositories;

import dev.ryan.AgileBoardBackEndSpring.entities.Board;
import dev.ryan.AgileBoardBackEndSpring.entities.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByWorkspaceIn(Set<Workspace> workspaces);
}
