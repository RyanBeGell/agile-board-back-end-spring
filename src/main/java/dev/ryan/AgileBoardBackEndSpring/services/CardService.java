package dev.ryan.AgileBoardBackEndSpring.services;

import dev.ryan.AgileBoardBackEndSpring.dtos.CardDTO;
import dev.ryan.AgileBoardBackEndSpring.entities.Card;
import dev.ryan.AgileBoardBackEndSpring.entities.User;

import java.util.List;

public interface CardService {
    CardDTO createCard(CardDTO cardDto, Long columnId, User user);
    CardDTO findCardById(Long id, User user);
    List<CardDTO> findAllCardsByColumnId(Long columnId, User user);
    CardDTO updateCard(CardDTO cardDto, User user);
    void deleteCardById(Long id, User user);
    void moveCardToPosition(Long cardId, Long newColumnId, int newPosition, User user);
    void updateCardPositions(Long columnId, List<Long> cardIds, User user);
}