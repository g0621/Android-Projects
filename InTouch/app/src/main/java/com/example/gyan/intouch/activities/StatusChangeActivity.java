package com.example.gyan.intouch.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.gyan.intouch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusChangeActivity extends AppCompatActivity {

    private String status;
    private Toolbar toolbar;
    private EditText editText;
    private DatabaseReference reference;

    private void tost(View view, String s) {
        Snackbar.make(view, s, Snackbar.LENGTH_LONG).show();
    }

    public void changetheStatus(final View view){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        status = editText.getText().toString();
        if (!status.equals("")){
            reference.setValue(editText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        tost(view,"Status changed successfully 🙂 ");
                    }else {
                        tost(view,"Unable to connect please try again ☹ ");
                    }
                }
            });
        }else {
            tost(view,"I know u have Status lol.. 🙂");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_change);

        toolbar = (Toolbar) findViewById(R.id.statuspage_appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        status = intent.getStringExtra("status");

        editText = (EditText) findViewById(R.id.change_status_editText);
        editText.setText(status);

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("user_status");
    }
}
