package dev.ryan.AgileBoardBackEndSpring.services;

import dev.ryan.AgileBoardBackEndSpring.entities.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User findUserById(Long id);
    List<User> findAllUsers();
    User updateUser(User user);
    void deleteUserById(Long id);

    User findUserByUsername(String username) throws UsernameNotFoundException;
}