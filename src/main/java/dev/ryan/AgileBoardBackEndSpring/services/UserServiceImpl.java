package dev.ryan.AgileBoardBackEndSpring.services;
import dev.ryan.AgileBoardBackEndSpring.entities.User;
import dev.ryan.AgileBoardBackEndSpring.entities.Workspace;
import dev.ryan.AgileBoardBackEndSpring.exceptions.UserNotFoundException;
import dev.ryan.AgileBoardBackEndSpring.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user); // Assumes user has an ID set
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    @Override
    @Transactional
    public User addWorkspaceToUser(Long userId, Workspace workspace) {
        User user = findUserById(userId); // Use existing method to handle not found exception
        user.getWorkspaces().add(workspace);
        workspace.getUsers().add(user); // Assuming bidirectional relationship is managed properly
        userRepository.save(user); // This should only trigger updates to the user-workspace association.
        return user;
}

    @Override
    @Transactional
    public User removeWorkspaceFromUser(Long userId, Long workspaceId) {
        User user = findUserById(userId);
        user.getWorkspaces().removeIf(w -> w.getId().equals(workspaceId));
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasWorkspaceWithName(Long userId, String workspaceName) {
        User user = findUserById(userId); // Reuses existing method to fetch user
        return user.getWorkspaces().stream().anyMatch(w -> w.getName().equals(workspaceName));
    }


}
