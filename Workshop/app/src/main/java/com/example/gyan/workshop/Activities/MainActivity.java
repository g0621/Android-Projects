package com.example.gyan.workshop.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.gyan.workshop.Fragments.Available_workshops;
import com.example.gyan.workshop.Fragments.Dashboard;
import com.example.gyan.workshop.Fragments.Login;
import com.example.gyan.workshop.Fragments.Signup;
import com.example.gyan.workshop.R;

//MainActivity class all fragments will be placed here
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //These three need to be updated after user login from login fragment so declared static
    public static TextView userNameTextView;
    public static TextView userEmailTextView;
    public static NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //At start Available_workshops Fragment will be loaded
        this.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_frag_holder, new Available_workshops(), "workshop")
                .commit();

        //Drawer Initialization
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Navigation bar TextView initialization
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        userNameTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_user_name);
        userEmailTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_user_email);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        this.getSupportFragmentManager().popBackStack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    /*
    * We need to show logout and Dashboard
     * only to logged-in user
    * and sign-in and login only logout user
    * or to those who haven't yet signed up.*/

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean isLoggedIn = this.getSharedPreferences("cur_user", Context.MODE_PRIVATE).getBoolean("is_logged_in", false);
        if (isLoggedIn) {
            menu.findItem(R.id.menu_login).setVisible(false);
            menu.findItem(R.id.menu_signup).setVisible(false);
            menu.findItem(R.id.logout).setVisible(true);
            menu.findItem(R.id.menu_dashboard).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_signup).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
        } else {
            menu.findItem(R.id.logout).setVisible(false);
            menu.findItem(R.id.menu_dashboard).setVisible(false);
            menu.findItem(R.id.menu_login).setVisible(true);
            menu.findItem(R.id.menu_signup).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_signup).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /*Logout method to logout user
      1. Shared preference are changed to reflect current
         user Status and id
      2. Navigation Drawer logout option is set invisible
      3. As accessed from 2 places so function created
    * */
    public void logout() {
        MainActivity.userNameTextView.setText("Guest");
        MainActivity.userEmailTextView.setText("Guest@xyz.com");
        this.getSharedPreferences("cur_user", Context.MODE_PRIVATE)
                .edit()
                .putBoolean("is_logged_in", false)
                .putInt("user_id", -1)
                .putString("user_name", "Guest")
                .apply();
        navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
        navigationView.getMenu().findItem(R.id.nav_signup).setVisible(true);
        navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frag_holder, new Login(), "logout")
                .addToBackStack("logout")
                .commit();
    }

    //function to redirect to signup page
    public void signupRedirect() {
        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frag_holder, new Signup(), "singup")
                .addToBackStack("signup")
                .commit();
    }

    //function to redirect to login page
    public void loginRedirect() {
        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frag_holder, new Login(), "login")
                .addToBackStack("login")
                .commit();
    }

    //function to redirect to Dashboard
    public void dashBoardRedirect() {
        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frag_holder, new Dashboard(), "dashboard")
                .addToBackStack("dashboard")
                .commit();
    }

    //Options item click handler function
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
            logout();
        } else if (id == R.id.menu_signup) {
            signupRedirect();
        } else if (id == R.id.menu_login) {
            loginRedirect();
        } else if (id == R.id.menu_dashboard) {
            dashBoardRedirect();
        }
        return super.onOptionsItemSelected(item);
    }


    //Navigation menu click handler function
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_login) {
            loginRedirect();
        } else if (id == R.id.nav_logout) {
            logout();
        } else if (id == R.id.nav_signup) {
            signupRedirect();
        } else if (id == R.id.nav_allworkshops) {
            this.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_frag_holder, new Available_workshops(), "allworkshops")
                    .addToBackStack("allworkshops")
                    .commit();
        } else if (id == R.id.nav_dashboard) {
            boolean isLoggedIn = this.getSharedPreferences("cur_user", Context.MODE_PRIVATE).getBoolean("is_logged_in", false);
            if (isLoggedIn) dashBoardRedirect();
            else {
                loginRedirect();
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
