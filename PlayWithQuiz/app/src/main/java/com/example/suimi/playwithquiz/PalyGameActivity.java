package com.example.suimi.playwithquiz;

import android.app.Dialog;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PalyGameActivity extends AppCompatActivity {
    String currentUser = "";

    private ViewPager mSlideViewPage;
    private LinearLayout mDotLayout;

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
        // custom dialog
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_enter_email);
        dialog.setTitle("Enter Email");

        // set the custom dialog components - text, button
        Button dialogButton = (Button)dialog.findViewById(R.id.btnStart);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etEmail = dialog.findViewById(R.id.etEmail);
                currentUser = etEmail.getText().toString();

                getQuestions();

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

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
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
