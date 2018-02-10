package com.example.gyan.workshop.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.gyan.workshop.Activities.MainActivity;
import com.example.gyan.workshop.R;
import com.example.gyan.workshop.Utils.WorkshopDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class Signup extends Fragment {

    private EditText emailEditText;
    private EditText passEditText;
    private EditText nameEditText;
    private ProgressDialog progressDialog;
    private WorkshopDatabase workshopDatabase;

    public Signup() {
        // Required empty public constructor
    }

    //FUNCTION TO SHOW SNACKBAR
    private void alert(View v, String s) {
        Snackbar.make(v, s, Snackbar.LENGTH_SHORT).show();
    }

    //FUNCTION TO VERIFY CREDENTIALS AND SIGNUP NEW USER
    private void checkAndSignup(View view) {
        String email = emailEditText.getText().toString();
        String password = passEditText.getText().toString();
        String name = nameEditText.getText().toString();
        if (TextUtils.isEmpty(email)) {
            alert(view, "Email cannot be empty !!");
        } else if (TextUtils.isEmpty(password)) {
            alert(view, "Password cannot be empty !!");
        } else if (TextUtils.isEmpty(name)) {
            alert(view, "Name cannot be Empty");
        } else {

            //CHECKS IF EMAIL IS ALREADY REGISTERED
            if (workshopDatabase.checkIfEmailExists(email)) {
                alert(view, "Email already registered");
            } else {

                //IF NOT THEN REGISTER NEW USER
                int id = workshopDatabase.addUser(name, password, email);
                alert(view, "Welcome " + name);

                //CHANGES THE NAVIGATION DRAWER USERNAME
                MainActivity.userNameTextView.setText(name);
                MainActivity.userEmailTextView.setText(email);

                //SETS SHARED PREFERENCES
                getActivity().getSharedPreferences("cur_user", Context.MODE_PRIVATE)
                        .edit()
                        .putBoolean("is_logged_in", true)
                        .putInt("user_id", id)
                        .putString("user_name", name)
                        .apply();

                //CHANGES THE NAVIGATION DRAWER MENU ITEM
                MainActivity.navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
                MainActivity.navigationView.getMenu().findItem(R.id.nav_signup).setVisible(false);
                MainActivity.navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);

                //REDIRECTS TO ALL WORKSHOPS FRAGMENT
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frag_holder, new Available_workshops())
                        .commit();
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        emailEditText = (EditText) view.findViewById(R.id.signup_email);
        passEditText = (EditText) view.findViewById(R.id.signup_password);
        nameEditText = (EditText) view.findViewById(R.id.signup_name);
        Button signupButton = (Button) view.findViewById(R.id.signup_button);

        //Sets activity title
        getActivity().setTitle("Signup");
        progressDialog = new ProgressDialog(getActivity());

        //sets onclickListner to signup button redirects to signup function.
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndSignup(v);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //gets instance of database
        workshopDatabase = new WorkshopDatabase(getActivity());
    }
}
