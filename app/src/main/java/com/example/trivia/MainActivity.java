package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.trivia.controller.AppController;
import com.example.trivia.data.AnswerlistR;
import com.example.trivia.data.Repository;
import com.example.trivia.databinding.ActivityMainBinding;
import com.example.trivia.model.Question;
import com.example.trivia.util.prefs;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private int index;
    List<Question> questions;
    private int c=0;
    private int sc=0;
    private com.example.trivia.util.prefs prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        prefs = new prefs(MainActivity.this);
        binding.highest.setText("Highest: " + prefs.getHighestScore());
        questions = new Repository().getQuestions(questionArrayList -> {
                    binding.questionAnswer.setText(questionArrayList.get(index).getAnswer());
            binding.questionNumberId.setText("Question: " + (index+1) + "/" + questionArrayList.size());
                }
        );
        binding.nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = (index+1) % questions.size();
                updateQuestion();
            }


        });

        binding.answerTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                updateQuestion();

            }
        });
        binding.answerFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                updateQuestion();


            }
        });



    }
    private void checkAnswer(boolean b) {
        boolean ans = questions.get(index).isAnswerTrue();
        if(b == ans) {
            Snackbar.make(binding.card, R.string.correct_answer, Snackbar.LENGTH_SHORT)
                    .show();
            sc = sc + 100;
            c=1;
        }
        else {
            Snackbar.make(binding.card, R.string.wrong_answer, Snackbar.LENGTH_SHORT)
                    .show();
            if(sc ==0)
                ;
            else
            sc = sc - 100;
        }
        Shake();
    }
    private void Shake()
    {
        if (c==0) {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);
        binding.questionAnswer.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionAnswer.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionAnswer.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    else
    {
        c=0;
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);
        binding.questionAnswer.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionAnswer.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionAnswer.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    }
    private void updateQuestion() {
        String ques = questions.get(index).getAnswer();
        binding.questionAnswer.setText(ques);
        binding.questionNumberId.setText("Question: " + (index+1) + "/" + questions.size());
        binding.score.setText("Score: " + sc);

    }

    @Override
    protected void onPause() {
        prefs.higestscore(sc);
        super.onPause();
    }
}