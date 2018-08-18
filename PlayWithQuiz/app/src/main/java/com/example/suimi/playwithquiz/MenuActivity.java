package com.example.suimi.playwithquiz;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    public static String LOG_TAG = "PlayWithQuiz";
    public String mDifficulty = "easy";
    String mEmail = "";

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
        final Button dialogButton = (Button)dialog.findViewById(R.id.btnStart);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etEmail = dialog.findViewById(R.id.etEmail);
                mEmail = etEmail.getText().toString();
                String pattern = "^[-a-z0-9~!$%^&*_=+}{\\'?]+(\\.[-a-z0-9~!$%^&*_=+}{\\'?]+)*@([a-z0-9_][-a-z0-9_]*(\\.[-a-z0-9_]+)*\\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?$";
                if(!mEmail.matches(pattern)){
                    TextView tvEmail = dialog.findViewById(R.id.tvEmail);
                    tvEmail.setText("Enter correct Email!");
                    tvEmail.setTextColor(Color.rgb(200,0,0));
                    etEmail.setText("");
                }else {
                    //getQuestions();
                    Intent intentPlayGame = new Intent(MenuActivity.this, PlayGameActivity.class);
                    intentPlayGame.putExtra(Intent.EXTRA_TEXT, mEmail);
                    intentPlayGame.putExtra(Intent.EXTRA_SUBJECT, mDifficulty);
                    startActivity(intentPlayGame);

                    dialog.dismiss();
                }
            }
        });

        // Event handlers for radio button
        RadioButton rbEasy = dialog.findViewById(R.id.rbEasy);
        rbEasy.setChecked(true);
        rbEasy.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mDifficulty = "easy";
            }
        });
        RadioButton rbMedium = dialog.findViewById(R.id.rbMedium);
        rbMedium.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mDifficulty = "medium";

            }
        });
        RadioButton rbHard = dialog.findViewById(R.id.rbHard);
        rbHard.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mDifficulty = "hard";

            }
        });

        dialog.show();
    }
}
