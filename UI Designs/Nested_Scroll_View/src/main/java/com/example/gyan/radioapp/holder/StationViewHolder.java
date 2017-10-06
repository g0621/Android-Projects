package com.example.gyan.radioapp.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gyan.radioapp.R;
import com.example.gyan.radioapp.model.Station;

/**
 * Created by Gyan on 9/22/2017.
 */

public class StationViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView textView;
    public StationViewHolder(View itemView) {
        super(itemView);

        this.imageView = (ImageView) itemView.findViewById(R.id.mainImage);
        this.textView = (TextView) itemView.findViewById(R.id.mainText);
    }

    public void updateUI(Station station){
        String uri = station.getImageURI();
        int resoure = imageView.getResources().getIdentifier(uri,null,imageView.getContext().getPackageName());
        imageView.setImageResource(resoure);

        textView.setText(station.getStationTitle());
    }
}
