package com.example.gyan.layout1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editText;

    public void custom_tost(View view){
        Toast c_tost = new Toast(getApplicationContext());
        View t_view = getLayoutInflater().inflate(R.layout.custom_toast_layout,null);
        ((TextView) t_view.findViewById(R.id.tost_text)).setText("Text : "+ editText.getText().toString());
        c_tost.setView(t_view);
        c_tost.setDuration(Toast.LENGTH_LONG);
        c_tost.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);

    }
}
