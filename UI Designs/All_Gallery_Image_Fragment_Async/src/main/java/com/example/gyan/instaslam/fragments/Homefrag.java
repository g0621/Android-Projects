package com.example.gyan.instaslam.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gyan.instaslam.R;

public class Homefrag extends Fragment {


    public Homefrag() {
        // Required empty public constructor
    }

    public static Homefrag newInstance() {
        Homefrag fragment = new Homefrag();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homefrag, container, false);

        return view;
    }

}
