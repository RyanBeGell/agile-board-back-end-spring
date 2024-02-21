package dev.ryan.AgileBoardBackEndSpring.services;

import dev.ryan.entities.User;
import dev.ryan.repositories.UserRepository;
import dev.ryan.services.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_ShouldReturnSavedUser() {
        User newUser = new User();
        newUser.setUsername("TestUser");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User savedUser = userService.createUser(newUser);

        assertThat(savedUser.getUsername()).isEqualTo("TestUser");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void findUserById_ShouldReturnUser() {
        Long userId = 1L;
        User found = new User();
        found.setId(userId);
        found.setUsername("FindUser");
        when(userRepository.findById(userId)).thenReturn(Optional.of(found));

        User result = userService.findUserById(userId);

        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getUsername()).isEqualTo("FindUser");
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() {
        User originalUser = new User();
        originalUser.setId(1L);
        originalUser.setUsername("OriginalUsername");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("UpdatedUsername");

        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(updatedUser);

        assertThat(result.getUsername()).isEqualTo("UpdatedUsername");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void deleteUserById_ShouldInvokeRepositoryDelete() {
        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUserById(userId);

        verify(userRepository).deleteById(userId);
    }
}
