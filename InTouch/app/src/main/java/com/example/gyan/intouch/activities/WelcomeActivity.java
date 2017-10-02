package com.example.gyan.intouch.activities;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.gyan.intouch.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {

    ImageView imageView;
    float startAlpha = 1f,endAlpha = 0f;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        imageView = (ImageView) findViewById(R.id.imageView2);

        if (imageView == null) throw new AssertionError();

        firebaseAuth = FirebaseAuth.getInstance();

        final ValueAnimator animator = ValueAnimator.ofFloat(startAlpha, endAlpha);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(1100);
        animator.start();

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float t;
                if ((float)animation.getAnimatedValue() - endAlpha < 0) t = -1 * ((float)animation.getAnimatedValue() - endAlpha);
                else t = ((float)animation.getAnimatedValue() - endAlpha);
                imageView.setAlpha(t);
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                float temp = startAlpha;
                startAlpha = endAlpha;
                endAlpha = temp;
            }
        });

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2500);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (user == null){
                        startActivity(new Intent(getApplicationContext(),startPageActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();
                    }else {
                        startActivity(new Intent(getApplicationContext(),MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();
                    }
                }
            }
        };
        thread.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
    }
}
