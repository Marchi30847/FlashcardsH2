package org.example.flashcards.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("original")
public class DisplayOriginalService implements DisplayService {

    @Override
    public void display(String text) {
        System.out.println(text);
    }
}
