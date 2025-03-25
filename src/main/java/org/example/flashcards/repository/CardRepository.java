package org.example.flashcards.repository;

import jakarta.persistence.EntityManager;
import org.example.flashcards.entity.Card;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
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
    public void update(Card card) throws NoSuchElementException {
        Card toUpdate = findById(card.getId())
                .orElseThrow(() -> new NoSuchElementException("Card not found"));
        toUpdate.setEnglish(card.getEnglish());
        toUpdate.setPolish(card.getPolish());
        toUpdate.setGerman(card.getGerman());
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
        String query = "select c from Card c";
        return entityManager.createQuery(query, Card.class).getResultList();
    }

    public List<Card> findAllSorted(String language, String order) {
        if (language == null || order == null) return List.of();
        String query = String.format("select c from Card c order by %s %s", language, order);
        return entityManager.createQuery(query, Card.class)
                .getResultList();
    }

    public List<Card> findAllByContainingPattern(String pattern) {
        if (pattern == null || pattern.isEmpty()) pattern = "";
        String query = "select c from Card c " +
                "where lower(c.english) like lower(:pattern) " +
                "or lower(c.polish) like lower(:pattern) " +
                "or lower(c.german) like lower(:pattern) ";
        return entityManager.createQuery(query, Card.class)
                .setParameter("pattern", "%" + pattern.trim() + "%")
                .getResultList();
    }

}
