package dev.ryan.AgileBoardBackEndSpring.services;

import dev.ryan.AgileBoardBackEndSpring.dtos.CardDTO;
import dev.ryan.AgileBoardBackEndSpring.entities.Card;
import dev.ryan.AgileBoardBackEndSpring.entities.Column;
import dev.ryan.AgileBoardBackEndSpring.entities.User;
import dev.ryan.AgileBoardBackEndSpring.exceptions.CardNotFoundException;
import dev.ryan.AgileBoardBackEndSpring.repositories.CardRepository;
import dev.ryan.AgileBoardBackEndSpring.repositories.ColumnRepository;
import dev.ryan.AgileBoardBackEndSpring.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final ColumnRepository columnRepository;
    private final UserRepository userRepository;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository, ColumnRepository columnRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.columnRepository = columnRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public CardDTO createCard(CardDTO cardDto, User user) {
        Column column = columnRepository.findById(cardDto.getColumnId())
                .orElseThrow(() -> new IllegalArgumentException("Column not found with id: " + cardDto.getColumnId()));

        if (!column.getBoard().getWorkspace().getUsers().contains(user)) {
            throw new AccessDeniedException("User does not have access to this column");
        }

        Card card = new Card();
        card.setTitle(cardDto.getTitle());
        card.setDescription(cardDto.getDescription());
        card.setColumn(column);
        card.setPosition(cardDto.getPosition());
        card.setDueDate(cardDto.getDueDate());
        card.setAssignees(cardDto.getAssigneeIds().stream()
                .map(userId -> userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId)))
                .collect(Collectors.toSet()));

        return toDto(cardRepository.save(card));
    }

    @Override
    @Transactional(readOnly = true)
    public CardDTO findCardById(Long id, User user) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException("Card not found with id: " + id));

        if (!card.getColumn().getBoard().getWorkspace().getUsers().contains(user)) {
            throw new AccessDeniedException("User does not have access to this card");
        }
        return toDto(card);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardDTO> findAllCardsByColumnId(Long columnId, User user) {
        Column column = columnRepository.findById(columnId)
                .orElseThrow(() -> new IllegalArgumentException("Column not found with id: " + columnId));

        if (!column.getBoard().getWorkspace().getUsers().contains(user)) {
            throw new AccessDeniedException("User does not have access to this column");
        }

        return cardRepository.findByColumnIdOrderByPosition(columnId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CardDTO updateCard(CardDTO cardDto, User user) {
        Card existingCard = cardRepository.findById(cardDto.getId())
                .orElseThrow(() -> new CardNotFoundException("Card not found with id: " + cardDto.getId()));

        if (!existingCard.getColumn().getBoard().getWorkspace().getUsers().contains(user)) {
            throw new AccessDeniedException("User does not have access to this card");
        }

        existingCard.setTitle(cardDto.getTitle());
        existingCard.setDescription(cardDto.getDescription());
        existingCard.setPosition(cardDto.getPosition());
        existingCard.setDueDate(cardDto.getDueDate());
        existingCard.setAssignees(cardDto.getAssigneeIds().stream().map(id -> new User(id)).collect(Collectors.toSet()));

        return toDto(cardRepository.save(existingCard));
    }

    @Override
    @Transactional
    public void deleteCardById(Long id, User user) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException("Card not found with id: " + id));

        if (!card.getColumn().getBoard().getWorkspace().getUsers().contains(user)) {
            throw new AccessDeniedException("User does not have access to this card");
        }
        cardRepository.delete(card);
    }

    @Override
    @Transactional
    public void updateCardPositions(Long columnId, List<Long> cardIds, User user) {
        Column column = columnRepository.findById(columnId)
                .orElseThrow(() -> new IllegalArgumentException("Column not found with id: " + columnId));

        if (!column.getBoard().getWorkspace().getUsers().contains(user)) {
            throw new AccessDeniedException("User does not have access to this column");
        }

        List<Card> cards = cardRepository.findAllById(cardIds);
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            card.setPosition(i);
            cardRepository.save(card);
        }
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
}