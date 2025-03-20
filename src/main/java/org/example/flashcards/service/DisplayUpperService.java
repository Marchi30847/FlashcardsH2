package org.example.flashcards.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("upper")
public class DisplayUpperService implements DisplayService {

    @Override
    public void display(String text) {
        System.out.println(text.toUpperCase());
    }
}
