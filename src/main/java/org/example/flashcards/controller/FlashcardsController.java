package org.example.flashcards.controller;

import org.example.flashcards.entity.Card;
import org.example.flashcards.exception.DuplicateCardException;
import org.example.flashcards.exception.EmptyDictionaryException;
import org.example.flashcards.exception.IncorrectInputException;
import org.example.flashcards.exception.NonexistentCardException;
import org.example.flashcards.service.CardService;
import org.example.flashcards.service.display.FormatService;
import org.example.flashcards.service.SpringDataCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

@Controller
public class FlashcardsController {
    private final FormatService formatService;
    private final CardService cardService;
    private final Scanner scanner;

    @Autowired
    public FlashcardsController(FormatService formatService, CardService cardService, Scanner scanner) {
        this.formatService = formatService;
        this.cardService = cardService;
        this.scanner = scanner;


    }

    public void start() {
        while (true) {
            System.out.println(formatService.format("Enter 1 to add a new card"));
            System.out.println(formatService.format("Enter 2 to delete a card"));
            System.out.println(formatService.format("Enter 3 to modify a card"));
            System.out.println(formatService.format("Enter 4 to display the dictionary"));
            System.out.println(formatService.format("Enter 5 to sort and display the dictionary"));
            System.out.println(formatService.format("Enter 6 to display cards containing a pattern"));
            System.out.println(formatService.format("Enter 7 to start the test"));
            System.out.println(formatService.format("Enter 8 to exit"));

            if (!scanner.hasNextInt()) {
                System.out.println(formatService.format("Invalid input, please enter a number between 1 and 6."));
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
                case 6 -> displayContainingPattern();
                case 7 -> startTest();
                case 8 -> System.exit(0);
                default ->
                        System.out.println(formatService.format("Invalid choice, please enter a number between 1 and 6."));
            }
        }
    }

    private void addCard() {
        System.out.println(formatService.format("\n" + "ADD A NEW FLASHCARD"));
        displayBreakLine();
        System.out.println(formatService.format("Enter a word in the following format: english,polish,german"));
        System.out.println(formatService.format("Example: apple,jabłko,Apfel"));
        displayBreakLine();

        String userInput = scanner.nextLine();

        List<String> translations = List.of(userInput.split(","));

        if (translations.size() != 3) {
            System.out.println(formatService.format("Invalid input, please follow the format"));
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
            System.out.println(formatService.format("The flashcard is already in the dictionary."));
            return;
        }

        System.out.println(formatService.format("A new flashcard added"));
        displayBreakLine();
    }

    private void deleteCard() {
        System.out.println(formatService.format("\n" + "DELETE AN EXISTING FLASHCARD"));
        displayBreakLine();
        System.out.println(formatService.format("Enter an id of the card you want to delete"));
        displayBreakLine();

        if (!scanner.hasNextLong()) {
            System.out.println(formatService.format("Invalid input, please follow the format"));
            scanner.next();
            return;
        }

        Long id = scanner.nextLong();
        scanner.nextLine();

        try {
            cardService.delete(id);
        } catch (NonexistentCardException e) {
            System.out.println(formatService.format("The flashcard is not in the dictionary."));
            return;
        }

        System.out.println(formatService.format("Your flashcard is deleted"));
        displayBreakLine();
    }

    public void modifyCard() {
        System.out.println(formatService.format("\n" + "MODIFY AN EXISTING FLASHCARD"));
        displayBreakLine();
        System.out.println(formatService.format("Enter an id of the word you want to modify"));
        displayBreakLine();


        if (!scanner.hasNextLong()) {
            System.out.println(formatService.format("Invalid input, please follow the format"));
            scanner.next();
            return;
        }

        Long id = scanner.nextLong();
        scanner.nextLine();

        try {
            Card card = cardService.getCardById(id);

            System.out.println(formatService.format("Enter a new word in English"));
            String englishWord = scanner.nextLine();
            card.setEnglish(englishWord.isBlank() ? card.getEnglish() : englishWord);

            System.out.println(formatService.format("Enter a new word in Polish"));
            String polishWord = scanner.nextLine();
            card.setPolish(polishWord.isBlank() ? card.getPolish() : polishWord);

            System.out.println(formatService.format("Enter a new word in German"));
            String germanWord = scanner.nextLine();
            card.setGerman(germanWord.isBlank() ? card.getGerman() : germanWord);

            cardService.update(card);
        } catch (NonexistentCardException e) {
            System.out.println(formatService.format("The flashcard is not in the dictionary."));
            return;
        }

        System.out.println(formatService.format("Your flashcard is updated"));
        displayBreakLine();
    }

    private void displayDictionary() {
        List<Card> cards;
        try {
            cards = cardService.getDictionary();
        } catch (EmptyDictionaryException e) {
            System.out.println(formatService.format("The dictionary is empty."));
            return;
        }

        displayListAsDictionary(cards);
    }

    private void displayDictionarySorted() {
        System.out.println(formatService.format("\n" + "DICTIONARY SORTED"));
        displayBreakLine();
        System.out.println(formatService.format("Enter one of the following languages to order by: english, polish, german"));
        displayBreakLine();
        String language = scanner.nextLine();

        displayBreakLine();
        System.out.println(formatService.format("Enter how you want to order the dictionary: asc, desc"));
        displayBreakLine();

        String order = scanner.nextLine();

        List<Card> cards;
        try {
            cards = cardService.getDictionarySorted(language, order);
        } catch (EmptyDictionaryException e) {
            System.out.println(formatService.format("The dictionary is empty."));
            return;
        } catch (IncorrectInputException e) {
            System.out.println(formatService.format("Invalid input, please follow the format"));
            return;
        }

        displayListAsDictionary(cards);
    }

    private void displayContainingPattern() {
        System.out.println(formatService.format("\n" + "WORDS CONTAINING PATTERN"));
        displayBreakLine();
        System.out.println(formatService.format("Enter the pattern you want to display"));
        displayBreakLine();
        String pattern = scanner.nextLine();

        List<Card> cards;
        try {
            cards = cardService.searchForAll(pattern);
        } catch (EmptyDictionaryException e) {
            System.out.println(formatService.format("The dictionary is empty."));
            return;
        }
        displayListAsDictionary(cards);
    }

    private void displayListAsDictionary(List<Card> cards) {
        System.out.println(formatService.format("\n" + "DICTIONARY"));
        displayBreakLine();
        System.out.println(formatService.format(
                        String.format("| %-15s | %-15s | %-15s | %-15s |",
                                "Id", "English", "Polish", "German")
                )
        );
        displayBreakLine();

        cards.forEach(this::displayCard);

        displayBreakLine();
    }

    private void displayCard(Card card) {
        System.out.println(formatService.format(
                String.format("| %-15s | %-15s | %-15s | %-15s |",
                        card.getId(), card.getEnglish(), card.getPolish(), card.getGerman()
                )
        ));
    }

    private void startTest() {
        List<Card> cards;
        try {
            cards = cardService.getDictionary();
        } catch (EmptyDictionaryException e) {
            System.out.println(formatService.format("The dictionary is empty."));
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

        System.out.println(formatService.format("Translate the card from " + questionLang + ": " + questionWord));
        displayBreakLine();

        System.out.println(formatService.format(correct1Lang + ": "));
        String userAnswer1 = scanner.nextLine().trim().toLowerCase();

        System.out.println(formatService.format(correct2Lang + ": "));
        String userAnswer2 = scanner.nextLine().trim().toLowerCase();

        displayBreakLine();

        if (userAnswer1.equalsIgnoreCase(correct1) && userAnswer2.equalsIgnoreCase(correct2)) {
            System.out.println(formatService.format("Correct!"));
        } else {
            System.out.println(formatService.format("Wrong! Correct answer: " + correct1Lang + " = " + correct1 +
                    ", " + correct2Lang + " = " + correct2));
        }

        displayBreakLine();
    }

    private void displayBreakLine() {
        System.out.println(formatService.format("-------------------------------------------------------------------------"));
    }

}
