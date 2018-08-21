package com.example.suimi.playwithquiz;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.provider.BaseColumns;
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

    // Store user's email address
    String mEmail = "";

    // For db access
    HistoryDbHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        dbHelper = new HistoryDbHelper(this);
        db =dbHelper.getReadableDatabase();

    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
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
        HistoryDbHelper dbHelper = new HistoryDbHelper(this);
        String email = getLastUser();

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_enter_email);
        dialog.setTitle("Enter Email");

        if(email.length() > 0)
            ((TextView)dialog.findViewById(R.id.etEmail)).setText(email);
        // set the custom dialog components - text, button
        final Button dialogButton = (Button)dialog.findViewById(R.id.btnStart);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etEmail = dialog.findViewById(R.id.etEmail);
                mEmail = etEmail.getText().toString();
                mEmail = mEmail.toLowerCase();
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

    public String getLastUser(){
        //SQLiteDatabase db = getWritableDatabase();

        String[] projection = {
                BaseColumns._ID,
                QuizContract.QuizTable.COLUMN_EMAIL,
        };
        String orderby = QuizContract.QuizTable.COLUMN_DATE + " DESC";
        Cursor cursor = db.query(
                QuizContract.QuizTable.TABLE_NAME,   // The table to query
                projection,                 // The array of columns to return (pass null to get all)
                null,               // The columns for the WHERE clause
                null,           // The values for the WHERE clause
                null,              // don't group the rows
                null,              // don't filter by row groups
                orderby,           // The sort order
                "1"
        );
        cursor.moveToFirst();
        //save the data from database to scoreList
        String email = cursor.getString(1);

        //db.close();

        return email;
    }


}
