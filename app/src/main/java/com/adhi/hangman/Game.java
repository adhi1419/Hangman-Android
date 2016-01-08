package com.adhi.hangman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import static java.util.Collections.*;

public class Game extends AppCompatActivity {

    String mQuestion, mHint;
    String[][] mEasyQuestion = {{"SHARK", "A dangerous Aquatic Animal"},
            {"CHAIR", "Its below you"}, {"SMILE", "Facial Expression"},
            {"MOUTH", "Speak"}, {"CLAP", "Appluad"}, {"SLEEP", "Rest"},
            {"DOOR", "Enter a place through this"}, {"BIRD", "Flying"},
            {"CIRCLE", "Has no edges"}, {"KICK", "Attack using foot"}};
    String[][] mIntermediateQuestion = {{"WHISPER", "Talk Softly"},
            {"EARTHQUAKE", "Natural Disaster"}, {"CHOP", "Butcher"},
            {"SEESAW", "Kid's Park Game"},
            {"SOAP", "Cleanse using a ____"},
            {"SCISSORS", "Cutting Instrument"},
            {"TURTLE", "As slow as a _____"}, {"MIRROR", "Reflection"},
            {"SALUTE", "Show Respect"}, {"BANANA", "Fruit"}};
    String[][] mDifficultQuestion = {{"DENTIST", "A type of Doctor"},
            {"PIZZA", "Type of Food"}, {"OWL", "A Bird"},
            {"CHESS", "Board Game"}, {"TORCH", "A Lighting Device"},
            {"FUNNY", "Charlie Chaplin"}, {"DRINK", "Thirsty"},
            {"VOLCANO", "A Natural Disaster"}, {"CHALK", "Board"},
            {"MARSHMALLOW", "Android"}};
    String mTextInput;
    int[] mHangmanLifeCycle = {R.drawable.hangman_stage_1, R.drawable.hangman_stage_2,
            R.drawable.hangman_stage_3, R.drawable.hangman_stage_4,
            R.drawable.hangman_stage_5};
    int mLives;
    int mDifficulty;
    char[] mQuestionArray, mForeverQuestionArray;
    GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent();
                myIntent.setClassName("com.adhi.hangman",
                        "com.adhi.hangman.GameHub");
                startActivity(myIntent);
            }
        });


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mDifficulty = extras.getInt("extras_difficulty");
            mLives = extras.getInt("extras_lives") + 2;
        }

        double randomNumber = Math.random();
        if (mDifficulty == 0) {
            mQuestion = mEasyQuestion[(int) (randomNumber * mEasyQuestion.length)][0];
            mHint = mEasyQuestion[(int) (randomNumber * mEasyQuestion.length)][1];
        } else if (mDifficulty == 1) {
            mQuestion = mIntermediateQuestion[(int) (randomNumber * mIntermediateQuestion.length)][0];
            mHint = mIntermediateQuestion[(int) (randomNumber * mIntermediateQuestion.length)][1];
        } else if (mDifficulty == 2) {
            mQuestion = mDifficultQuestion[(int) (randomNumber * mDifficultQuestion.length)][0];
            mHint = mDifficultQuestion[(int) (randomNumber * mDifficultQuestion.length)][1];
        }

        final TextView mQuestionText = (TextView) findViewById(R.id.question_text);
        final TextView mHintText = (TextView) findViewById(R.id.hint_text);
        final ImageView mHangmanStages = (ImageView) findViewById(R.id.drawable_hangman);
        ImageView mRestartButton = (ImageView) findViewById(R.id.btn_restart);
        ImageView mGameHubButton = (ImageView) findViewById(R.id.btn_home);
        final View mGameFinishLayout = findViewById(R.id.layout_game_finish);
        final View mUserInputLayout = findViewById(R.id.layout_gridview);

        mQuestionText.setText(mQuestion);
        mHintText.setText("Hint: " + mHint);
        mQuestionArray = mQuestion.toCharArray();
        mForeverQuestionArray = mQuestion.toCharArray();

        mHangmanStages.setImageResource(mHangmanLifeCycle[4 - mLives]);

        for (int i = 0; i < mQuestionArray.length; i++) {
            if (!mIsAVowel(mQuestionArray[i])) {
                mQuestionArray[i] = '�';
            }
            mQuestionText.setText(new String(mQuestionArray));
        }

        ArrayList<String> mRandomChars = new ArrayList<String>();
        ArrayList<String> mCharsInQues = new ArrayList<String>();

        for (int i = 0; i < mForeverQuestionArray.length; i++) {
            if (!mCharsInQues.contains(new String(String.valueOf(mForeverQuestionArray[i])))
                    && !mIsAVowel(mForeverQuestionArray[i])) {
                mCharsInQues.add(new String(String.valueOf(mForeverQuestionArray[i])));
            }
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            if (!mIsAVowel(c) && !mCharsInQues.contains(new String(String.valueOf(c)))) {
                mRandomChars.add(new String(String.valueOf(c)));
            }
        }
        shuffle(mRandomChars);
        mRandomChars.subList(0, 6).clear();
        for (int i = 0; i < mRandomChars.size(); i++) {
            Log.e("Hangman", mRandomChars.get(i));
        }
        mRandomChars.addAll(mCharsInQues);
        shuffle(mRandomChars);

        String[] mGridViewChars = new String[15];
        mGridViewChars = mRandomChars.toArray(mGridViewChars);

        mGridView = (GridView) findViewById(R.id.gridView1);
        mGridView.setAdapter(new GridViewCustomAdapter(this, mGridViewChars));
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                int c = 0;

                mTextInput = (((TextView) v.findViewById(R.id.grid_item_label))
                        .getText()).toString().toUpperCase(
                        Locale.getDefault());
                for (int i = 0; i < mForeverQuestionArray.length; i++) {
                    if (mQuestionArray[i] == mTextInput.charAt(0)) {
                        c = -1;
                        break;
                    }
                    if (mForeverQuestionArray[i] == mTextInput.charAt(0)) {
                        mQuestionArray[i] = mTextInput.charAt(0);
                        c++;
                    }
                }
                if (c == 0) {
                    mLives--;
                    mHangmanStages.setImageResource(mHangmanLifeCycle[4 - mLives]);
                } else if (c == -1) {
                    Toast.makeText(getApplicationContext(),
                            "Letter Already Exists", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    mQuestionText.setText(new String(mQuestionArray));
                }
                if (mLives == 0) {
                    mQuestionText.setText("GAME OVER!");
                    mUserInputLayout.setVisibility(View.INVISIBLE);
                    mGameFinishLayout.setVisibility(View.VISIBLE);
                }
                if (new String(mQuestionArray).equals(new String(
                        mForeverQuestionArray))) {
                    mHangmanStages.setImageResource(R.drawable.hangman_alive);
                    mUserInputLayout.setVisibility(View.INVISIBLE);
                    mGameFinishLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        mRestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        mGameHubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent();
                myIntent.setClassName("com.adhi.hangman",
                        "com.adhi.hangman.GameHub");
                startActivity(myIntent);
            }
        });
    }


    public boolean mIsAVowel(char inputChar) {
        return "AEIOUaeiou".indexOf(inputChar) != -1;
    }
}
