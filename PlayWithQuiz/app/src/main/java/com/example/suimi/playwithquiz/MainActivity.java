package com.example.suimi.playwithquiz;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.stetho.Stetho;

public class MainActivity extends MenuActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_enter_email);
        dialog.setTitle("Enter Email");

        // set the custom dialog components - text, button
        Button dialogButton = (Button)dialog.findViewById(R.id.btnStart);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etEmail = dialog.findViewById(R.id.etEmail);
                //currentUser = etEmail.getText().toString();

                //getQuestions();
                Intent intentPlayGame = new Intent(MainActivity.this, PalyGameActivity.class);
                intentPlayGame.putExtra(Intent.EXTRA_TEXT, etEmail.getText().toString());
//        intentPlayGame.putExtra(Intent.EXTRA_SUBJECT, "Comp science");
                startActivity(intentPlayGame);
                dialog.dismiss();
            }
        });

        dialog.show();

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
