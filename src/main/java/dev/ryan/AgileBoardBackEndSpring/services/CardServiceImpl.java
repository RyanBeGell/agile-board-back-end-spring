package dev.ryan.AgileBoardBackEndSpring.services;

import dev.ryan.AgileBoardBackEndSpring.dtos.CardDTO;
import dev.ryan.AgileBoardBackEndSpring.entities.Card;
import dev.ryan.AgileBoardBackEndSpring.entities.Column;
import dev.ryan.AgileBoardBackEndSpring.entities.User;
import dev.ryan.AgileBoardBackEndSpring.repositories.CardRepository;
import dev.ryan.AgileBoardBackEndSpring.repositories.ColumnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl implements CardService {
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ColumnRepository columnRepository;

    @Override
    @Transactional
    public CardDTO createCard(CardDTO cardDto, Long columnId, User user) {
        Column column = columnRepository.findById(columnId).orElseThrow(() -> new IllegalArgumentException("Column not found with id: " + columnId));
        int newPosition = getMaxCardPosition(columnId) + 1;
        Card card = new Card();
        card.setTitle(cardDto.getTitle());
        card.setDescription(cardDto.getDescription());
        card.setColumn(column);
        card.setPosition(newPosition);
        card = cardRepository.save(card);
        return toDto(card);
    }

    @Override
    public CardDTO findCardById(Long id, User user) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Card not found with id: " + id));
        return toDto(card);
    }

    @Override
    public List<CardDTO> findAllCardsByColumnId(Long columnId, User user) {
        List<Card> cards = cardRepository.findByColumnIdOrderByPosition(columnId);
        return cards.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public CardDTO updateCard(CardDTO cardDto, User user) {
        Card card = cardRepository.findById(cardDto.getId()).orElseThrow(() -> new IllegalArgumentException("Card not found with id: " + cardDto.getId()));
        card.setTitle(cardDto.getTitle());
        card.setDescription(cardDto.getDescription());
        cardRepository.save(card);
        return toDto(card);
    }

    @Override
    @Transactional
    public void deleteCardById(Long id, User user) {
        Card card = cardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Card not found with id: " + id));
        cardRepository.delete(card);
        reorderRemainingCards(card.getColumn().getId());
    }

    @Override
    @Transactional
    public void moveCardToPosition(Long cardId, Long newColumnId, int newPosition, User user) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new IllegalArgumentException("Card not found with id: " + cardId));
        card.setColumn(columnRepository.findById(newColumnId).orElseThrow(() -> new IllegalArgumentException("Column not found with id: " + newColumnId)));
        card.setPosition(newPosition);
        reorderRemainingCards(card.getColumn().getId());
        cardRepository.save(card);
    }

    @Override
    @Transactional
    public void updateCardPositions(Long columnId, List<Long> cardIds, User user) {
        List<Card> cards = cardRepository.findAllById(cardIds);
        int position = 1;
        for (Long cardId : cardIds) {
            Card card = cards.stream().filter(c -> c.getId().equals(cardId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Card not found with id: " + cardId));
            card.setPosition(position++);
        }
        cardRepository.saveAll(cards);
    }

    private CardDTO toDto(Card card) {
        return new CardDTO(
                card.getId(),
                card.getTitle(),
                card.getDescription(),
                card.getColumn().getId(),
                card.getPosition(),
                card.getDueDate(),
                card.getAssignees().stream().map(User::getId).collect(Collectors.toSet())
        );
    }


    private int getMaxCardPosition(Long columnId) {
        return cardRepository.findByColumnIdOrderByPosition(columnId).stream()
                .mapToInt(Card::getPosition)
                .max()
                .orElse(0);
    }

    private void reorderRemainingCards(Long columnId) {
        List<Card> cards = cardRepository.findByColumnIdOrderByPosition(columnId);
        int position = 1;
        for (Card card : cards) {
            card.setPosition(position++);
        }
        cardRepository.saveAll(cards);
    }
}
