package dev.ryan.AgileBoardBackEndSpring.services;

import dev.ryan.AgileBoardBackEndSpring.dtos.UserDTO;
import dev.ryan.AgileBoardBackEndSpring.entities.User;
import dev.ryan.AgileBoardBackEndSpring.entities.Workspace;

import java.util.List;

public interface WorkspaceService {
    Workspace createWorkspace(Workspace workspace, User user);
    Workspace updateWorkspace(Long id, Workspace workspace,User user);
    void deleteWorkspace(Long id, User user);
    Workspace getWorkspaceById(Long id, User user);
    List<UserDTO> findAllWorkspaces(User user);
    boolean canAccess(Long workspaceId, User user); // Used with @PreAuthorize
}
