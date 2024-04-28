package dev.ryan.AgileBoardBackEndSpring.controllers;

import dev.ryan.AgileBoardBackEndSpring.dtos.CardDTO;
import dev.ryan.AgileBoardBackEndSpring.entities.Card;
import dev.ryan.AgileBoardBackEndSpring.entities.User;
import dev.ryan.AgileBoardBackEndSpring.services.CardService;
import dev.ryan.AgileBoardBackEndSpring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;
    private final UserService userService;

    @Autowired
    public CardController(CardService cardService, UserService userService) {
        this.cardService = cardService;
        this.userService = userService;
    }

    @PostMapping("/column/{columnId}")
    public ResponseEntity<CardDTO> createCard(@RequestBody CardDTO cardDto,
                                              @PathVariable Long columnId,
                                              Authentication authentication) {
        User user = userService.findUserByUsername(authentication.getName());
        CardDTO savedCard = cardService.createCard(cardDto, columnId, user);
        return ResponseEntity.ok(savedCard);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CardDTO> getCardById(@PathVariable Long id, Authentication authentication) {
        User user = userService.findUserByUsername(authentication.getName());
        CardDTO card = cardService.findCardById(id, user);
        return ResponseEntity.ok(card);
    }

    @GetMapping("/column/{columnId}")
    public ResponseEntity<List<CardDTO>> getCardsByColumn(@PathVariable Long columnId, Authentication authentication) {
        User user = userService.findUserByUsername(authentication.getName());
        List<CardDTO> cards = cardService.findAllCardsByColumnId(columnId, user);
        return ResponseEntity.ok(cards);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardDTO> updateCard(@PathVariable Long id, @RequestBody CardDTO cardDto, Authentication authentication) {
        User user = userService.findUserByUsername(authentication.getName());
        cardDto.setId(id);
        CardDTO updatedCard = cardService.updateCard(cardDto, user);
        return ResponseEntity.ok(updatedCard);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id, Authentication authentication) {
        User user = userService.findUserByUsername(authentication.getName());
        cardService.deleteCardById(id, user);
        return ResponseEntity.ok().build();
    }
}