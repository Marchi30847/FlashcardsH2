package org.example.flashcards.repository;

import jakarta.persistence.EntityManager;
import org.example.flashcards.entity.Card;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class CardRepository {
    private final EntityManager entityManager;

    public CardRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void save(Card card) {
        entityManager.persist(card);
    }

    @Transactional
    public void update(Card card) {
        entityManager.merge(card);
    }

    @Transactional
    public void deleteById(Long id) {
        Card card = entityManager.find(Card.class, id);
        if (card != null) {
            entityManager.remove(card);
        }
    }

    public Optional<Card> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Card.class, id));
    }

    public List<Card> findAll() {
        return entityManager.createQuery("select c from Card c", Card.class).getResultList();
    }

    public List<Card> findAllSorted(String language, String order) {
        String query = String.format("select c from Card c order by %s %s", language, order);
        return entityManager.createQuery(query, Card.class).getResultList();
    }
}
