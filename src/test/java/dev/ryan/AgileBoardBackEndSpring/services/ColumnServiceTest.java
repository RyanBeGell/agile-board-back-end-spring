package dev.ryan.AgileBoardBackEndSpring.services;

import dev.ryan.entities.Column;
import dev.ryan.repositories.ColumnRepository;
import dev.ryan.services.ColumnServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class ColumnServiceTest {

    @Mock
    private ColumnRepository columnRepository;

    @InjectMocks
    private ColumnServiceImpl columnService;

    @Test
    void createColumn_ShouldReturnSavedColumn() {
        Column newColumn = new Column();
        newColumn.setName("ToDo");
        when(columnRepository.save(any(Column.class))).thenReturn(newColumn);

        Column savedColumn = columnService.createColumn(newColumn);

        assertThat(savedColumn.getName()).isEqualTo("ToDo");
        verify(columnRepository).save(any(Column.class));
    }

    @Test
    void findColumnById_ShouldReturnColumn() {
        Long columnId = 1L;
        Column found = new Column();
        found.setId(columnId);
        found.setName("InProgress");
        when(columnRepository.findById(columnId)).thenReturn(Optional.of(found));

        Column result = columnService.findColumnById(columnId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(columnId);
        assertThat(result.getName()).isEqualTo("InProgress");
    }

    @Test
    void updateColumn_ShouldReturnUpdatedColumn() {
        Column originalColumn = new Column();
        originalColumn.setId(1L);
        originalColumn.setName("OriginalName");

        Column updatedColumn = new Column();
        updatedColumn.setId(1L);
        updatedColumn.setName("UpdatedName");

        when(columnRepository.save(any(Column.class))).thenReturn(updatedColumn);

        Column result = columnService.updateColumn(updatedColumn);

        assertThat(result.getName()).isEqualTo("UpdatedName");
        verify(columnRepository).save(any(Column.class));
    }

    @Test
    void deleteColumnById_ShouldInvokeRepositoryDelete() {
        Long columnId = 1L;
        doNothing().when(columnRepository).deleteById(columnId);

        columnService.deleteColumnById(columnId);

        verify(columnRepository).deleteById(columnId);
    }
}