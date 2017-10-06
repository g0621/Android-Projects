package com.example.gyan.radioapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gyan.radioapp.R;
import com.example.gyan.radioapp.model.Station;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFrag extends Fragment {

    private String imageURI, title;
    public ImageView imageView;
    public TextView textView;

    public DetailFrag() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DetailFrag newInstance(Station station) {
        DetailFrag fragment = new DetailFrag();
        Bundle args = new Bundle();
        args.putString("imgURI", station.getImageURI());
        args.putString("title", station.getStationTitle());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageURI = getArguments().getString("imgURI");
            title = getArguments().getString("title");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        textView = (TextView) view.findViewById(R.id.textView1);
        imageView = (ImageView) view.findViewById(R.id.image1);
        textView.setText(title);
        int resoure = imageView.getResources().getIdentifier(imageURI,null,imageView.getContext().getPackageName());
        imageView.setImageResource(resoure);
        return view;
    }

}
