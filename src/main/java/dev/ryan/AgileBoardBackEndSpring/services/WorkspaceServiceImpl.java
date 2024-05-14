package dev.ryan.AgileBoardBackEndSpring.services;

import dev.ryan.AgileBoardBackEndSpring.dtos.UserDTO;
import dev.ryan.AgileBoardBackEndSpring.dtos.WorkspaceDTO;
import dev.ryan.AgileBoardBackEndSpring.entities.User;
import dev.ryan.AgileBoardBackEndSpring.entities.Workspace;
import dev.ryan.AgileBoardBackEndSpring.exceptions.UserNotFoundException;
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
    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public Workspace createWorkspace(Workspace workspace, User user) {
        // Check if workspace with the same name already exists for the user
        if (userService.hasWorkspaceWithName(user.getId(), workspace.getName())) {
            throw new IllegalStateException("You already have a workspace with the name: " + workspace.getName());
        }

        // Save and flush workspace immediately to ensure it's managed and has an ID.
        workspace = workspaceRepository.save(workspace);
//        workspaceRepository.flush();

        // Now add the workspace to the user and save the user.
        // This avoids the problem of creating a duplicate workspace entry.
        userService.addWorkspaceToUser(user.getId(), workspace);

        return workspace;
    }



    @Override
    @Transactional
    public Workspace updateWorkspace(Long id, Workspace workspace, User user) {
        Workspace existingWorkspace = getWorkspaceById(id, user);
        existingWorkspace.setName(workspace.getName());
        existingWorkspace.setIcon(workspace.getIcon());
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
    @Transactional(readOnly = true)
    public List<UserDTO> findAllWorkspaces(User user) {
        // Fetch user again within the transaction to ensure initialization of lazily loaded collections
        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + user.getId()));

        Set<Workspace> workspaces = managedUser.getWorkspaces();
        List<UserDTO> userDTOs = new ArrayList<>();

        for (Workspace workspace : workspaces) {
            UserDTO dto = new UserDTO();
            dto.setId(managedUser.getId());
            dto.setUsername(managedUser.getUsername());

            WorkspaceDTO workspaceDTO = new WorkspaceDTO();
            workspaceDTO.setId(workspace.getId());
            workspaceDTO.setName(workspace.getName());
            workspaceDTO.setIcon(workspace.getIcon().getIcon()); // Ensure icon is fetched

            dto.setWorkspaces(Collections.singletonList(workspaceDTO));
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
