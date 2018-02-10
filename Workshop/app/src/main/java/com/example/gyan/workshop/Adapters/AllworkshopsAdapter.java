package com.example.gyan.workshop.Adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gyan.workshop.Fragments.Login;
import com.example.gyan.workshop.Models.Workshop;
import com.example.gyan.workshop.R;
import com.example.gyan.workshop.Utils.WorkshopDatabase;

import java.util.ArrayList;

/**
 * Created by Gyan on 1/24/2018.
 */

//ADAPTER CLASS fro AllWorkshops Recycler View.
//    Helper Class "AllworkshopsViewHolder" used as ViewHolder for Adapter

public class AllworkshopsAdapter extends RecyclerView.Adapter<AllworkshopsAdapter.AllworkshopsViewHolder> {

    //Local variable Initialization
    ArrayList<Workshop> workshopArrayList;
    Context context;
    WorkshopDatabase workshopDatabase;

    //Constructor to initialize local variables
    public AllworkshopsAdapter(ArrayList<Workshop> workshopArrayList, Context context) {
        this.workshopArrayList = workshopArrayList;
        this.context = context;
        workshopDatabase = new WorkshopDatabase(context);
    }

    @Override
    public AllworkshopsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workshop_card, parent, false);
        return new AllworkshopsViewHolder(itemview);
    }

    /*
    * ViewHolder Handler to control
    * 1. User not logged in to login page
    * 2. Single workshop cannot be applied twice.
    * 3. Handle Apply Button*/
    @Override
    public void onBindViewHolder(final AllworkshopsViewHolder holder, int position) {
        //Fetch workshop from the arrayList
        final Workshop workshop = workshopArrayList.get(position);

        // Sets Description and Title
        holder.titleTextView.setText(workshop.name);
        holder.descriptionTextView.setText(workshop.descripction);

        //Fetch Current User Status from "SHARED PREFERENCE"
        boolean t = context.getSharedPreferences("cur_user", Context.MODE_PRIVATE)
                .getBoolean("is_logged_in", false);

        //If User Logged in
        if (t) {

            //Check if User already  Enrolled in Workshop
            //Fetch current User Id from the Shared Preferences
            final int id = context.getSharedPreferences("cur_user", Context.MODE_PRIVATE)
                    .getInt("user_id", -1);

            //If already Applied then do
            if (workshopDatabase.checkIfEnrolled(String.valueOf(id), String.valueOf(workshop.id))) {
                holder.applyNowButton.setText("Already Applied");
                holder.applyNowButton.setBackgroundColor(context.getResources().getColor(R.color.disabled));
                holder.applyNowButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(v, "Already Applied...", Snackbar.LENGTH_SHORT).show();
                    }
                });

                //Else Make it clickable only once
            } else {
                holder.applyNowButton.setText("Apply Now");
                holder.applyNowButton.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                holder.applyNowButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //REGISTER THE USER TO THIS WORKSHOPIN DATABASE
                        workshopDatabase.enrollInWorkshop(id, workshop.id);
                        Snackbar.make(v, "Applied for " + workshop.name, Snackbar.LENGTH_SHORT).show();
                        holder.applyNowButton.setText("Already Applied");
                        holder.applyNowButton.setBackgroundColor(context.getResources().getColor(R.color.disabled));
                        holder.applyNowButton.setClickable(false);
                    }
                });
            }

            //else if user is not logged in redirect to login page
        } else {
            holder.applyNowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "You must be logged in...", Snackbar.LENGTH_SHORT).show();
                    ((FragmentActivity) context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_frag_holder, new Login())
                            .commit();
                }
            });
        }
    }

    //Return item count to recyclerView
    @Override
    public int getItemCount() {
        return workshopArrayList.size();
    }


    //ViewHolder Helper Class
    class AllworkshopsViewHolder extends RecyclerView.ViewHolder {

        //Views deceleration on card
        TextView titleTextView;
        TextView descriptionTextView;
        Button applyNowButton;

        public AllworkshopsViewHolder(View itemView) {
            super(itemView);

            //Views Assignment from the Custom card(res/layout/workshop_card)
            titleTextView = (TextView) itemView.findViewById(R.id.workshop_title);
            descriptionTextView = (TextView) itemView.findViewById(R.id.workshop_desc);
            applyNowButton = (Button) itemView.findViewById(R.id.applynow_button);
        }
    }
}
