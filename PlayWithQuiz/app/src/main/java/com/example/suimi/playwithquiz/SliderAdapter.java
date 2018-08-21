package com.example.suimi.playwithquiz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SliderAdapter extends PagerAdapter {
    // Container activity - PlayGameActivity
    Context mContext;
    LayoutInflater layoutInflater;

    // Data source for page
    public ArrayList<Question> slide_data;

    // store user's answer
    public int[] userAnswer;

    // container, data source
    public SliderAdapter(Context context, ArrayList<Question> data) {
        this.mContext = context;

        // data source of slide - ArrayList of questions and choices from api
        slide_data = data;

        // Initialize the userAnswer with -1 => -1 means user didn't enter answer when app calculate score
        userAnswer = new int[PlayGameActivity.NO_OF_QUESTIONS];
        for (int i = 0; i < userAnswer.length; i++)
            userAnswer[i] = -1;
    }


    @Override
    public int getCount() {
        return slide_data.size();
    }

    // check if the view is the object which is created in the method <public Object instantiateItem()>
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout) o;
    }


    // Create a page of viewpager(slider)
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        TextView tvQuestion = view.findViewById(R.id.tvQuestion);
        RadioButton rbChoice1 = view.findViewById(R.id.rbChoice1);
        RadioButton rbChoice2 = view.findViewById(R.id.rbChoice2);
        RadioButton rbChoice3 = view.findViewById(R.id.rbChoice3);
        RadioButton rbChoice4 = view.findViewById(R.id.rbChoice4);

        // get the current Question
        Question q = slide_data.get(position);

        // Initialize textview and ratio button(Question and choices)
        tvQuestion.setText(q.getQuestion());
        rbChoice1.setText(q.getNthChoice(0));
        rbChoice2.setText(q.getNthChoice(1));
        rbChoice3.setText(q.getNthChoice(2));
        rbChoice4.setText(q.getNthChoice(3));

        Button btnSubmit = view.findViewById(R.id.btnSubmit);
        //MAKE SUBMIT BUTTON VISIBLE ON LAST QUESTION
        if (position == 4) {
            // Check user answered all question
            btnSubmit.setVisibility(View.VISIBLE);

            btnSubmit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ((PlayGameActivity) mContext).submitUserAnswers();
                }
            });
        } else {
            btnSubmit.setVisibility(View.INVISIBLE);
        }

        // When phone is rotated, app display user's selection
        // if there are user's choice then check the radio button user selected
        if (userAnswer[position] > -1) {
            RadioGroup rgChoices = view.findViewById(R.id.rgChoices);
            switch (userAnswer[position]) {
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

        // EventListener for radio buttons
        rbChoice1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                userAnswer[position] = 0;
                Log.i("RecycleView", "Radio Button1 Clicked");
            }
        });
        rbChoice2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                userAnswer[position] = 1;
                Log.i("RecycleView", "Radio Button2 Clicked");
            }
        });
        rbChoice3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                userAnswer[position] = 2;
                Log.i("RecycleView", "Radio Button3 Clicked");
            }
        });
        rbChoice4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                userAnswer[position] = 3;
                Log.i("RecycleView", "Radio Button4 Clicked");
            }
        });

        container.addView(view);


        return view;
    }

    // It is called when a page is destoryed
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}


