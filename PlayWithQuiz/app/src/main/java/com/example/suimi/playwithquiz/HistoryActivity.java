package com.example.suimi.playwithquiz;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends MenuActivity {
    HistoryDbHelper dbHelper;
    SQLiteDatabase db;
    ArrayList<History> scoreList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbHelper = new HistoryDbHelper(this);
         db =dbHelper.getReadableDatabase();

        //save data sample to test
        //saveValuesToDB();

        //fetch the score data from the database
       //fetchDataFromDB();

        //fetch Data Async
        new FetchScoreData().execute();
        initRecylerView();


    }

    public void initRecylerView(){
        RecyclerView myRcView = findViewById(R.id.rc_view);
        ScoreAdapter scoreAdapter = new ScoreAdapter(scoreList);

        // studentAdapter.studentList = studentList;

        myRcView.setAdapter(scoreAdapter);
        myRcView.setLayoutManager(new LinearLayoutManager(this));


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
        Date now = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz", Locale.CANADA);
        System.out.println("Format 2:   " + dateFormatter.format(now));
        ContentValues values = new ContentValues();
        values.put(QuizContract.QuizTable.COLUMN_EMAIL, "newEmail@mac.com");
        values.put(QuizContract.QuizTable.COLUMN_SCORE, 2);
        values.put(QuizContract.QuizTable.COLUMN_DATE, dateFormatter.format(now));
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



            tvResults.append(email + "||" +score + "||" + date + "||" +difficulty+ "\n");
        }

    }

    public class FetchScoreData extends AsyncTask<String, Void, ArrayList<History>> {

        @Override
        protected void onPreExecute() {
            TextView tvResults = findViewById(R.id.tv_results);
            tvResults.setText("Data Starting to Load...");;


        }


        @Override
        protected ArrayList<History> doInBackground(String... values) {


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
                Integer id = cursor.getInt(0);
                String email = cursor.getString(1);
                Integer score = cursor.getInt(2);
                String date = cursor.getString(3);
                Integer difficulty = cursor.getInt(4);
                History scoreHistory = new History();
                scoreHistory.setId(id);
                scoreHistory.setEmail(email);
                scoreHistory.setScore(score);
                scoreHistory.setDate(date);
                scoreHistory.setDifficulty(difficulty);
                scoreList.add(scoreHistory);


            }

            return scoreList;
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ArrayList<History> scoreList) {
            TextView tvResults = findViewById(R.id.tv_results);
            for (int i = 0; i < scoreList.size(); i++) {
                History scores = (History) scoreList.get(i);
                tvResults.append(scores.getId()+ "||" + scores.getEmail() + "||" + scores.getScore() + "||"+ scores.getDate() + "||" + scores.getDifficulty() + "\n");
            }




        }
    }
}
