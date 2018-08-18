package com.example.suimi.playwithquiz;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.OneItemViewHolder>{

    ArrayList<History> scoreList;
    ScoreAdapter(ArrayList<History> list){

        scoreList = list;
    }
    @NonNull
    @Override
    public ScoreAdapter.OneItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View inflatedView = inflater.inflate(R.layout.per_item_view_layout, viewGroup, false);

        return new OneItemViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull OneItemViewHolder oneItemViewHolder, final int i) {

       // oneItemViewHolder.tvOne.setText(scoreList.get(i).getId()+"");
        oneItemViewHolder.tvOneItemEmail.setText(scoreList.get(i).getEmail());
        oneItemViewHolder.tvSecondItemScore.setText(scoreList.get(i).getScore()+""+" / 5");
        oneItemViewHolder.tvThirdItemDate.setText(scoreList.get(i).getDate());
        oneItemViewHolder.tvFourthItemDifficulty.setText(scoreList.get(i).getDifficultyStr());
        oneItemViewHolder.singleItemParentLayout.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                Log.d("recycleView", "Clicked card Number" + Integer.toString(i));


            }
        });

    }


            @Override
            public int getItemCount() {
                return scoreList.size();
            }

            class OneItemViewHolder extends RecyclerView.ViewHolder {
                TextView tvOne;
                TextView tvOneItemEmail;
                TextView tvSecondItemScore;
                TextView tvThirdItemDate;
                TextView tvFourthItemDifficulty;

                FrameLayout singleItemParentLayout;

                public OneItemViewHolder(@NonNull View itemView) {
                    super(itemView);
                    tvOneItemEmail = itemView.findViewById(R.id.tv_in_itemEmail);
                    tvSecondItemScore = itemView.findViewById(R.id.tv_in_item2Score);
                    tvThirdItemDate = itemView.findViewById(R.id.tv_in_item3Date);
                    tvFourthItemDifficulty = itemView.findViewById(R.id.tv_in_item4Difficulty);
                    singleItemParentLayout = itemView.findViewById(R.id.single_item_parent_layout);
                }


            }





    }

