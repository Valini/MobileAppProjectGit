package com.example.suimi.playwithquiz;

import android.provider.BaseColumns;

public final class QuizContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private QuizContract() {
    }

    /* Inner class that defines the table contents */
    public static class QuizTable implements BaseColumns {
        public static final String TABLE_NAME = "history";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_SCORE = "score";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_DIFFICULTY = "difficulty";

    }
}



