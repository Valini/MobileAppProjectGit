package com.example.suimi.playwithquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
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
}
