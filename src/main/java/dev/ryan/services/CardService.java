package dev.ryan.services;

import dev.ryan.entities.Card;
import java.util.List;
import java.util.Optional;

public interface CardService {
    Card createCard(Card card);
    Card findCardById(Long id);
    List<Card> findAllCards();
    Card updateCard(Card card);
    void deleteCardById(Long id);
}