package com.example.suimi.playwithquiz;

import java.util.Random;

public class Question {
    public static int NO_OF_CHOICES = 4;

    private String question;
    private String[] choices;
    private int[] choicesOrder;
    private String answer;

    public Question(){
        choices = new String[NO_OF_CHOICES];
        choicesOrder = new int[NO_OF_CHOICES];
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

    public String getNthChoiceStr(int position){
        return choices[choicesOrder[position]];
    }

    public void setNthChoiceStr(int position, String text){
        choices[position] = text;
    }

    public void setAnswer(String a){
        answer = a;
    }

    public boolean isCorrectAnswer(int a){
        for(int i=0; i < NO_OF_CHOICES; i++){
            if(choicesOrder[i] == a && choices[i].equals(answer)){
                return true;
            }
        }

        return false;
    }

    public boolean isCorrectAnswer(String a){
        if (a.equals(answer))
            return true;

        return false;
    }


    public void makeChoiceOrder(){
        Random rand = new Random();
        int i = 0;

        while(i < 4) {
            boolean isDone = true;
            int n = rand.nextInt(NO_OF_CHOICES);
            for(int j=0; j < i; j++) {
                if(choicesOrder[j] == n) {
                    isDone = false;
                    break;
                }
                if(!isDone) break;
            }
            if (isDone) {
                choicesOrder[i] = n;
                i++;
            }
        }
    }
}
