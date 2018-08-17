package com.example.suimi.playwithquiz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class HistoryDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "scoredb.db";
     private SQLiteDatabase db;

     /*
    public HistoryDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }*/

    public HistoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
/*
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table history (email text, score integer, date text, difficulty integer)");
    }
    */

    static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + QuizContract.QuizTable.TABLE_NAME + " (" +
                    QuizContract.QuizTable._ID + " INTEGER PRIMARY KEY," +
                    QuizContract.QuizTable.COLUMN_EMAIL + " TEXT," +
                    QuizContract.QuizTable.COLUMN_SCORE + " INTEGER," +
                    QuizContract.QuizTable.COLUMN_DATE + " TEXT," +
                    QuizContract.QuizTable.COLUMN_DIFFICULTY + " INTEGER)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + QuizContract.QuizTable.TABLE_NAME;



        public void onCreate(SQLiteDatabase db){

            this.db = db;
            db.execSQL(SQL_CREATE_ENTRIES);

        }
     /*
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists history");
        onCreate(sqLiteDatabase);
    }
    */

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
        }
}
