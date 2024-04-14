package dev.ryan.AgileBoardBackEndSpring.services;

import dev.ryan.AgileBoardBackEndSpring.entities.Card;
import dev.ryan.AgileBoardBackEndSpring.exceptions.CardNotFoundException;
import dev.ryan.AgileBoardBackEndSpring.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public Card createCard(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public Card findCardById(Long id) {
        return cardRepository.findById(id).orElseThrow( () -> new CardNotFoundException("Card not found with id: " + id));
    }

    @Override
    public List<Card> findAllCards() {
        return cardRepository.findAll();
    }

    @Override
    public Card updateCard(Card card) {
        return cardRepository.save(card); // Assumes card has an ID set
    }

    @Override
    public void deleteCardById(Long id) {
        cardRepository.deleteById(id);
    }
}
