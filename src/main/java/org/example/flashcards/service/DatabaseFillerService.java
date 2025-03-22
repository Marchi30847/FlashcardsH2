package org.example.flashcards.service;

import org.example.flashcards.entity.Card;
import org.example.flashcards.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
@PropertySource("classpath:paths.yaml")
public class DatabaseFillerService {

    @Autowired
    public DatabaseFillerService(CardRepository cardRepository, @Value("${initial-data-source}") String filePath) {
        if (cardRepository.findAll().isEmpty()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    List<String> data = List.of(line.split(","));
                    cardRepository.save(new Card(data.get(0), data.get(1), data.get(2)));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}