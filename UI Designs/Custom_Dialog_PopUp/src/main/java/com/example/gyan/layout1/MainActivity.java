package com.example.gyan.layout1;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView pass,email;
    AlertDialog ad;

    public void startPopup(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Sign In");
        builder.setMessage("Enter credentials");
        final View view1 = this.getLayoutInflater().inflate(R.layout.dialoge_popup,null);
        ((Button) view1.findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass.setText("email : " + ((EditText)view1.findViewById(R.id.editText)).getText().toString());
                email.setText("password : " + ((EditText)view1.findViewById(R.id.editText2)).getText().toString());
                ad.cancel();
            }
        });
        builder.setView(view1);
        ad = builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pass = (TextView) findViewById(R.id.showpass);
        email = (TextView) findViewById(R.id.email);

        ((Button)findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPopup();
            }
        });
    }
}
