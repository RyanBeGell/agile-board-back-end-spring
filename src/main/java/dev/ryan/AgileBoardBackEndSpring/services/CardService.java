package dev.ryan.AgileBoardBackEndSpring.services;

import dev.ryan.AgileBoardBackEndSpring.entities.Card;
import java.util.List;

public interface CardService {
    Card createCard(Card card);
    Card findCardById(Long id);
    List<Card> findAllCards();
    Card updateCard(Card card);
    void deleteCardById(Long id);
}