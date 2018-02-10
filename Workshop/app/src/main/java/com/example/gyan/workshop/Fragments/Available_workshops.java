package com.example.gyan.workshop.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gyan.workshop.Adapters.AllworkshopsAdapter;
import com.example.gyan.workshop.Models.Workshop;
import com.example.gyan.workshop.R;
import com.example.gyan.workshop.Utils.WorkshopDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Available_workshops extends Fragment {

    //variable Decelerations
    WorkshopDatabase workshopDatabase;
    RecyclerView recyclerView;
    ArrayList<Workshop> workshopArrayList;
    public AllworkshopsAdapter allworkshopsAdapter;

    public Available_workshops() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Variable initializations
        View view = inflater.inflate(R.layout.fragment_available_workshops, container, false);
        getActivity().setTitle("All workshops");
        recyclerView = (RecyclerView) view.findViewById(R.id.main_recycler_holder);
        return view;
    }

    //Used to refresh recyclerview on resume
    @Override
    public void onResume() {
        super.onResume();
        allworkshopsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //GET INSTANCE OF DATABASE
        workshopDatabase = new WorkshopDatabase(getActivity());

        //ADDS 5 NEW DUMMY WORKSHOPS EVERY TIME IN DATABASE
        workshopDatabase.createDummyWorkshops();

        //RETRIEVE WORKSHOPS FROM DATABASE
        workshopArrayList = workshopDatabase.queryWorkshops();

        //Adapter initialization for recycler View
        allworkshopsAdapter = new AllworkshopsAdapter(workshopArrayList, getContext());

        //recyclerview initialization
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(allworkshopsAdapter);
        recyclerView.setHasFixedSize(true);
    }
}
