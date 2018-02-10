package com.example.gyan.workshop.Adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.gyan.workshop.Models.Workshop;
import com.example.gyan.workshop.R;
import com.example.gyan.workshop.Utils.WorkshopDatabase;

import java.util.ArrayList;

/**
 * Created by Gyan on 1/24/2018.
 */

//Adapter class for RecyclerView of DashBoard Fragment
//DashBoardViewHolder is the helper class of this adapter

public class DashBoardAdapter extends RecyclerView.Adapter<DashBoardAdapter.DashBoardViewHolder> {

    //Variable decelerations
    WorkshopDatabase workshopDatabase;
    ArrayList<Workshop> workshopArrayList;
    Context context;

    //Constructor
    public DashBoardAdapter(ArrayList<Workshop> workshopArrayList, Context context) {
        this.workshopArrayList = workshopArrayList;
        this.context = context;
        workshopDatabase = new WorkshopDatabase(context);
    }

    @Override
    public DashBoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workshop_card, parent, false);
        return new DashBoardViewHolder(itemview);
    }

    //As no functionality is assigned in given assignment so just a Snackabar
    @Override
    public void onBindViewHolder(DashBoardViewHolder holder, int position) {
        final Workshop workshop = workshopArrayList.get(position);
        holder.titleTextView.setText(workshop.name);
        holder.descriptionTextView.setText(workshop.descripction);
        holder.applyNowButton.setText("Continue..");
        holder.applyNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Feature Coming soon", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return workshopArrayList.size();
    }


    //ViewHolder Helper Class
    class DashBoardViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView descriptionTextView;
        Button applyNowButton;

        public DashBoardViewHolder(View itemView) {
            super(itemView);

            //View initialization from the card.
            titleTextView = (TextView) itemView.findViewById(R.id.workshop_title);
            descriptionTextView = (TextView) itemView.findViewById(R.id.workshop_desc);
            applyNowButton = (Button) itemView.findViewById(R.id.applynow_button);
        }
    }
}
