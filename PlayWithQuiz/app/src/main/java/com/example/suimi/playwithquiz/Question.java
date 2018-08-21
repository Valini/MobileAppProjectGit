package com.example.suimi.playwithquiz;

import java.util.Random;

public class Question {
    public static int NO_OF_CHOICES = 4;

    private String question;
    private String[] choices;
    private String answer;

    public Question(){
        choices = new String[NO_OF_CHOICES];
    }

    public Question(String question, String answer){
        this();
        this.question = question;
        this.answer = answer;
    }

    public void setQuestion(String q){
        question = q;
    }

    public String getQuestion(){
        return question;
    }

    public String getAnswer() { return answer; }

    // return the index of correct answer
    public int getAnswerIdx(){
        for(int i=0; i < NO_OF_CHOICES; i++){
            if (answer.equals(choices[i]))  return i;
        }

        return -1;
    }

    // put a choice in an order random
    public void addChoice(String c){
        Random rand = new Random();

        while(true) {
            int n = rand.nextInt(NO_OF_CHOICES);
            if (choices[n] == null || choices[n].length() <= 0) {
                choices[n] = c;
                break;
            }
        }
    }

    // return string of nth choice
    public String getNthChoice(int nth){
        return choices[nth];
    }

    public void setAnswer(String a){
        answer = a;
    }


    // Check user's answer is correct
    public boolean isCorrectAnswer(int a){
        if ( a < 0 || a > NO_OF_CHOICES)    return false;


        if(choices[a].equals(answer))   return true;

        return false;
    }

    // Check user's answer is correct
    public boolean isCorrectAnswer(String a){
        if (a.equals(answer))
            return true;

        return false;
    }

}
