package org.example.flashcards.service;

import org.example.flashcards.entity.Card;
import org.example.flashcards.exception.DuplicateCardException;
import org.example.flashcards.exception.EmptyDictionaryException;
import org.example.flashcards.exception.IncorrectInputException;
import org.example.flashcards.exception.NonexistentCardException;
import org.example.flashcards.repository.SpringDataCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class SpringDataCardService {

    private final SpringDataCardRepository springDataCardRepository;

    @Autowired
    public SpringDataCardService(SpringDataCardRepository springDataCardRepository) {
        this.springDataCardRepository = springDataCardRepository;
    }

    public void add(Card card) throws DuplicateCardException {
        if (StreamSupport.stream(springDataCardRepository.findAll().spliterator(), false)
                .anyMatch(elem -> elem.equals(card))) {
            throw new DuplicateCardException("The card already exists");
        }
        springDataCardRepository.save(card);
    }

    public void delete(Long id) throws NonexistentCardException {
        if (!springDataCardRepository.existsById(id)) {
            throw new NonexistentCardException("The card does not exist");
        }
        springDataCardRepository.deleteById(id);
    }

    public void update(Card card) throws NonexistentCardException {
        if (card.getId() == null || !springDataCardRepository.existsById(card.getId())) {
            throw new NonexistentCardException("The card does not exist");
        }
        springDataCardRepository.save(card);
    }

    public List<Card> getDictionary() throws EmptyDictionaryException {
        List<Card> cards = (List<Card>) springDataCardRepository.findAll();
        if (cards.isEmpty()) {
            throw new EmptyDictionaryException("The dictionary is empty");
        }
        return cards;
    }

    public List<Card> getDictionarySorted(String language, String order) throws EmptyDictionaryException, IncorrectInputException {
        if (springDataCardRepository.count() == 0) {
            throw new EmptyDictionaryException("The dictionary is empty");
        }

        List<Card> cards;
        switch (order.toLowerCase()) {
            case "asc" -> {
                switch (language.toLowerCase()) {
                    case "english" -> cards = springDataCardRepository.findAllByOrderByEnglishAsc();
                    case "polish" -> cards = springDataCardRepository.findAllByOrderByPolishAsc();
                    case "german" -> cards = springDataCardRepository.findAllByOrderByGermanAsc();
                    default -> throw new IncorrectInputException("Incorrect input");
                }
            }
            case "desc" -> {
                switch (language.toLowerCase()) {
                    case "english" -> cards = springDataCardRepository.findAllByOrderByEnglishDesc();
                    case "polish" -> cards = springDataCardRepository.findAllByOrderByPolishDesc();
                    case "german" -> cards = springDataCardRepository.findAllByOrderByGermanDesc();
                    default -> throw new IncorrectInputException("Incorrect input");
                }
            }
            default -> throw new IncorrectInputException("Incorrect input");
        }

        return cards;
    }

    public Card getCardById(Long id) throws NonexistentCardException {
        return springDataCardRepository.findById(id)
                .orElseThrow(() -> new NonexistentCardException("The card does not exist"));
    }
}