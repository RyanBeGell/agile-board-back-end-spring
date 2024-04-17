package dev.ryan.AgileBoardBackEndSpring.repositories;

import dev.ryan.AgileBoardBackEndSpring.entities.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

}