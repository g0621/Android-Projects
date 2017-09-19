package com.example.gyan.coolcal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView result;
    String toCalc="";
    Calcu c;

    public void addTocalc(View v){
        String temp = v.getTag().toString();
        toCalc += temp;
        result.setText(toCalc);
    }

    public void startCalc(View v){
        String res = Integer.toString(c.evaluate(toCalc));
        toCalc = res;
        result.setText(res);
    }

    public void clear(View v){
        result.setText("");
        toCalc = "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = (TextView) findViewById(R.id.resultTextView);
        c = new Calcu();
    }
}
