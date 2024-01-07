package com.mygdx.game.common;

public class Levels {

    private final String playerUsername;
    private int level;

    public Levels(String playerUsername, int level) {
        this.playerUsername = playerUsername;
        this.level = level;
    }

    public Levels() {
        this.playerUsername = "";
        this.level = 0;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getPlayerName() {
        return playerUsername;
    }

    public int getScore() {
        return level;
    }



}
