package com.example.gyan.radioapp.model;

/**
 * Created by Gyan on 9/22/2017.
 */

public class Station {
    final String DRAWABLE = "drawable/";
    private String stationTitle;
    private String imageURI;

    public Station(String stationTitle, String imageURI) {
        this.stationTitle = stationTitle;
        this.imageURI = imageURI;
    }

    public String getStationTitle() {
        return stationTitle;
    }

    public String getImageURI() {
        return DRAWABLE + imageURI;
    }
}
