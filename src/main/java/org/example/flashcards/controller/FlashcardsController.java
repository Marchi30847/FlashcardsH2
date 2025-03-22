package org.example.flashcards.controller;

import org.example.flashcards.entity.Card;
import org.example.flashcards.exception.DuplicateCardException;
import org.example.flashcards.exception.EmptyDictionaryException;
import org.example.flashcards.exception.IncorrectInputException;
import org.example.flashcards.exception.NonexistentCardException;
import org.example.flashcards.service.CardService;
import org.example.flashcards.service.DisplayService;
import org.example.flashcards.service.SpringDataCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

@Controller
public class FlashcardsController {
    private final DisplayService displayService;
    private final SpringDataCardService cardService;
    private final Scanner scanner;

    @Autowired
    public FlashcardsController(DisplayService displayService, SpringDataCardService cardService, Scanner scanner) {
        this.displayService = displayService;
        this.cardService = cardService;
        this.scanner = scanner;


    }

    public void start() {
        while (true) {
            displayService.display("Enter 1 to add a new card");
            displayService.display("Enter 2 to delete a card");
            displayService.display("Enter 3 to modify a card");
            displayService.display("Enter 4 to display the dictionary");
            displayService.display("Enter 5 to sort and display the dictionary");
            displayService.display("Enter 6 to start the test");
            displayService.display("Enter 7 to exit");

            if (!scanner.hasNextInt()) {
                displayService.display("Invalid input, please enter a number between 1 and 6.");
                scanner.next();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addCard();
                case 2 -> deleteCard();
                case 3 -> modifyCard();
                case 4 -> displayDictionary();
                case 5 -> displayDictionarySorted();
                case 6 -> startTest();
                case 7 -> System.exit(0);
                default -> displayService.display("Invalid choice, please enter a number between 1 and 6.");
            }
        }
    }

    private void addCard() {
        displayService.display("\n" + "ADD A NEW FLASHCARD");
        displayService.display("-------------------------------------------------------------------------");
        displayService.display("Enter a word in the following format: english,polish,german");
        displayService.display("Example: apple,jabłko,Apfel");
        displayService.display("-------------------------------------------------------------------------");

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

        try {
            cardService.add(card);
        } catch (DuplicateCardException e) {
            displayService.display("The flashcard is already in the dictionary.");
            return;
        }

        displayService.display("A new flashcard added");
        displayService.display("-------------------------------------------------------------------------");
    }

    private void deleteCard() {
        displayService.display("\n" + "DELETE AN EXISTING FLASHCARD");
        displayService.display("-------------------------------------------------------------------------");
        displayService.display("Enter an id of the card you want to delete");
        displayService.display("-------------------------------------------------------------------------");

        try {
            Long id = scanner.nextLong();
            scanner.nextLine();
            cardService.delete(id);
        } catch (InputMismatchException e) {
            displayService.display("Invalid input, please follow the format");
        } catch (NonexistentCardException e) {
            displayService.display("The flashcard is not in the dictionary.");
        }
    }

    public void modifyCard() {
        displayService.display("\n" + "MODIFY AN EXISTING FLASHCARD");
        displayService.display("-------------------------------------------------------------------------");
        displayService.display("Enter an id of the word you want to modify");
        displayService.display("-------------------------------------------------------------------------");

        try {
            Long id = scanner.nextLong();
            scanner.nextLine();
            Card card = cardService.getCardById(id);

            displayService.display("Enter a new word in English");
            String englishWord = scanner.nextLine();
            card.setEnglish(englishWord);

            displayService.display("Enter a new word in Polish");
            String polishWord = scanner.nextLine();
            card.setPolish(polishWord);

            displayService.display("Enter a new word in German");
            String germanWord = scanner.nextLine();
            card.setGerman(germanWord);

            cardService.update(card);
        } catch (InputMismatchException e) {
            displayService.display("Invalid input, please follow the format");
        } catch (NonexistentCardException e) {
            displayService.display("The flashcard is not in the dictionary.");
        }
    }

    private void displayDictionary() {
        List<Card> cards;
        try {
            cards = cardService.getDictionary();
        } catch (EmptyDictionaryException e) {
            displayService.display("The dictionary is empty.");
            return;
        }

        displayListAsDictionary(cards);
    }

    private void displayDictionarySorted() {
        displayService.display("\n" + "DICTIONARY SORTED");
        displayService.display("-------------------------------------------------------------------------");
        displayService.display("Enter one of the following languages to order by: english, polish, german");
        displayService.display("-------------------------------------------------------------------------");

        String language = scanner.nextLine();

        displayService.display("-------------------------------------------------------------------------");
        displayService.display("Enter how you want to order the dictionary: asc, desc");
        displayService.display("-------------------------------------------------------------------------");

        String order = scanner.nextLine();

        List<Card> cards;
        try {
            cards = cardService.getDictionarySorted(language, order);
        } catch (EmptyDictionaryException e) {
            displayService.display("The dictionary is empty.");
            return;
        } catch (IncorrectInputException e) {
            displayService.display("Invalid input, please follow the format");
            return;
        }

        displayListAsDictionary(cards);
    }

    private void displayListAsDictionary(List<Card> cards) {
        displayService.display("\n" + "DICTIONARY");
        displayService.display("-------------------------------------------------------------------------");
        displayService.display(String.format("| %-15s | %-15s | %-15s | %-15s |", "Id", "English", "Polish", "German"));
        displayService.display("-------------------------------------------------------------------------");

        cards.forEach(this::displayCard);

        displayService.display("-------------------------------------------------------------------------");
    }

    private void displayCard(Card card) {
        displayService.display(
                String.format("| %-15s | %-15s | %-15s | %-15s |",
                        card.getId(), card.getEnglish(), card.getPolish(), card.getGerman()
                )
        );
    }

    private void startTest() {
        List<Card> cards;
        try {
            cards = cardService.getDictionary();
        } catch (EmptyDictionaryException e) {
            displayService.display("The dictionary is empty.");
            return;
        }

        Random random = new Random();
        Card card = cards.get(random.nextInt(cards.size()));

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
        displayService.display("-------------------------------------------------------------------------");

        displayService.display(correct1Lang + ": ");
        String userAnswer1 = scanner.nextLine().trim().toLowerCase();

        displayService.display(correct2Lang + ": ");
        String userAnswer2 = scanner.nextLine().trim().toLowerCase();

        displayService.display("-------------------------------------------------------------------------");

        if (userAnswer1.equalsIgnoreCase(correct1) && userAnswer2.equalsIgnoreCase(correct2)) {
            displayService.display("Correct!");
        } else {
            displayService.display("Wrong! Correct answer: " + correct1Lang + " = " + correct1 +
                    ", " + correct2Lang + " = " + correct2);
        }

        displayService.display("-------------------------------------------------------------------------");
    }

}
