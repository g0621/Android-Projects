package com.example.gyan.radioapp.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.gyan.radioapp.Fragments.DetailFrag;
import com.example.gyan.radioapp.Fragments.MainFragments;
import com.example.gyan.radioapp.R;
import com.example.gyan.radioapp.model.Station;

public class MainActivity extends AppCompatActivity{

    private static MainActivity mainActivity;

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

     private static void setMainActivity(MainActivity mainActivity) {
        MainActivity.mainActivity = mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity.setMainActivity(this);

        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_holder);

        if (fragment == null){
            fragment = MainFragments.newInstance("bla","ka");
            manager.beginTransaction().add(R.id.fragment_holder,fragment).commit();
        }
    }
    public void loadDetailScreen(Station selectedStation){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder,DetailFrag.newInstance(selectedStation)).addToBackStack(null).commit();
    }
}
