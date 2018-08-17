package com.example.suimi.playwithquiz;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PalyGameActivity extends MenuActivity {
    static int NO_OF_QUESTIONS = 5;

    String currentUser = "";

    private ViewPager mSlideViewPage;
    private LinearLayout mDotLayout;
    private Button btnSubmit;

    private SliderAdapter sliderAdapter;

    private TextView[] mDots;

    private ArrayList<Question> questionList;

    public ArrayList<Question> getQuestionList(){
        return questionList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paly_game);
        btnSubmit = findViewById(R.id.btnSubmit);

        getQuestions();
    }

    public void ShowAskSaveDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_ask_save);
        dialog.setTitle("Info");

        // set the custom dialog components - text, button
        Button saveButton = dialog.findViewById(R.id.btnSave);
        Button cancelButton = dialog.findViewById(R.id.btnCancel);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(MenuActivity.LOG_TAG, "Saving... and Sending an email...");
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
            apiUrl = new URL("https://opentdb.com/api.php?amount=5&difficulty=easy&type=multiple");
            new FetchDataFromApi().execute(apiUrl);
        }catch (MalformedURLException ex){
            ex.printStackTrace();
        }
    }

    public void createSlides(){
        mSlideViewPage = findViewById(R.id.slideViewPager);
        mDotLayout = findViewById(R.id.dotsLayout);

        sliderAdapter = new SliderAdapter(this, questionList);

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

    // event handler for clicking button SUBMIT
    public void submitUserAnswers(View view){
        // make score using user's answers
        int score = 0;
        int[] userAnswer = sliderAdapter.userAnswer;
        for(int i=0; i < userAnswer.length; i++){
            View pageView = sliderAdapter.getPageItem(i);


            if (pageView != null){
                RadioGroup rgChoices = pageView.findViewById(R.id.rgChoices);
                int id = rgChoices.getCheckedRadioButtonId();

                if(id != -1) {
                    if (id == R.id.rbChoice1) userAnswer[i] = 0;
                    else if (id == R.id.rbChoice2) userAnswer[i] = 1;
                    else if (id == R.id.rbChoice3) userAnswer[i] = 2;
                    else if (id == R.id.rbChoice4) userAnswer[i] = 3;

                    Question q = questionList.get(i);
                    if(q.isCorrectAnswer(userAnswer[i])) score++;
                }
            }
        }

        Log.i("PlayWithQuiz", "SCORE : " + score);
        ShowAskSaveDialog();
    }



    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);

            if(i == 4){
                btnSubmit.setVisibility(View.VISIBLE);
            }else{
                btnSubmit.setVisibility(View.INVISIBLE);
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
            questionList = new ArrayList<>();

            try {
                // Convert string to JSONObject
                JSONObject jsonObject = new JSONObject(s);
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

                    // set the question object
                    question.setNthChoiceStr(0, a);
                    String incorrectAnswer = item.getString("incorrect_answers");
                    JSONArray otherChoices = new JSONArray(incorrectAnswer);
                    for(int j=1; j<Question.NO_OF_CHOICES; j++){
                        question.setNthChoiceStr(j, otherChoices.get(j-1).toString());
                    }
                    // mix the choices
                    question.makeChoiceOrder();

                    // add question to the list
                    questionList.add(question);
                }

                createSlides();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
