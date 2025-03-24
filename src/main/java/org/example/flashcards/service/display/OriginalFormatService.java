package org.example.flashcards.service.display;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("original")
public class OriginalFormatService implements FormatService {

    @Override
    public String format(String text) {
        return text;
    }
}
