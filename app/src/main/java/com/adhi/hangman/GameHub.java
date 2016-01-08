package com.adhi.hangman;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

public class GameHub extends AppCompatActivity {

    FloatingActionButton mFab;
    AlertDialog.Builder mAlertDialog;
    String mAlertDialogMessage;
    String mAppVersionName;
    ImageView mStartGame;
    Spinner mDifficultySpinner, mLivesSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_hub);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                about();
            }
        });

        mDifficultySpinner = (Spinner) findViewById(R.id.spinner_difficulty);
        mLivesSpinner = (Spinner) findViewById(R.id.spinner_lives);

        ArrayAdapter<CharSequence> mDifficultySpinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.difficulty_array, R.layout.spinner_layout);
        mDifficultySpinnerAdapter.setDropDownViewResource(R.layout.spinner_layout);

        ArrayAdapter<CharSequence> mLivesSpinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.lives_array, R.layout.spinner_layout);
        mLivesSpinnerAdapter.setDropDownViewResource(R.layout.spinner_layout);

        mDifficultySpinner.setAdapter(mDifficultySpinnerAdapter);
        mLivesSpinner.setAdapter(mLivesSpinnerAdapter);

        mStartGame = (ImageView) findViewById(R.id.button_start);
        mStartGame.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent();
                myIntent.putExtra("extras_difficulty",
                        mDifficultySpinner.getSelectedItemPosition());
                myIntent.putExtra("extras_lives", mLivesSpinner.getSelectedItemPosition());
                myIntent.setClassName("com.adhi.hangman",
                        "com.adhi.hangman.Game");
                startActivity(myIntent);
            }
        });
    }

    private void about() {
        try {
            mAppVersionName = getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mAlertDialogMessage = "Simple Hangman App created as a project under Mobile App Club, BPGC\n\nDeveloped by: Adhithya R\nApp Version: "
                + mAppVersionName;
        mAlertDialog = new AlertDialog.Builder(GameHub.this, R.style.MyAlertDialogStyle);
        mAlertDialog.setTitle("About");
        mAlertDialog.setMessage(mAlertDialogMessage);
        mAlertDialog.setCancelable(false);
        mAlertDialog.setPositiveButton(("Okay"),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface mDialogInterface, int k) {
                        mDialogInterface.cancel();
                    }
                });
        mAlertDialog.create();
        mAlertDialog.show();

    }


}
