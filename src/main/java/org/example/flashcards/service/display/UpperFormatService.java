package org.example.flashcards.service.display;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("upper")
public class UpperFormatService implements FormatService {

    @Override
    public String format(String text) {
        return text.toUpperCase();
    }
}
