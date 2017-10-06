package com.example.gyan.radioapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gyan.radioapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragments extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public MainFragments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragments.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragments newInstance(String param1, String param2) {
        MainFragments fragment = new MainFragments();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_fragments, container, false);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        StationFrag frag1 = StationFrag.newInstance(0);
        StationFrag frag2 = StationFrag.newInstance(1);
        StationFrag frag3 = StationFrag.newInstance(2);
        fragmentManager.beginTransaction().add(R.id.container_topRow,frag1).commit();
        fragmentManager.beginTransaction().add(R.id.container_midRow,frag2).commit();
        fragmentManager.beginTransaction().add(R.id.container_botRow,frag3).commit()      ;
        return view;
    }

}
