package org.example.flashcards.service.display;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("lower")
public class LowerFormatService implements FormatService {

    @Override
    public String format(String text) {
        return text.toLowerCase();
    }
}
