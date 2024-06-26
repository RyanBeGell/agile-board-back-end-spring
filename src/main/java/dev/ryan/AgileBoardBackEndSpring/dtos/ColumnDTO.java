package dev.ryan.AgileBoardBackEndSpring.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ColumnDTO {
    private Long id;
    private String name;
    private Integer position;
    private Long boardId;  // Reference to the board by ID
    private List<CardDTO> cards;  // Include cards directly
}
