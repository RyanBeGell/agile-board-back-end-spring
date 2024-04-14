package dev.ryan.AgileBoardBackEndSpring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ryan.AgileBoardBackEndSpring.entities.Column;
import dev.ryan.AgileBoardBackEndSpring.services.ColumnService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ColumnControllerTest {

    private MockMvc mockMvc;
    private ColumnService columnService;
    private ColumnController columnController;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        columnService = mock(ColumnService.class);
        columnController = new ColumnController(columnService);
        mockMvc = MockMvcBuilders.standaloneSetup(columnController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void createColumn_ValidColumn_ReturnsColumn() throws Exception {
        Column column = new Column(); // Setup your column object
        column.setId(1L);
        // Assuming Column has more properties to set up

        when(columnService.createColumn(any(Column.class))).thenReturn(column);

        mockMvc.perform(post("/api/columns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(column)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(column.getId()));
    }

    @Test
    public void getColumnById_ExistingColumnId_ReturnsColumn() throws Exception {
        Long columnId = 1L;
        Column column = new Column();
        column.setId(columnId);

        when(columnService.findColumnById(columnId)).thenReturn(column);

        mockMvc.perform(get("/api/columns/{id}", columnId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(column.getId()));
    }

    @Test
    public void getAllColumns_ColumnsExist_ReturnsColumnList() throws Exception {
        List<Column> columns = Arrays.asList(new Column()); // Setup your columns list
        when(columnService.findAllColumns()).thenReturn(columns);

        mockMvc.perform(get("/api/columns"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(columns.get(0).getId()));
    }

    @Test
    public void updateColumn_ValidColumn_ReturnsUpdatedColumn() throws Exception {
        Long columnId = 1L;
        Column column = new Column();
        column.setId(columnId);

        when(columnService.updateColumn(any(Column.class))).thenReturn(column);

        mockMvc.perform(put("/api/columns/{id}", columnId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(column)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(column.getId()));
    }

    @Test
    public void deleteColumn_ExistingColumnId_ReturnsOk() throws Exception {
        Long columnId = 1L;

        mockMvc.perform(delete("/api/columns/{id}", columnId))
                .andExpect(status().isOk());
    }
}
