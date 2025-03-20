package org.example.flashcards.repository;

import jakarta.persistence.EntityManager;
import org.example.flashcards.entity.Card;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class CardRepository {
    private EntityManager entityManager;

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
    public void delete(Card card) {
        entityManager.remove(card);
    }

    public List<Card> findAll() {
        return entityManager.createQuery("select c from Card c", Card.class).getResultList();
    }

    public Card findById(Long id) {
        return entityManager.find(Card.class, id);
    }


}
