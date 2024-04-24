package dev.ryan.AgileBoardBackEndSpring.dtos;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardDTO {
    private Long id;
    private String title;
    private String description;
    private Long columnId;
    private Integer position;
    private Date dueDate;
    private Set<Long> assigneeIds; // Only user IDs to prevent recursion
}