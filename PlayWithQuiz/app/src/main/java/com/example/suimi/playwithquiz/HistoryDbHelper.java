package com.example.suimi.playwithquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.Menu;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


public class HistoryDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "scoredb.db";
    private SQLiteDatabase db;

    public HistoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

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

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }


    public void saveScoreToDB(String email, int score, int difficulty){
        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        SimpleDateFormat currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentTime.setTimeZone(TimeZone.getTimeZone("GMT-04:00"));

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(QuizContract.QuizTable.COLUMN_EMAIL, email);
        values.put(QuizContract.QuizTable.COLUMN_SCORE, score);
        values.put(QuizContract.QuizTable.COLUMN_DATE, currentTime.format(new Date()));
        values.put(QuizContract.QuizTable.COLUMN_DIFFICULTY, difficulty);
        //Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(QuizContract.QuizTable.TABLE_NAME, null, values);


        //print the id of the new row inserted
        Log.i(MenuActivity.LOG_TAG, "Row Number is " +newRowId);

        db.close();
    }

}
