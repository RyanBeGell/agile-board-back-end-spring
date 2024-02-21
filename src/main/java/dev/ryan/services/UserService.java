package dev.ryan.services;

import dev.ryan.entities.User;
import java.util.List;

public interface UserService {
    User createUser(User user);
    User findUserById(Long id);
    List<User> findAllUsers();
    User updateUser(User user);
    void deleteUserById(Long id);
}