package com.example.suimi.playwithquiz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
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
    Context context;
    LayoutInflater layoutInflater;
    public ArrayList<Question> slide_data;
    public int[] userAnswer;
    Object currObj, preObj;

    public SliderAdapter(Context context, ArrayList<Question> data){
        this.context = context;
        slide_data = data;
        userAnswer = new int[5];
        for(int i=0; i < userAnswer.length; i++)
            userAnswer[i] = -1;
    }

    public void submitUserAnswers(View view){
      /*  // make score usin user's answers
        int score = 0;

        for(int i=0; i < answers.length; i++){
            if(isCorrectAnswer(answers[i])) score++;
        }*/
    }

    @Override
    public int getCount() {
        return slide_data.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        TextView tvQuestion = view.findViewById(R.id.tvQuestion);
        RadioButton rbChoice1 = view.findViewById(R.id.rbChoice1);
        RadioButton rbChoice2 = view.findViewById(R.id.rbChoice2);
        RadioButton rbChoice3 = view.findViewById(R.id.rbChoice3);
        RadioButton rbChoice4 = view.findViewById(R.id.rbChoice4);
        Button btnSubmit = view.findViewById(R.id.btnBubmit);

        if(position == 4)
            btnSubmit.setVisibility(View.VISIBLE);
        else
            btnSubmit.setVisibility(View.INVISIBLE);

        Question q = slide_data.get(position);

        tvQuestion.setText(q.getQuestion());
        rbChoice1.setText(q.getNthChoiceStr(0));
        rbChoice2.setText(q.getNthChoiceStr(1));
        rbChoice3.setText(q.getNthChoiceStr(2));
        rbChoice4.setText(q.getNthChoiceStr(3));

        container.addView(view);

        preObj = currObj;
        currObj = view;
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }

    @Override
    public void startUpdate(@NonNull ViewGroup container) {
        super.startUpdate(container);

        if(preObj != null) {
            int position = container.indexOfChild((View)currObj) - 1;
            RadioGroup rgChoices = ((View) preObj).findViewById(R.id.rgChoices);
            RadioButton rbChoice1 = ((View) preObj).findViewById(R.id.rbChoice1);
            RadioButton rbChoice2 = ((View) preObj).findViewById(R.id.rbChoice2);
            RadioButton rbChoice3 = ((View) preObj).findViewById(R.id.rbChoice3);
            RadioButton rbChoice4 = ((View) preObj).findViewById(R.id.rbChoice4);

            int id = rgChoices.getCheckedRadioButtonId();
            //userAnswer[position] = id;
            if (id == R.id.rbChoice1) userAnswer[position] = 0;
            else if (id == R.id.rbChoice2) userAnswer[position] = 1;
            else if (id == R.id.rbChoice3) userAnswer[position] = 2;
            else if (id == R.id.rbChoice4) userAnswer[position] = 3;
        }
    }

}
