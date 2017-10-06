package com.example.gyan.radioapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gyan.radioapp.Activities.MainActivity;
import com.example.gyan.radioapp.R;
import com.example.gyan.radioapp.holder.StationViewHolder;
import com.example.gyan.radioapp.model.Station;

import java.util.ArrayList;

/**
 * Created by Gyan on 9/22/2017.
 */

public class StationAdapter extends RecyclerView.Adapter<StationViewHolder>{

    private ArrayList<Station> stations;

    public StationAdapter(ArrayList<Station> stations) {
        this.stations = stations;
    }

    @Override
    public StationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View StationCards = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_station,parent,false);
        return new StationViewHolder(StationCards);
    }

    @Override
    public void onBindViewHolder(StationViewHolder holder,final int position) {
        final Station station = stations.get(position);
        holder.updateUI(station);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getMainActivity().loadDetailScreen(station);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stations.size();
    }
}
