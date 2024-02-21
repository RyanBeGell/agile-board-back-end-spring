package dev.ryan.AgileBoardBackEndSpring.services;

import dev.ryan.entities.Card;
import dev.ryan.repositories.CardRepository;
import dev.ryan.services.CardServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardServiceImpl cardService;

    @Test
    void createCard_ShouldReturnSavedCard() {
        Card newCard = new Card();
        newCard.setTitle("New Task");
        when(cardRepository.save(any(Card.class))).thenReturn(newCard);

        Card savedCard = cardService.createCard(newCard);

        assertThat(savedCard.getTitle()).isEqualTo("New Task");
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    void findCardById_ShouldReturnCard() {
        Long cardId = 1L;
        Card found = new Card();
        found.setId(cardId);
        found.setTitle("Found Task");
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(found));

        Card result = cardService.findCardById(cardId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(cardId);
        assertThat(result.getTitle()).isEqualTo("Found Task");
    }

    @Test
    void updateCard_ShouldReturnUpdatedCard() {
        Card originalCard = new Card();
        originalCard.setId(1L);
        originalCard.setTitle("Original Task");

        Card updatedCard = new Card();
        updatedCard.setId(1L);
        updatedCard.setTitle("Updated Task");

        when(cardRepository.save(any(Card.class))).thenReturn(updatedCard);

        Card result = cardService.updateCard(updatedCard);

        assertThat(result.getTitle()).isEqualTo("Updated Task");
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    void deleteCardById_ShouldInvokeRepositoryDelete() {
        Long cardId = 1L;
        doNothing().when(cardRepository).deleteById(cardId);

        cardService.deleteCardById(cardId);

        verify(cardRepository).deleteById(cardId);
    }
}