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
    Context context;
    LayoutInflater layoutInflater;
    public ArrayList<Question> slide_data;
    public int[] userAnswer;
    public View[] pageViews;

    public SliderAdapter(Context context, ArrayList<Question> data){
        this.context = context;
        slide_data = data;
        pageViews = new View[PalyGameActivity.NO_OF_QUESTIONS];
        userAnswer = new int[PalyGameActivity.NO_OF_QUESTIONS];
        for(int i=0; i < userAnswer.length; i++)
            userAnswer[i] = -1;
    }


    @Override
    public int getCount() {
        return slide_data.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout) o;
    }

    public View getPageItem(int position){
        return pageViews[position];
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

        Question q = slide_data.get(position);

        tvQuestion.setText(q.getQuestion());
        rbChoice1.setText(q.getNthChoiceStr(0));
        rbChoice2.setText(q.getNthChoiceStr(1));
        rbChoice3.setText(q.getNthChoiceStr(2));
        rbChoice4.setText(q.getNthChoiceStr(3));

        container.addView(view);

        pageViews[position] = view;


        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
