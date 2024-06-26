package dev.ryan.AgileBoardBackEndSpring.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private List<WorkspaceDTO> workspaces;
}
