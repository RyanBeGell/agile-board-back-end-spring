package dev.ryan.AgileBoardBackEndSpring.services;

import dev.ryan.AgileBoardBackEndSpring.entities.User;
import dev.ryan.AgileBoardBackEndSpring.entities.Workspace;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User findUserById(Long id);
    List<User> findAllUsers();
    User updateUser(User user);
    void deleteUserById(Long id);

    User findUserByUsername(String username) throws UsernameNotFoundException;

    @Transactional
    User addWorkspaceToUser(Long userId, Workspace workspace);

    // Method to remove a workspace from a user
    User removeWorkspaceFromUser(Long userId, Long workspaceId);

    @Transactional(readOnly = true)
    boolean hasWorkspaceWithName(Long userId, String workspaceName);
}