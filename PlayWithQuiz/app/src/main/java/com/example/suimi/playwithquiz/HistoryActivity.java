package com.example.suimi.playwithquiz;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);



        //save data sample to test
        saveValuesToDB();

        //fetch the score data from the database
       fetchDataFromDB();

    }


    public void saveValuesToDB(){

        /*
       HistoryDbHelper dbHelper = new HistoryDbHelper(this, "scoredb", null, 1);
        //creating an instance of the db
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //to help you insert data
        ContentValues values = new ContentValues();
        values.put("email", "mac@com");
        values.put("score", 3);
        values.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        values.put("difficulty", 1);
        //nullCoLumnHack:null does not allow null entry. To allow null pass  nullCoLumnHack:"name"
        long rowId = db.insert("history", null,values);
*/
        HistoryDbHelper dbHelper = new HistoryDbHelper(this);
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(QuizContract.QuizTable.COLUMN_EMAIL, "newEmail@mac.com");
        values.put(QuizContract.QuizTable.COLUMN_SCORE, 2);
        values.put(QuizContract.QuizTable.COLUMN_DATE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        values.put(QuizContract.QuizTable.COLUMN_DIFFICULTY, 2);
        //Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(QuizContract.QuizTable.TABLE_NAME, null, values);


        //print the id of the new row inserted
        Log.i("MyTag", "Row Number is " +newRowId);



    }

    protected void fetchDataFromDB(){
        /*
        HistoryDbHelper dbHelper = new HistoryDbHelper(this, "scoredb", null, 1);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //mention which columns to fetch
        String [] columns ={"email", "score", "date", "difficulty"};
        Cursor cursor = db.query("history", columns, null, null, null, null, null );

        cursor.moveToFirst();
        Log.i("ValueFromDB", cursor.getString(0));

        TextView tvResults = findViewById(R.id.tv_results);
        tvResults.setText("Score:" + "\n");

        while (cursor.moveToNext()) {
            String email= cursor.getString(0);
            Integer score = cursor.getInt(1);
            String date = cursor.getString(2);
            Integer difficulty=cursor.getInt(3);


            //tvResults.append(cursor.getString(0) + "||" +cursor.getString(1) + "||" + cursor.getString(2) + cursor.getString(3)+ "\n");
            tvResults.append(email + "||" +score + "||" + date + "||" +difficulty+ "\n");
        }
*/
        HistoryDbHelper dbHelper = new HistoryDbHelper(this);
        SQLiteDatabase db =dbHelper.getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                QuizContract.QuizTable.COLUMN_EMAIL,
                QuizContract.QuizTable.COLUMN_SCORE,
                QuizContract.QuizTable.COLUMN_DATE,
                QuizContract.QuizTable.COLUMN_DIFFICULTY
        };



        Cursor cursor = db.query(
                QuizContract.QuizTable.TABLE_NAME,   // The table to query
                projection,                 // The array of columns to return (pass null to get all)
                null,               // The columns for the WHERE clause
                null,           // The values for the WHERE clause
                null,              // don't group the rows
                null,              // don't filter by row groups
                null             // The sort order
                );

        cursor.moveToFirst();
        Log.i("ValueFromDB", cursor.getString(0));

        TextView tvResults = findViewById(R.id.tv_results);
        tvResults.setText("Score:" + "\n");

        while (cursor.moveToNext()) {
            String email= cursor.getString(1);
            Integer score = cursor.getInt(2);
            String date = cursor.getString(3);
            Integer difficulty=cursor.getInt(4);


            //tvResults.append(cursor.getString(0) + "||" +cursor.getString(1) + "||" + cursor.getString(2) + cursor.getString(3)+ "\n");
            tvResults.append(email + "||" +score + "||" + date + "||" +difficulty+ "\n");
        }

    }
}
