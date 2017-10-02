package com.example.gyan.intouch.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.gyan.intouch.Adapters.TabPagerAdapter;
import com.example.gyan.intouch.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private Toolbar mToolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabPagerAdapter tabPagerAdapter;
    public static boolean goingToOtherActivity = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case R.id.account_setting:
                goingToOtherActivity = true;
                startActivity(new Intent(getApplicationContext(),AccountSettingActivity.class));
                return true;
            case R.id.logout:
                if (user != null){
                    reference.child("online").setValue(ServerValue.TIMESTAMP);
                    reference.child("device_token").setValue(null);
                }
                firebaseAuth.signOut();
                logoutUser();
                return true;
            case R.id.allUserMenu:
                goingToOtherActivity = true;
                startActivity(new Intent(getApplicationContext(), AllUsersActivity.class));
                return true;
            default:
                return true;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if (user != null){
            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuth.getCurrentUser().getUid());
        }
        mToolbar = (Toolbar) findViewById(R.id.mainpage_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("In-Touch");

        tabLayout = (TabLayout) findViewById(R.id.main_tabs);
        viewPager = (ViewPager) findViewById(R.id.main_view_pager);
        tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void logoutUser(){
        Intent intent = new Intent(getApplicationContext(),startPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            logoutUser();
        }else {
            reference.child("online").setValue("true");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        goingToOtherActivity = false;
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (!goingToOtherActivity){
            if (user != null){
                reference.child("online").setValue(ServerValue.TIMESTAMP);
            }
        }
    }

}
