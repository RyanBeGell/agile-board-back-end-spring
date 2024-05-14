package dev.ryan.AgileBoardBackEndSpring.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
    private Long id;
    private String name;
    private String description;
    private Long workspaceId;  // Only ID to avoid recursion
    private String gradient;
    private List<ColumnDTO> columns;  // Include columns directly

}
