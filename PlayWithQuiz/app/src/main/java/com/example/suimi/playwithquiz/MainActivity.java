package com.example.suimi.playwithquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickBtnPlayGame(View view){
        Intent intentPlayGame = new Intent(MainActivity.this, PalyGameActivity.class);
//        intentToStartActivity.putExtra(Intent.EXTRA_TEXT, "Sent from Main Activity");
//        intentToStartActivity.putExtra(Intent.EXTRA_SUBJECT, "Comp science");
        startActivity(intentPlayGame);
    }

    public void clickBtnShowHistory(View view){
        Intent intentHistory = new Intent(MainActivity.this, HistoryActivity.class);
//        intentToStartActivity.putExtra(Intent.EXTRA_TEXT, "Sent from Main Activity");
//        intentToStartActivity.putExtra(Intent.EXTRA_SUBJECT, "Comp science");
        startActivity(intentHistory);

    }
}
