package org.example.flashcards.controller;

import org.example.flashcards.entity.Card;
import org.example.flashcards.repository.CardRepository;
import org.example.flashcards.service.DisplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

@Controller
public class FlashcardsController {
    private final DisplayService displayService;
    private final CardRepository cardRepository;
    private final Scanner scanner;

    @Autowired
    public FlashcardsController(DisplayService displayService, CardRepository cardRepository, Scanner scanner) {
        this.displayService = displayService;
        this.cardRepository = cardRepository;
        this.scanner = scanner;
    }

    public void start() {
        while (true) {
            displayService.display("Enter 1 to add a new card");
            displayService.display("Enter 2 to display the dictionary");
            displayService.display("Enter 3 to start the test");
            displayService.display("Enter 4 to exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addCard();
                case 2 -> displayDictionary();
                case 3 -> startTest();
                case 4 -> System.exit(0);
            }
        }
    }

    private void displayDictionary() {
        if (cardRepository.findAll().isEmpty()) {
            System.out.println("No dictionary found");
            return;
        }

        displayService.display("\n" + "DICTIONARY");
        displayService.display("-------------------------------------------------------");
        displayService.display(String.format("| %-15s | %-15s | %-15s |", "English", "Polish", "German"));
        displayService.display("-------------------------------------------------------");

        List<Card> cards = cardRepository.findAll();
        cards.forEach(this::displayCard);

        displayService.display("-------------------------------------------------------");
    }

    private void displayCard(Card card) {
        displayService.display(
                String.format("| %-15s | %-15s | %-15s |",
                        card.getEnglish(), card.getPolish(), card.getGerman()
                )
        );
    }

    private void addCard() {
        displayService.display("\n" + "ADD A NEW FLASHCARD");
        displayService.display("-------------------------------------------------------");
        displayService.display("Enter a word in the following format: english,polish,german");
        displayService.display("Example: apple,jabłko,Apfel");
        displayService.display("-------------------------------------------------------");

        String userInput = scanner.nextLine();

        List<String> translations = List.of(userInput.split(","));

        if (translations.size() != 3) {
            displayService.display("Invalid input, please follow the format");
            return;
        }

        Card card = new Card(
                translations.get(0).trim(),
                translations.get(1).trim(),
                translations.get(2).trim()
        );

        if (cardRepository.findAll().contains(card)) {
            displayService.display("The flashcard is already in the dictionary.");
            return;
        }

        cardRepository.save(card);

        displayService.display("A new flashcard added");
        displayService.display("-------------------------------------------------------");
    }

    private void startTest() {
        List<Card> entries = cardRepository.findAll();

        if (entries.isEmpty()) {
            displayService.display("The dictionary is empty");
            return;
        }

        Random random = new Random();
        Card card = entries.get(random.nextInt(entries.size()));

        String[][] languages = {
                {card.getPolish(), "Polish", card.getEnglish(), "English", card.getGerman(), "German"},
                {card.getEnglish(), "English", card.getPolish(), "Polish", card.getGerman(), "German"},
                {card.getGerman(), "German", card.getPolish(), "Polish", card.getEnglish(), "English"}
        };

        int randomLanguage = random.nextInt(3);
        String questionWord = languages[randomLanguage][0];
        String questionLang = languages[randomLanguage][1];
        String correct1 = languages[randomLanguage][2];
        String correct1Lang = languages[randomLanguage][3];
        String correct2 = languages[randomLanguage][4];
        String correct2Lang = languages[randomLanguage][5];

        displayService.display("\n" + "Translate the card from " + questionLang + ": " + questionWord);
        displayService.display("-------------------------------------------------------");

        displayService.display(correct1Lang + ": ");
        String userAnswer1 = scanner.nextLine().trim().toLowerCase();

        displayService.display(correct2Lang + ": ");
        String userAnswer2 = scanner.nextLine().trim().toLowerCase();

        displayService.display("-------------------------------------------------------");

        if (userAnswer1.equalsIgnoreCase(correct1) && userAnswer2.equalsIgnoreCase(correct2)) {
            displayService.display("Correct!");
        } else {
            displayService.display("Wrong! Correct answer: " + correct1Lang + " = " + correct1 +
                    ", " + correct2Lang + " = " + correct2);
        }

        displayService.display("-------------------------------------------------------");
    }

}
