package org.example.flashcards.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("lower")
public class DisplayLowerService implements DisplayService {

    @Override
    public void display(String text) {
        System.out.println(text.toLowerCase());
    }
}
