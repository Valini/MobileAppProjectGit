package com.example.suimi.playwithquiz;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Random;

public class MenuActivity extends AppCompatActivity {

    public static String LOG_TAG = "PlayWithQuiz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int selectedItemId = item.getItemId();

        if(selectedItemId == R.id.miPlayGame) {
            ShowEmailEnteringDialog();

        }else if(selectedItemId == R.id.miShowHistory){
            Intent intentHistory = new Intent(this, HistoryActivity.class);
            startActivity(intentHistory);

        }


        return true;
    }

    public void ShowEmailEnteringDialog(){
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
                Intent intentPlayGame = new Intent(MenuActivity.this, PalyGameActivity.class);
                intentPlayGame.putExtra(Intent.EXTRA_TEXT, etEmail.getText().toString());
                startActivity(intentPlayGame);
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
