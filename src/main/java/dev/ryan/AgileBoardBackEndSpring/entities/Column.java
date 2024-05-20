package dev.ryan.AgileBoardBackEndSpring.entities;
import dev.ryan.AgileBoardBackEndSpring.dtos.ColumnDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "board_column")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Column {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "column", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Card> cards = new HashSet<>();

    private Integer position;

    public ColumnDTO toDto() {
        ColumnDTO dto = new ColumnDTO();
        dto.setId(this.id);
        dto.setName(this.name);
        dto.setBoardId(this.board != null ? this.board.getId() : null);
        dto.setPosition(this.position);
        dto.setCards(this.cards.stream().map(Card::toDto).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Column column = (Column) o;

        return Objects.equals(id, column.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
