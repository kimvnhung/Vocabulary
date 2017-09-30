package com.kimvan.hung.vocabulary.testWord;

/**
 * Created by h on 09/09/2017.
 */

public class GameProperties {
    private String name_of_the_game,description;

    public GameProperties() {
    }

    public GameProperties(String name_of_the_game, String description) {
        this.name_of_the_game = name_of_the_game;
        this.description = description;
    }

    public String getName_of_the_game() {
        return name_of_the_game;
    }

    public void setName_of_the_game(String name_of_the_game) {
        this.name_of_the_game = name_of_the_game;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
