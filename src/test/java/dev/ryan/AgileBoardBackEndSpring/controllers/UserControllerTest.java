package dev.ryan.AgileBoardBackEndSpring.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ryan.controllers.UserController;
import dev.ryan.entities.User;
import dev.ryan.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;
    private UserService userService;
    private UserController userController;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createUser_ValidUser_ReturnsCreatedUser() throws Exception {
        User user = new User(); // Create a valid user object here
        user.setId(1L); // Set a dummy ID for testing purposes
        when(userService.createUser(any(User.class))).thenReturn(user);

        String jsonUser = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(user.getId())); // Ensure the response contains the user ID
    }

    @Test
    void getUserById_ExistingUserId_ReturnsUser() throws Exception {
        Long userId = 1L; // Existing user ID
        User user = new User(); // Create a valid user object here
        user.setId(userId);
        when(userService.findUserById(userId)).thenReturn(user);

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(user.getId())); // Ensure the response contains the user ID
    }

    @Test
    void getAllUsers_NoUsers_ReturnsEmptyList() throws Exception {
        when(userService.findAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty()); // Ensure the response is an empty array
    }

    @Test
    void updateUser_ValidUser_ReturnsUpdatedUser() throws Exception {
        Long userId = 1L; // Existing user ID
        User user = new User(); // Create a valid user object here
        user.setId(userId);
        when(userService.updateUser(any(User.class))).thenReturn(user);

        String jsonUser = objectMapper.writeValueAsString(user);

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUser))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(user.getId())); // Ensure the response contains the user ID
    }

    @Test
    void deleteUser_ExistingUserId_ReturnsOk() throws Exception {
        Long userId = 1L; // Existing user ID

        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isOk());
    }
}