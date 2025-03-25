package org.example.flashcards.service;

import org.example.flashcards.entity.Card;
import org.example.flashcards.exception.DuplicateCardException;
import org.example.flashcards.exception.EmptyDictionaryException;
import org.example.flashcards.exception.IncorrectInputException;
import org.example.flashcards.exception.NonexistentCardException;
import org.example.flashcards.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {
    private final CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public void add(Card card) throws DuplicateCardException {
        if (cardRepository.findAll().contains(card)) {
            throw new DuplicateCardException("The card already exists");
        }
        cardRepository.save(card);
    }

    public void delete(Long id) throws NonexistentCardException {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NonexistentCardException("The card does not exist"));
        cardRepository.deleteById(card.getId());
    }

    public void update(Card card) throws NonexistentCardException {
        if (card.getId() == null || cardRepository.findById(card.getId()).isEmpty()) {
            throw new NonexistentCardException("The card does not exist");
        }
        cardRepository.update(card);
    }

    public List<Card> getDictionary() throws EmptyDictionaryException {
        if (cardRepository.findAll().isEmpty()) {
            throw new EmptyDictionaryException("The dictionary is empty");
        }
        return cardRepository.findAll();
    }

    public List<Card> getDictionarySorted(String language, String order) throws EmptyDictionaryException, IncorrectInputException {
        if (cardRepository.findAll().isEmpty()) {
            throw new EmptyDictionaryException("The dictionary is empty");
        }
        if (!List.of("english", "polish", "german").contains(language.toLowerCase()) ||
                !List.of("asc", "desc").contains(order.toLowerCase())) {
            throw new IncorrectInputException("Incorrect input");
        }
        return cardRepository.findAllSorted(language, order);
    }

    public List<Card> searchForAll(String pattern) throws EmptyDictionaryException {
        List<Card> allContaining = cardRepository.findAllByContainingPattern(pattern);

        if (allContaining.isEmpty()) {
            throw new EmptyDictionaryException("The dictionary is empty");
        }

        return allContaining;
    }

    public Card getCardById(Long id) throws NonexistentCardException {
        return cardRepository.findById(id)
                .orElseThrow(() -> new NonexistentCardException("The card does not exist"));
    }

}
