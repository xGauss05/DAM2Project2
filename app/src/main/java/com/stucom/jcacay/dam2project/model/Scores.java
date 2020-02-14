package com.stucom.jcacay.dam2project.model;

import com.google.gson.annotations.SerializedName;

public class Scores {

    @SerializedName("level")
    private int level;
    @SerializedName("score")
    private int score;
    @SerializedName("playedAt")
    private String playedAt;

    public Scores(int level, int score, String playedAt) {
        this.level = level;
        this.score = score;
        this.playedAt = playedAt;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPlayedAt() {
        return playedAt;
    }

    public void setPlayedAt(String playedAt) {
        this.playedAt = playedAt;
    }
}
