package org.example.flashcards.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Card {

    @Id
    @GeneratedValue
    private Long id;

    private String english;
    private String polish;
    private String german;

    public Card(String english, String polish, String german) {
        this.english = english;
        this.polish = polish;
        this.german = german;
    }

    public Card() {}

    public Long getId() {return id;}
    public String getEnglish() {return english;}
    public String getPolish() {return polish;}
    public String getGerman() {return german;}

    public void setEnglish(String english) {this.english = english;}
    public void setPolish(String polish) {this.polish = polish;}
    public void setGerman(String german) {this.german = german;}
}

