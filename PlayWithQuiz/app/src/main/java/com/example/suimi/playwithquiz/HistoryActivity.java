package com.example.suimi.playwithquiz;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

        //print the id of the new row inserted
        Log.i("MyTag", "Row Number is " +rowId);



    }

    protected void fetchDataFromDB(){
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



    }
}
