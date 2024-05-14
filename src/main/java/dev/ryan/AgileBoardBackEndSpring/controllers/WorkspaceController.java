package dev.ryan.AgileBoardBackEndSpring.controllers;

import dev.ryan.AgileBoardBackEndSpring.dtos.UserDTO;
import dev.ryan.AgileBoardBackEndSpring.entities.User;
import dev.ryan.AgileBoardBackEndSpring.entities.Workspace;
import dev.ryan.AgileBoardBackEndSpring.services.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @Autowired
    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllWorkspaces(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<UserDTO> workspaces = workspaceService.findAllWorkspaces(user);
        return ResponseEntity.ok(workspaces);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createWorkspace(@RequestBody Workspace workspace, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        try {
            Workspace createdWorkspace = workspaceService.createWorkspace(workspace, user);
            return ResponseEntity.ok(createdWorkspace);
        } catch (IllegalStateException e) {
            // Returning a more specific status code for conflict/error
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<Workspace> getWorkspaceById(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Workspace workspace = workspaceService.getWorkspaceById(id, user);
        return ResponseEntity.ok(workspace);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Workspace> updateWorkspace(@PathVariable Long id, @RequestBody Workspace workspace, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Workspace updatedWorkspace = workspaceService.updateWorkspace(id, workspace, user);
        return ResponseEntity.ok(updatedWorkspace);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        workspaceService.deleteWorkspace(id, user);
        return ResponseEntity.ok().build();
    }
}
