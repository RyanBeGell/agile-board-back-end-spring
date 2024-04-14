package dev.ryan.AgileBoardBackEndSpring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ryan.AgileBoardBackEndSpring.entities.Card;
import dev.ryan.AgileBoardBackEndSpring.services.CardService;
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

public class CardControllerTest {

    private MockMvc mockMvc;
    private CardService cardService;
    private CardController cardController;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        cardService = mock(CardService.class);
        cardController = new CardController(cardService);
        mockMvc = MockMvcBuilders.standaloneSetup(cardController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void createCard_ValidCard_ReturnsCard() throws Exception {
        Card card = new Card(); // Setup your card object
        card.setId(1L);
        // Assuming Card has more properties to set up

        when(cardService.createCard(any(Card.class))).thenReturn(card);

        mockMvc.perform(post("/api/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(card)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(card.getId()));
    }

    @Test
    public void getCardById_ExistingCardId_ReturnsCard() throws Exception {
        Long cardId = 1L;
        Card card = new Card();
        card.setId(cardId);

        when(cardService.findCardById(cardId)).thenReturn(card);

        mockMvc.perform(get("/api/cards/{id}", cardId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(card.getId()));
    }

    @Test
    public void getAllCards_CardsExist_ReturnsCardList() throws Exception {
        List<Card> cards = Arrays.asList(new Card()); // Setup your cards list
        when(cardService.findAllCards()).thenReturn(cards);

        mockMvc.perform(get("/api/cards"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(cards.get(0).getId()));
    }

    @Test
    public void updateCard_ValidCard_ReturnsUpdatedCard() throws Exception {
        Long cardId = 1L;
        Card card = new Card();
        card.setId(cardId);

        when(cardService.updateCard(any(Card.class))).thenReturn(card);

        mockMvc.perform(put("/api/cards/{id}", cardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(card)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(card.getId()));
    }

    @Test
    public void deleteCard_ExistingCardId_ReturnsOk() throws Exception {
        Long cardId = 1L;

        mockMvc.perform(delete("/api/cards/{id}", cardId))
                .andExpect(status().isOk());
    }
}