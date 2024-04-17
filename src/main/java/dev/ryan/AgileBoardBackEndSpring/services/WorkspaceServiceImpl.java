package dev.ryan.AgileBoardBackEndSpring.services;

import dev.ryan.AgileBoardBackEndSpring.dtos.UserDTO;
import dev.ryan.AgileBoardBackEndSpring.dtos.WorkspaceDTO;
import dev.ryan.AgileBoardBackEndSpring.entities.User;
import dev.ryan.AgileBoardBackEndSpring.entities.Workspace;
import dev.ryan.AgileBoardBackEndSpring.repositories.UserRepository;
import dev.ryan.AgileBoardBackEndSpring.repositories.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private WorkspaceRepository workspaceRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Workspace createWorkspace(Workspace workspace, User user) {
        user.getWorkspaces().add(workspace);
        // Also add the user to the workspace's set of users
        workspace.getUsers().add(user);

        // depending on cascade settings, saving one might be enough, need to test this still
        System.out.println("User Workspaces before save: " + user.getWorkspaces());
        System.out.println("Workspace Users before save: " + workspace.getUsers());
        userRepository.save(user);  // Since User owns the relationship
        workspaceRepository.save(workspace);

        return workspace;
    }

    @Override
    @Transactional
    public Workspace updateWorkspace(Long id, Workspace workspace, User user) {
        Workspace existingWorkspace = getWorkspaceById(id, user);
        existingWorkspace.setName(workspace.getName());

        if (!canAccess(id, user)) {
            throw new AccessDeniedException("Not authorized to access this workspace");
        }

        return workspaceRepository.save(existingWorkspace);
    }

    @Override
    @Transactional
    public void deleteWorkspace(Long id, User user) {
        Workspace workspace = getWorkspaceById(id, user);

        // Check access before attempting to delete
        if (!canAccess(id, user)) {
            System.out.println("Access denied for user: " + user.getUsername());
            throw new AccessDeniedException("Not authorized to access this workspace");
        }
        workspace.getUsers().forEach(u -> u.getWorkspaces().remove(workspace));
        userRepository.saveAll(workspace.getUsers());  // Save the changes to users

        // Now delete the workspace
        workspaceRepository.delete(workspace);
    }

    @Override
    public Workspace getWorkspaceById(Long id, User user) {
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Workspace not found"));

        if (!canAccess(id, user)) {
            throw new AccessDeniedException("Not authorized to access this workspace");
        }

        return workspace;
    }

    @Override
    public List<UserDTO> findAllWorkspaces(User user) {
        Set<Workspace> workspaces = user.getWorkspaces();
        List<UserDTO> userDTOs = new ArrayList<>();

        for (Workspace workspace : workspaces) {
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());

            WorkspaceDTO workspaceDTO = new WorkspaceDTO();
            workspaceDTO.setId(workspace.getId());
            workspaceDTO.setName(workspace.getName());

            dto.setWorkspaces(Collections.singletonList(workspaceDTO)); // Add only specific workspace details
            userDTOs.add(dto);
        }

        return userDTOs;
    }

    @Override
    public boolean canAccess(Long workspaceId, User user) {
        return workspaceRepository.findById(workspaceId)
                .map(workspace -> {
                    boolean hasAccess = workspace.getUsers().contains(user);
                    System.out.println("Access check for user " + user.getUsername() + " on workspace " + workspaceId + ": " + hasAccess);
                    return hasAccess;
                })
                .orElse(false);
    }

}
