package com.example.gyan.braintest;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Random random = new Random();
    ArrayList<Integer> answers;
    TextView resultText , scoreText , timeLeft,  sumTextView;
    Button startButton,playAgainButton;
    CountDownTimer countDownTimer;
    RelativeLayout gamePlay;
    boolean playing;

    int correctAnsLocation,tempIncorrect,a,b,score = 0, tries = 0;

    public void playAgain(View view){
        playing = true;
        score = 0;tries = 0;
        setScore();genAnswerList();setNumbers();
        countDownTimer.start();
        resultText.setText("");
        playAgainButton.setVisibility(View.INVISIBLE);
    }

    public void start(View view){
        startButton.setVisibility(View.INVISIBLE);
        gamePlay.setVisibility(View.VISIBLE);
        playing = true;
        playAgain(view);
    }

    public void setScore(){
        String st = "" + score + " / " + tries;
        scoreText.setText(st);
    }

    public void setNumbers(){
        for (int i = 0;i <4;i++){
            Button counter = (Button) findViewById(R.id.grid).findViewWithTag("" + i);
            counter.setText(answers.get(i).toString());
        }
    }

    public void genAnswerList(){
        answers = new ArrayList<Integer>();
        a = random.nextInt(21);b = random.nextInt(21);
        sumTextView.setText(""+a+" + "+b);
        correctAnsLocation = random.nextInt(4);
        for (int i = 0;i <4;i++){
            if(i == correctAnsLocation) answers.add(a+b);
            else {
                tempIncorrect = random.nextInt(41);
                while (tempIncorrect == a+b) tempIncorrect = random.nextInt(41);
                answers.add(tempIncorrect);
            }
        }
    }

    public void chooseAnswer(View view){
        if(playing) {
            int t = Integer.parseInt(view.getTag().toString());
            tries++;
            if (t == correctAnsLocation) {
                score++;
                resultText.setText("Correct !!");
                genAnswerList();
                setNumbers();
                setScore();
            } else {
                setScore();
                resultText.setText("Wrong !!");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button) findViewById(R.id.startButton);
        sumTextView = (TextView) findViewById(R.id.sumTextView);
        resultText = (TextView) findViewById(R.id.resultText);
        scoreText = (TextView) findViewById(R.id.scoreText);
        timeLeft = (TextView) findViewById(R.id.timeLeft);
        playAgainButton = (Button) findViewById(R.id.playAgainButton);
        gamePlay = (RelativeLayout) findViewById(R.id.gamePlay);
        playing = false;

        countDownTimer = new CountDownTimer(30100,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                String st = Integer.toString((int)(millisUntilFinished/1000));
                timeLeft.setText(st + "s");
            }

            @Override
            public void onFinish() {
                playing = false;
                timeLeft.setText("0s");
                resultText.setText("Score : "+ score + " / " + tries);
                playAgainButton.setVisibility(View.VISIBLE);
            }
        };
    }
}
