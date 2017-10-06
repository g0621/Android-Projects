package com.example.gyan.instaslam.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gyan.instaslam.R;


public class FeedActivities extends Fragment {

    public FeedActivities() {
        // Required empty public constructor
    }

    public static FeedActivities newInstance() {
        FeedActivities fragment = new FeedActivities();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_activities, container, false);

        return view;
    }

}
