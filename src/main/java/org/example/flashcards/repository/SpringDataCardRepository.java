package org.example.flashcards.repository;

import org.example.flashcards.entity.Card;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SpringDataCardRepository extends CrudRepository<Card, Long> {
    void deleteCardById(Long id);
    Card findCardById(Long id);
    List<Card> findAllByOrderByEnglishAsc();
    List<Card> findAllByOrderByPolishAsc();
    List<Card> findAllByOrderByGermanAsc();
    List<Card> findAllByOrderByEnglishDesc();
    List<Card> findAllByOrderByPolishDesc();
    List<Card> findAllByOrderByGermanDesc();

}
