package com.bignerdranch.android.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private TextView mAnswerTextView;
    private Button mCheatButton;
    private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
    private static final String EXTRA_CHEATED = "com.bignerdranch.android.geoquiz.cheated";
    private static final String KEY_CHEATED = "cheated";
    private boolean mAnswer;
    private boolean mCheated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if(savedInstanceState != null){
            mCheated = savedInstanceState.getBoolean(KEY_CHEATED,false);
        }

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);
        mCheatButton = (Button) findViewById(R.id.show_answer_button);
        mAnswer = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        if(mCheated){
            if(mAnswer){
                mAnswerTextView.setText(R.string.true_button);
            }else{
                mAnswerTextView.setText(R.string.false_button);
            }

            Intent i = new Intent();
            i.putExtra(EXTRA_CHEATED, true);
            setResult(RESULT_OK, i);
        }

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAnswer){
                    mAnswerTextView.setText(R.string.true_button);
                }else{
                    mAnswerTextView.setText(R.string.false_button);
                }

                mCheated = true;
                Intent i = new Intent();
                i.putExtra(EXTRA_CHEATED, true);
                setResult(RESULT_OK, i);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mCheatButton.getWidth() / 2;
                    int cy = mCheatButton.getHeight() / 2;
                    float radius = mCheatButton.getWidth();
                    Animator anim = ViewAnimationUtils.createCircularReveal(mCheatButton, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mAnswerTextView.setVisibility(View.VISIBLE);
                            mCheatButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                }else{
                    mAnswerTextView.setVisibility(View.VISIBLE);
                    mCheatButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(KEY_CHEATED, mCheated);
    }

    public static Intent newIntent(Context packageContext, boolean answer){
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, answer);
        return i;
    }

    public static boolean cheated(Intent i){
        return i.getBooleanExtra(EXTRA_CHEATED, false);
    }
}
