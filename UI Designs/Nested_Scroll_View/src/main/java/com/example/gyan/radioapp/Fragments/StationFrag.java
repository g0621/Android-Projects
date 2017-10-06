package com.example.gyan.radioapp.Fragments;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gyan.radioapp.Adapters.StationAdapter;
import com.example.gyan.radioapp.R;
import com.example.gyan.radioapp.services.DataServices;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StationFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StationFrag extends Fragment {

    private int station_type;

    public StationFrag() {
        // Required empty public constructor
    }

    public static StationFrag newInstance(int STATION_TYPE) {
        StationFrag fragment = new StationFrag();
        Bundle args = new Bundle();
        args.putInt("stationTyp",STATION_TYPE);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            station_type = getArguments().getInt("stationTyp");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_station, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);             //make it true if u know that the contents will have same size all the time. make list fast
        StationAdapter adapter;                         // 0 -> feature , 1-> recent , 2 -> party
        if (station_type == 0){
            adapter = new StationAdapter(DataServices.getInstance().getFeaturedStations());
        }else if (station_type == 1){
            adapter = new StationAdapter(DataServices.getInstance().getRecentStations());
        }else {
            adapter = new StationAdapter(DataServices.getInstance().getPartyStations());
        }
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new horizontalSpace(15));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        return view;
    }

}
class horizontalSpace extends RecyclerView.ItemDecoration {

    private final int spacer;

    public horizontalSpace(int spacer) {
        this.spacer = spacer;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.right = spacer;
    }
}
