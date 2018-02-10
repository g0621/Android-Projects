package com.example.gyan.workshop.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.gyan.workshop.Activities.MainActivity;
import com.example.gyan.workshop.Adapters.DashBoardAdapter;
import com.example.gyan.workshop.Models.Workshop;
import com.example.gyan.workshop.R;
import com.example.gyan.workshop.Utils.WorkshopDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard extends Fragment {

    TextView nameDashboard;
    TextView emailDashBoard;
    TextView notEnrolledInAny;
    RecyclerView dashboardRecycler;
    WorkshopDatabase workshopDatabase;
    DashBoardAdapter dashBoardAdapter;
    ArrayList<Workshop> workshopArrayList;

    public Dashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //sets the activity title
        getActivity().setTitle("DashBoard");

        //gets username and email from MainActivity
        String username = MainActivity.userNameTextView.getText().toString();
        String email = MainActivity.userEmailTextView.getText().toString();
        nameDashboard = view.findViewById(R.id.dashboard_name);
        emailDashBoard = view.findViewById(R.id.dashboard_email);
        notEnrolledInAny = view.findViewById(R.id.not_enrolled);

        //sets username and email of dashboard fragment
        nameDashboard.setText(username);
        emailDashBoard.setText(email);
        dashboardRecycler = view.findViewById(R.id.dashboard_recycler);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //gets database instance
        workshopDatabase = new WorkshopDatabase(getActivity());

        //gets current user user id from shared preferences
        int id = getActivity().getSharedPreferences("cur_user", Context.MODE_PRIVATE)
                .getInt("user_id", -1);

        //gets current enrolled workshops
        workshopArrayList = workshopDatabase.getCurrentWorkShop(String.valueOf(id));

        //initialize recyclerview
        dashBoardAdapter = new DashBoardAdapter(workshopArrayList, getContext());
        dashboardRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        dashboardRecycler.setItemAnimator(new DefaultItemAnimator());
        dashboardRecycler.setAdapter(dashBoardAdapter);
        dashboardRecycler.setHasFixedSize(true);

        //if there are no course enrolled then shows "No course enrolled yet"
        if (workshopArrayList.size() == 0) {
            notEnrolledInAny.setVisibility(View.VISIBLE);
            dashboardRecycler.setVisibility(View.INVISIBLE);

            //else shows the recyclerview
        } else {
            notEnrolledInAny.setVisibility(View.INVISIBLE);
            dashboardRecycler.setVisibility(View.VISIBLE);
        }
    }
}
