package com.example.gyan.intouch.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gyan.intouch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class RegisterActivity extends AppCompatActivity {

    Toolbar toolbar;
    private EditText usernameEdit,passwordEdit,DisplayNameEdit;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private String defalutPic  = "https://firebasestorage.googleapis.com/v0/b/intouch-c470b.appspot.com/o/Profile_img%2Fdefaultpic.jpg?alt=media&token=17290f59-e579-4d91-a132-15ec64721120";
    private String defaultThumb = "https://firebasestorage.googleapis.com/v0/b/intouch-c470b.appspot.com/o/Thumb_img%2Fdefaultpic.png?alt=media&token=10f72031-e87f-48d8-b145-d14e9ebcc261";

    private void tost(View view ,String s){
        Snackbar.make(view , s , Snackbar.LENGTH_SHORT).show();
    }

    public void registerUser(final View view){
        final String email = usernameEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        final String Name = DisplayNameEdit.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(Name)){
            tost(view , "All fields Required :)");
        }else {
            progressDialog.setTitle("Creating Account");
            progressDialog.setMessage("Journey begins in a Moment :)");
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                String userKey = firebaseAuth.getCurrentUser().getUid();
                                String deviceId = FirebaseInstanceId.getInstance().getToken();
                                DatabaseReference UserBase = databaseReference.child("Users").child(userKey);
                                UserBase.child("user_name").setValue(Name);
                                UserBase.child("user_email").setValue(email);
                                UserBase.child("device_token").setValue(deviceId);
                                UserBase.child("user_status").setValue("Hey there, Whats going on :)");
                                UserBase.child("user_image").setValue(defalutPic);
                                UserBase.child("user_thumb_image").setValue(defaultThumb)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        });
                            }else {
                                tost(view ,"Please try again :)");
                            }
                            progressDialog.dismiss();
                        }
                    });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = (Toolbar) findViewById(R.id.regpage_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(this);

        usernameEdit = (EditText) findViewById(R.id.loginEmail);
        passwordEdit = (EditText) findViewById(R.id.regpassword);
        DisplayNameEdit = (EditText) findViewById(R.id.regName);

        firebaseAuth = FirebaseAuth.getInstance();

    }

}
