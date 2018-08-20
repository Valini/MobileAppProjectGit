package com.example.suimi.playwithquiz;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class PlayGameActivity extends MenuActivity {
    static int NO_OF_QUESTIONS = 5;
    private boolean isLoading = false;
    public String mDifficulty = "easy";     // 1 - Easy, 2 - Medium, 3 - Hard

    // Store user's email address
    String mCurrentUser = "";
    // Store jsonstring from api call -> this is for processing to restore data when resume the activity
    String mJsonString;
    // need restore or not(true-restore, false-don't need to restore)
    boolean mIsInstanceStateSaved = false;

    private ViewPager mSlideViewPage;
    private LinearLayout mDotLayout;

    private SliderAdapter mSliderAdapter;

    private TextView[] mDots;

    private ArrayList<Question> mQuestionList;
    public int score;
    public int[] mUserAnswer;   // remember user's answers


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paly_game);

        if(savedInstanceState != null)
            mIsInstanceStateSaved = true;


        // store email address and difficulty user entered which is passed by dialog(MainActivity)
        Intent intentReceived = getIntent();
        if(intentReceived.hasExtra(Intent.EXTRA_TEXT)){
            mCurrentUser = intentReceived.getStringExtra(Intent.EXTRA_TEXT);
        }
        if(intentReceived.hasExtra(Intent.EXTRA_SUBJECT)){
            mDifficulty = intentReceived.getStringExtra(Intent.EXTRA_SUBJECT);
        }


        Log.i(MenuActivity.LOG_TAG, "[OnCreate] EMAIL : " + mCurrentUser + " | DIFFICULTY : " + mDifficulty);
    }

    @Override
    protected  void onResume(){
        super.onResume();

        // prevent to get Q/A from api when user rotate the phone
        if(!mIsInstanceStateSaved)
            // Get new Q/A
            getQuestions();
        else {
            // Use Q/A had before rotate
            parseJSONString();
        }
        Log.i(MenuActivity.LOG_TAG, "On Resume");
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save email address, data from api and user's selection
        mUserAnswer = mSliderAdapter.userAnswer;

        outState.putString("EMAIL", mCurrentUser);
        outState.putString("JSON_QUIZ", mJsonString);
        outState.putIntArray("USER_ANSWERS", mUserAnswer);

        Log.i(MenuActivity.LOG_TAG,  "In onSaveInstanceState");
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // restore the data before stopped
        mCurrentUser = savedInstanceState.getString("EMAIL");
        mJsonString = savedInstanceState.getString("JSON_QUIZ");
        mUserAnswer = savedInstanceState.getIntArray("USER_ANSWERS");

        Log.i(MenuActivity.LOG_TAG,  "In onRestoreInstanceState");
    }


    @Override
    public void onBackPressed (){
        NavUtils.navigateUpTo(this, NavUtils.getParentActivityIntent(this));
    }


    // event handler for clicking button SUBMIT
    public void submitUserAnswers(){
        // get user's Answer from mSliderAdapter(mSliderAdapter keep the user's Answer)
        mUserAnswer = mSliderAdapter.userAnswer;

        // Calculate score
        score = 0;
        for(int i = 0; i < mUserAnswer.length; i++){
            Question q = mQuestionList.get(i);
            if(q.isCorrectAnswer(mUserAnswer[i])) score++;

            if(mUserAnswer[i] >= 0) {
                Log.i(MenuActivity.LOG_TAG, "\n" + "Question No." + (i + 1) + " - " +
                        q.getNthChoice(mUserAnswer[i]) + " : " + q.getAnswer() + "(" + q.getAnswerIdx() + ")");
            }else{
                Log.i(MenuActivity.LOG_TAG, "User haven't answered this question.\n");
            }
        }

        Log.i(MenuActivity.LOG_TAG, "SCORE : " + score);
        ShowAskSaveDialog();
    }



    public void ShowAskSaveDialog(){
        boolean isAnsweredAll = true;

        // check user answered every questions or not
        for(int i = 0; i< mUserAnswer.length; i++){
            if(mUserAnswer[i] == -1){
                isAnsweredAll = false;
                break;
            }
        }

        // Display dialog, the message will differ depending on user answered all or not
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_ask_save);
        dialog.setTitle("Info");

        TextView tvInfo = dialog.findViewById(R.id.tvInfo);
        if(!isAnsweredAll) {
            tvInfo.setText("Score : " + score + "/" + NO_OF_QUESTIONS + "\nYou missed some questions. Save?");
        }else{
            tvInfo.setText("Score : " + score + "/" + NO_OF_QUESTIONS + "\nDo you want to save?");
        }

        // set the custom dialog components - text, button
        Button saveButton = dialog.findViewById(R.id.btnSave);
        Button cancelButton = dialog.findViewById(R.id.btnCancel);

        // event listener for save
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Log.i(MenuActivity.LOG_TAG, "Saving... and Sending an email...");

            // data for saving into db
            int difficulty=0;
            if (mDifficulty.equalsIgnoreCase("easy")){
                difficulty=0;
            }
            else if (mDifficulty.equalsIgnoreCase("medium")){
                difficulty=1;
            }
            else if (mDifficulty.equalsIgnoreCase("hard")) {
                difficulty=2;
            }

            // Save result into db
           // HistoryDbHelper dbHelper = new HistoryDbHelper(PlayGameActivity.this);
            //dbHelper.saveScoreToDB(mCurrentUser, score, difficulty);

            saveScoreToDB(mCurrentUser, score, difficulty);

            moveToHistoryActivityAndSendEmail();

            dialog.dismiss();
            }
        });

        // event listener for cancel
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    // make query to get questions using api from trivia
    public void getQuestions(){
        URL apiUrl;
        try {
            apiUrl = new URL("https://opentdb.com/api.php?amount=5&difficulty=" + mDifficulty + "&type=multiple");
            Log.i(MenuActivity.LOG_TAG, "https://opentdb.com/api.php?amount=5&difficulty=" + mDifficulty + "&type=multiple");
            new FetchDataFromApi().execute(apiUrl);


        }catch (MalformedURLException ex){
            ex.printStackTrace();
        }
    }

    public void createSlides(){
        mSlideViewPage = findViewById(R.id.slideViewPager);
        mDotLayout = findViewById(R.id.dotsLayout);

        mSliderAdapter = new SliderAdapter(this, mQuestionList);

        if(mIsInstanceStateSaved)
            mSliderAdapter.userAnswer = mUserAnswer;


        mSlideViewPage.setAdapter(mSliderAdapter);

        addDotsIndicator(0);
        mSlideViewPage.addOnPageChangeListener(viewListener);
    }

    public void addDotsIndicator(int position){
        mDots = new TextView[5];

        mDotLayout.removeAllViews();
        for(int i=0; i < mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.holo_blue_dark));

            mDotLayout.addView(mDots[i]);
        }

        if(mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    // remove special characters in string from api
    private String replaceSpecialCharater(String s){
        // eliminate spacial characters
        s = s.replace("&nbsp;", " ");
        s = s.replace("&lt;", "<");
        s = s.replace("&gt;", ">");
        s = s.replace("&amp;", "&");
        s = s.replace("&quot;", "'");
        s = s.replace("&#039;", "'");
        s = s.replace("&eacute;", "é");
        s = s.replace("&shy;", "-");
        s = s.replace("&#173;", "-");
        return s;
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    public class FetchDataFromApi extends AsyncTask<URL, Void, String> {
        TextView tvLoading = findViewById(R.id.tvLoading);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvLoading.setVisibility(View.VISIBLE);
            isLoading = true;
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL myUrl = urls[0];
            String response = "";
            try {
                response = NetworkUtility.getResponseFromHttpUrl(myUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            isLoading = false;

            return response;
        }


        @Override
        protected void onPostExecute(String s) {
            mJsonString = s;

            tvLoading.setVisibility(View.INVISIBLE);
            parseJSONString();
        }

    }

    public void parseJSONString(){
        mQuestionList = new ArrayList<>();

        try {
            mJsonString = replaceSpecialCharater(mJsonString);
            // Convert string to JSONObject
            JSONObject jsonObject = new JSONObject(mJsonString);
            // Get only results part as a string
            String results = jsonObject.getString("results");
            // Make array with results string
            JSONArray jsonArray = new JSONArray(results);

            for(int i=0; i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);
                // Fetch from json string
                String q = item.getString("question");
                String a = item.getString("correct_answer");

                // Create question object with q and a
                Question question = new Question(q, a);

                // set the choices object
                question.addChoice(a);
                String incorrectAnswer = item.getString("incorrect_answers");
                JSONArray otherChoices = new JSONArray(incorrectAnswer);
                for(int j=0; j<Question.NO_OF_CHOICES-1; j++){
                    question.addChoice(otherChoices.get(j).toString());
                }

                // add question to the list
                mQuestionList.add(question);
            }

            createSlides();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void moveToHistoryActivityAndSendEmail(){
        String resultString = "Quiz Whiz : Game Score - [" + score + "/" + NO_OF_QUESTIONS + "]";

        Intent intentHistory = new Intent(PlayGameActivity.this, HistoryActivity.class);
        intentHistory.putExtra(Intent.EXTRA_TEXT, mCurrentUser);
        intentHistory.putExtra(Intent.EXTRA_SUBJECT, resultString);

        startActivity(intentHistory);
    }

    public void saveScoreToDB(String email, int score, int difficulty){
        // Gets the data repository in write mode
        //SQLiteDatabase db = getWritableDatabase();

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

        //db.close();
    }

}

