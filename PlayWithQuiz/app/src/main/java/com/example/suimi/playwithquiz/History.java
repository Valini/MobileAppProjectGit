package com.example.suimi.playwithquiz;

public class History {
    String email;
    int score;
    String date;
    int difficulty;

    History(){}

    History(String email, int score){
        this.email = email;
        this.score = score;
    }

    History(String email, int score, String date, int difficulty){
        this(email, score);
        this.date = date;
        this.difficulty = difficulty;
    }


}
