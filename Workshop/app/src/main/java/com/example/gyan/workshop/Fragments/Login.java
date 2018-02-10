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
import com.example.gyan.workshop.Models.User;
import com.example.gyan.workshop.R;
import com.example.gyan.workshop.Utils.WorkshopDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class Login extends Fragment {

    //Variables deceleration
    private EditText emailEditText;
    private EditText passEditText;
    private ProgressDialog progressDialog;
    private WorkshopDatabase workshopDatabase;

    public Login() {
        // Required empty public constructor
    }

    //Function to show Snackbar
    private void alert(View view, String s) {
        Snackbar.make(view, s, Snackbar.LENGTH_SHORT).show();
    }

    //Function to check the login criteria
    private void checkAndLogin(final View view) {
        String email = emailEditText.getText().toString();
        String password = passEditText.getText().toString();

        //if any of the field is empty shows a snackbar
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            alert(view, "Both field required !!");
        } else {
            //starts login process
            progressDialog.setTitle("Signing in");
            progressDialog.setMessage("just a moment");
            progressDialog.show();

            //Verify login credentials from database
            User user = workshopDatabase.loginWithEmail(email, password);
            if (user != null) {
                progressDialog.dismiss();
                alert(view, "Welcome back " + user.name);

                //Sets name of navigation drawer
                MainActivity.userNameTextView.setText(user.name);
                MainActivity.userEmailTextView.setText(user.email);

                //sets shared preferences to check current user status
                getActivity().getSharedPreferences("cur_user", Context.MODE_PRIVATE)
                        .edit()
                        .putBoolean("is_logged_in", true)
                        .putInt("user_id", user.id)
                        .putString("user_name", user.name)
                        .apply();

                //changes navigation drawer menu items
                MainActivity.navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
                MainActivity.navigationView.getMenu().findItem(R.id.nav_signup).setVisible(false);
                MainActivity.navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);

                //Redirects to students Dashboard
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frag_holder, new Dashboard())
                        .commit();
            } else {

                //means no such combination of email password exists
                progressDialog.dismiss();
                alert(view, "Email Password mismatch");
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);

        //changes activity Title
        getActivity().setTitle("Login");
        emailEditText = (EditText) view.findViewById(R.id.login_email);
        passEditText = (EditText) view.findViewById(R.id.login_password);
        Button loginButton = (Button) view.findViewById(R.id.login_button);
        progressDialog = new ProgressDialog(getActivity());

        //sets onClickListner to login Button and call login method.
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndLogin(v);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //get instance of database
        workshopDatabase = new WorkshopDatabase(getActivity());
    }
}
