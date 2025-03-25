package org.example.flashcards.repository;

import org.example.flashcards.entity.Card;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface SpringDataCardRepository extends CrudRepository<Card, Long> {
    List<Card> findAllByOrderByEnglishAsc();
    List<Card> findAllByOrderByPolishAsc();
    List<Card> findAllByOrderByGermanAsc();

    List<Card> findAllByOrderByEnglishDesc();
    List<Card> findAllByOrderByPolishDesc();
    List<Card> findAllByOrderByGermanDesc();

    List<Card> findAllByEnglishContainingIgnoreCaseOrPolishContainingIgnoreCaseOrGermanContainingIgnoreCase(String english, String polish, String german);
}
