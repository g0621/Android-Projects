package com.example.gyan.layout1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    Button button;
    ImageView imageView;
    Animation btnAnim,imgAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        imageView = (ImageView) findViewById(R.id.image);
        imgAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.for_image);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.for_btn);
        button.setAnimation(btnAnim);
        imageView.setAnimation(imgAnim);
    }
}
