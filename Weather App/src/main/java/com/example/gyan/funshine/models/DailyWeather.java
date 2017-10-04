package com.example.gyan.funshine.models;

/**
 * Created by Gyan on 9/27/2017.
 */

public class DailyWeather {
    private String cityname;
    private String country;
    private int max_temp;
    private int min_temp;
    private int current_temp;
    private String weather;
    private String formatter_date;

    public DailyWeather(String cityname, String country, int max_temp, int min_temp, int current_temp, String weather, String formatter_date) {
        this.cityname = cityname;
        this.country = country;
        this.max_temp = max_temp;
        this.min_temp = min_temp;
        this.current_temp = current_temp;
        this.weather = weather;
        this.formatter_date = preetydate(formatter_date);
    }

    @Override
    public String toString() {
        return "DailyWeather{" +
                "cityname='" + cityname + '\'' +
                ", country='" + country + '\'' +
                ", max_temp=" + max_temp +
                ", min_temp=" + min_temp +
                ", current_temp=" + current_temp +
                ", weather='" + weather + '\'' +
                ", formatter_date='" + formatter_date + '\'' +
                '}';
    }

    public String preetydate(String raw_date){
        return raw_date.split(" ")[0];
    }

    public String getCityname() {
        return cityname;
    }

    public String getCountry() {
        return country;
    }

    public int getMax_temp() {
        return max_temp;
    }

    public int getMin_temp() {
        return min_temp;
    }

    public int getCurrent_temp() {
        return current_temp;
    }

    public String getWeather() {
        return weather;
    }

    public String getFormatter_date() {
        return formatter_date;
    }
}
