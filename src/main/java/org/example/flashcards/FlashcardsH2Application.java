package org.example.flashcards;

import org.example.flashcards.controller.FlashcardsController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class FlashcardsH2Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(
                FlashcardsH2Application.class, args
        );

        context.getBean(FlashcardsController.class).start();
    }

}
