package com.example.suimi.playwithquiz;

import android.app.Dialog;
import android.content.Intent;
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
import java.util.ArrayList;
import java.util.List;

public class PlayGameActivity extends MenuActivity {
    static int NO_OF_QUESTIONS = 5;
    public String mDifficulty = "easy";     // 1 - Easy, 2 - Medium, 3 - Hard

    // Store user's email address
    String mCurrentUser = "";
    // Store jsonstring from api call -> this is for processing to restore data when resume the activity
    String mJsonString;
    // need restore or not(true-restore, false-don't need to restore)
    boolean mIsInstanceStateSaved = false;

    private ViewPager mSlideViewPage;
    private LinearLayout mDotLayout;
    private Button mBtnSubmit;

    private SliderAdapter sliderAdapter;

    private TextView[] mDots;

    private ArrayList<Question> questionList;
    public int score;
    public int[] userAnswer;

    public ArrayList<Question> getQuestionList(){
        return questionList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paly_game);


        if(savedInstanceState != null)
            mIsInstanceStateSaved = true;


        // fetch email address passed by dialog(MainActivity)
        Intent intentReceived = getIntent();
        if(intentReceived.hasExtra(Intent.EXTRA_TEXT)){
            mCurrentUser = intentReceived.getStringExtra(Intent.EXTRA_TEXT);
        }
        if(intentReceived.hasExtra(Intent.EXTRA_SUBJECT)){
            mDifficulty = intentReceived.getStringExtra(Intent.EXTRA_SUBJECT);
        }


        Log.i(MenuActivity.LOG_TAG, "On Create");

    }

    @Override
    protected  void onResume(){
        super.onResume();

        if(!mIsInstanceStateSaved)
            getQuestions();
        else {
            parseJSONString();
        }
        Log.i(MenuActivity.LOG_TAG, "On Resume");

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        userAnswer = sliderAdapter.userAnswer;

        outState.putString("JSON_QUIZ", mJsonString);
        outState.putIntArray("USER_ANSWERS", userAnswer);

        Log.i(MenuActivity.LOG_TAG,  "In onSaveInstanceState");
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mJsonString = savedInstanceState.getString("JSON_QUIZ");
        userAnswer = savedInstanceState.getIntArray("USER_ANSWERS");

        Log.i(MenuActivity.LOG_TAG,  "In onRestoreInstanceState");
    }


    @Override
    public void onBackPressed (){
        NavUtils.navigateUpTo(this, NavUtils.getParentActivityIntent(this));
    }


    // event handler for clicking button SUBMIT
    public void submitUserAnswers(){
        // make score using user's answers
        userAnswer = sliderAdapter.userAnswer;
        score = 0;
        for(int i=0; i < userAnswer.length; i++){
            View pageView = sliderAdapter.getPageItem(i);

            if (pageView != null){
                Question q = questionList.get(i);
                if(q.isCorrectAnswer(userAnswer[i])) score++;

                if(userAnswer[i] >= 0) {
                    Log.i(MenuActivity.LOG_TAG, "\n" + "Question No." + (i + 1) + " - " +
                            q.getNthChoice(userAnswer[i]) + " : " + q.getAnswer() + "(" + q.getAnswerIdx() + ")");
                }else{
                    Log.i(MenuActivity.LOG_TAG, "User haven't answered this question.\n");
                }
            }
        }

        Log.i(MenuActivity.LOG_TAG, "SCORE : " + score);
        ShowAskSaveDialog();
    }



    public void ShowAskSaveDialog(){
        boolean isAnsweredAll = true;
        for(int i=0; i<userAnswer.length;i++){
            if(userAnswer[i] == -1){
                isAnsweredAll = false;
                break;
            }
        }
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_ask_save);
        dialog.setTitle("Info");

        TextView tvInfo = dialog.findViewById(R.id.tvInfo);
        if(!isAnsweredAll) {
            tvInfo.setText("Score : " + score + "/" + NO_OF_QUESTIONS + "\nSave as missing question?");
        }else{
            tvInfo.setText("Score : " + score + "/" + NO_OF_QUESTIONS + "\nDo you want to save?");
        }

        // set the custom dialog components - text, button
        Button saveButton = dialog.findViewById(R.id.btnSave);
        Button cancelButton = dialog.findViewById(R.id.btnCancel);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(MenuActivity.LOG_TAG, "Saving... and Sending an email...");
                HistoryDbHelper dbHelper = new HistoryDbHelper(PlayGameActivity.this);
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

                dbHelper.saveScoreToDB(mCurrentUser, score, difficulty);

                sendEmail();
                dialog.dismiss();
            }
        });

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

        sliderAdapter = new SliderAdapter(this, questionList);

        if(mIsInstanceStateSaved)
            sliderAdapter.userAnswer = userAnswer;


        mSlideViewPage.setAdapter(sliderAdapter);

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
            mDots[i].setTextColor(getResources().getColor(R.color.colorAccent));

            mDotLayout.addView(mDots[i]);
        }

        if(mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

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

            userAnswer = sliderAdapter.userAnswer;

            if(userAnswer[position] > -1){
                RadioGroup rgChoices = sliderAdapter.getPageItem(position).findViewById(R.id.rgChoices);
                switch(userAnswer[position]) {
                    case 0:
                        rgChoices.check(R.id.rbChoice1);
                        break;
                    case 1:
                        rgChoices.check(R.id.rbChoice2);
                        break;
                    case 2:
                        rgChoices.check(R.id.rbChoice3);
                        break;
                    case 3:
                        rgChoices.check(R.id.rbChoice4);
                        break;
                }
            }


        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    public class FetchDataFromApi extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL myUrl = urls[0];
            String response = "";
            try {
                response = NetworkUtility.getResponseFromHttpUrl(myUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }


        @Override
        protected void onPostExecute(String s) {
            mJsonString = s;

            parseJSONString();
        }

    }

    public void parseJSONString(){
        questionList = new ArrayList<>();

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
                questionList.add(question);
            }

            createSlides();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void sendEmail(){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        //i.setData(Uri.parse("mailto:"));
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{mCurrentUser});
        i.putExtra(Intent.EXTRA_SUBJECT, "Quiz Whiz");
        i.putExtra(Intent.EXTRA_TEXT   , "Quiz Whiz : Game Score - [" + score + "]");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));

            new WaitEmail().execute();

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(PlayGameActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public class WaitEmail extends AsyncTask<String, Void, List<History>> {
        @Override
        protected List<History> doInBackground(String... strings) {
            try {
                Thread.sleep(3000);

                Intent intentHistory = new Intent(PlayGameActivity.this, HistoryActivity.class);
                startActivity(intentHistory);

            }catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

    }
}

