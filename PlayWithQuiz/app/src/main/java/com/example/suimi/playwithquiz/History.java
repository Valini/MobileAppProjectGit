package com.example.suimi.playwithquiz;

public class History {
    private int id;
    private String email;
    private int score;
    private String date;
    private int difficulty; // 0 - Easy 1 - Medium 2 - Hard

    History(){}

    History(Integer id, String email, int score, String date, int difficulty){
        this.id =id;
        this.email=email;
        this.score=score;
        this.date = date;
        this.difficulty = difficulty;
    }

    History(String email, int score){

        this.email = email;
        this.score = score;
    }

    History(String email, int score, String date, int difficulty){
        this(email, score);
        this.date = date;
        this.difficulty = difficulty;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public int getScore(){
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }

    public void setDate(String date){
        this.date = date;
    }
    public String getDate(){
        return date;
    }

    public String getDifficultyStr(){
        switch(difficulty){
            case 0:
                return "EASY";
            case 1:
                return "MEDIUM";
            case 3:
                return "HARD";
        }

        return "EASY";
    }

    public void setDifficultyStr(String difficulty){
        switch(difficulty){
            case "EASY":
                this.difficulty = 0;
                break;
            case "MEDIUM":
                this.difficulty = 1;
                break;
            case "HARD":
                this.difficulty = 2;
                break;
        }
    }

    public int getDifficulty(){
        return difficulty;
    }

    public void setDifficulty(int difficulty){
        this.difficulty = difficulty;
    }
}
