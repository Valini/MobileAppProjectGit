package com.example.suimi.playwithquiz;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

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
    ArrayList<History> scoreList = new ArrayList<>();
    public boolean mIsSendEmail = false;
    public String mCurrentUser = "";
    public String mMailString = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Stetho.initializeWithDefaults(this);

        Intent intentReceived = getIntent();
        if(intentReceived.hasExtra(Intent.EXTRA_TEXT)){
            mCurrentUser = intentReceived.getStringExtra(Intent.EXTRA_TEXT);
        }
        if(intentReceived.hasExtra(Intent.EXTRA_SUBJECT)){
            mMailString = intentReceived.getStringExtra(Intent.EXTRA_SUBJECT);
        }


         //fetch Score History Data  Async
        new FetchScoreData().execute();

    }


    public void initRecylerView(){

        RecyclerView myRcView = findViewById(R.id.rc_view);
        ScoreAdapter scoreAdapter = new ScoreAdapter(scoreList);
        myRcView.setAdapter(scoreAdapter);
        myRcView.setLayoutManager(new LinearLayoutManager(this));

        if (mCurrentUser.length() > 0 && mMailString.length() >0 ){
            //sendEmail();
            new SendEmail().execute();
            mMailString = "";
        }
    }

    public void onBackPressed (){
        NavUtils.navigateUpTo(this, NavUtils.getParentActivityIntent(this));
    }



    public class FetchScoreData extends AsyncTask<String, Void, ArrayList<History>> {

        @Override
        protected void onPreExecute() {
            /*

            TextView tvResults = findViewById(R.id.tv_results);
            tvResults.setText("Data Starting to Load...");;

*/
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
            String orderby = QuizContract.QuizTable.COLUMN_DATE + " DESC";
            Cursor cursor = db.query(
                    QuizContract.QuizTable.TABLE_NAME,   // The table to query
                    projection,                 // The array of columns to return (pass null to get all)
                    null,               // The columns for the WHERE clause
                    null,           // The values for the WHERE clause
                    null,              // don't group the rows
                    null,              // don't filter by row groups
                    orderby           // The sort order
            );
            cursor.moveToFirst();
            int cursorSize = cursor.getCount();
            //save the data from database to scoreList
            do {
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


            }while (cursor.moveToNext());

            return scoreList;
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(ArrayList<History> scoreList) {
            //display the data in RecyclerView
            initRecylerView();

        }
    }

    public class SendEmail extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            String senderEmail = "suimiedevelop@gmail.com";
            String subject = "Quiz Whiz";

            try{
                GMailSender sender = new GMailSender("suimiedevelop@gmail.com", "suimieDevelop!@#");
                sender.sendMail(subject, mMailString, senderEmail, mCurrentUser);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void a) {
            Toast.makeText(HistoryActivity.this, "Email has sent!",
                    Toast.LENGTH_SHORT).show();
        }
    }

//    public void sendEmail(){
//
//        Intent i = new Intent(Intent.ACTION_SEND);
//        i.setType("message/rfc822");
//        //i.setData(Uri.parse("mailto:"));
//        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{mCurrentUser});
//        i.putExtra(Intent.EXTRA_SUBJECT, "Quiz Whiz");
//        i.putExtra(Intent.EXTRA_TEXT   , mMailString);
//        try {
//            startActivity(Intent.createChooser(i, "Send mail..."));
//        } catch (android.content.ActivityNotFoundException ex) {
//            Toast.makeText(HistoryActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
//        }
//
//
//    }

}
