package com.example.gyan.radioapp.services;

import com.example.gyan.radioapp.model.Station;

import java.util.ArrayList;

/**
 * Created by Gyan on 9/23/2017.
 */

public class DataServices {
    private static final DataServices ourInstance = new DataServices();

    public static DataServices getInstance() {
        return ourInstance;
    }

    private DataServices() {
    }

    public ArrayList<Station> getFeaturedStations(){
        //do all the downloading stuff here

        ArrayList<Station> list = new ArrayList<>();
        list.add(new Station("Flight plan (Tunes for travel)","flightplanmusic"));
        list.add(new Station("Two-Wheeling (Bike Playlist)","bicyclemusic"));
        list.add(new Station("Kids Jam (music for children)","kidsmusic"));

        return list;
    }

    public ArrayList<Station> getRecentStations(){
        //do all the downloading stuff here

        ArrayList<Station> list = new ArrayList<>();
        list.add(new Station("Flight plan (Tunes for travel)","flightplanmusic"));
        list.add(new Station("Two-Wheeling (Bike Playlist)","bicyclemusic"));
        list.add(new Station("Kids Jam (music for children)","kidsmusic"));

        return list;
    }
    public ArrayList<Station> getPartyStations(){
        //do all the downloading stuff here

        ArrayList<Station> list = new ArrayList<>();
        list.add(new Station("Flight plan (Tunes for travel)","flightplanmusic"));
        list.add(new Station("Two-Wheeling (Bike Playlist)","bicyclemusic"));
        list.add(new Station("Kids Jam (music for children)","kidsmusic"));

        return list;
    }
}
