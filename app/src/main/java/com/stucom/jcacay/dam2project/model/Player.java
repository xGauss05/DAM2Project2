package com.stucom.jcacay.dam2project.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Player implements Comparable<Player> {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("image")
    private String image;
    @SerializedName("teamId")
    private int teamId;
    @SerializedName("from")
    private String from;
    @SerializedName("totalScore")
    private int totalScore;
    @SerializedName("lastLevel")
    private int lastLevel;
    @SerializedName("lastScore")
    private int lastScore;
    @SerializedName("scores")
    private List<Scores> scores;

    public Player() {
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public List<Scores> getScores() {
        return scores;
    }

    public void setScores(List<Scores> scores) {
        this.scores = scores;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getLastLevel() {
        return lastLevel;
    }

    public void setLastLevel(int lastLevel) {
        this.lastLevel = lastLevel;
    }

    public int getLastScore() {
        return lastScore;
    }

    public void setLastScore(int lastScore) {
        this.lastScore = lastScore;
    }

    public Player(String name, String email, String image) {
        this.name = name;
        this.email = email;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void loadFromPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        this.name = prefs.getString("playerName", "");
        this.email = prefs.getString("playerEmail", "");
        this.image = prefs.getString("playerAvatar", null);
    }

    public void saveToPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("playerName", name);
        prefsEditor.putString("playerEmail", email);
        prefsEditor.putString("playerAvatar", image);
        prefsEditor.apply();
        prefsEditor.commit();
    }

    public void resetPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.clear();
        prefsEditor.apply();
        prefsEditor.commit();
    }

    @Override
    public int compareTo(@NonNull Player player) {
        if (this.totalScore < player.totalScore) {
            return 1;
        } else if (this.totalScore > player.totalScore) {
            return -1;
        }
        return 0;
    }
}
