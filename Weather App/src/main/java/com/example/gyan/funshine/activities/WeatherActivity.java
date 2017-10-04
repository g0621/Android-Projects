package com.example.gyan.funshine.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.gyan.funshine.R;
import com.example.gyan.funshine.models.DailyWeather;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeatherActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    private final String URLbase = "http://api.openweathermap.org/data/2.5/forecast?appid=YOURAPPID&units=metric";
    private ImageView weatherIcon;
    private ImageView topWeatherIcon;
    private TextView weatherDate;
    private TextView currentTemp;
    private TextView minTemp;
    private TextView cityCountry;
    private TextView weatherDescription;
    private RecyclerView recyclerView;
    private GoogleApiClient googleApiClient;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1 :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.i("Maps","permission started location services");
                    startLocationServices();
                }else {
                    Toast.makeText(getApplicationContext(),"cannot proceed without permission",Toast.LENGTH_SHORT).show();
                }
        }
    }

    weatherAdapetr adapetr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        recyclerView = (RecyclerView) findViewById(R.id.content_weather_recycler);
        adapetr = new weatherAdapetr(arrayList);  //the downloaded content

        recyclerView.setAdapter(adapetr);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .enableAutoManage(this,this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        weatherIcon = (ImageView) findViewById(R.id.weather_icon);
        topWeatherIcon = (ImageView)findViewById(R.id.weather_icon_mini);
        weatherDate = (TextView) findViewById(R.id.formated_date);
        currentTemp = (TextView) findViewById(R.id.currentTemp);
        minTemp = (TextView) findViewById(R.id.minTemo);
        cityCountry = (TextView) findViewById(R.id.city_country);
        weatherDescription = (TextView) findViewById(R.id.weatherType);

    }
    private ArrayList<DailyWeather> arrayList = new ArrayList<>();

    public void downloadWeatherData(Location location){
        final String url = URLbase + "&lat=" + location.getLatitude() + "&lon=" + location.getLongitude();
        final JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET,url,null,new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject city = response.getJSONObject("city");
                    String cityName = city.getString("name");
                    String country = city.getString("country");
                    JSONArray list = response.getJSONArray("list");
                    int count = response.getInt("cnt");
                    arrayList.clear();

                    for (int i = 0 ; i < count ; i++){
                        JSONObject object = list.getJSONObject(i);
                        JSONObject main = object.getJSONObject("main");
                        Double currentTemp = main.getDouble("temp");
                        Double maxTemp = main.getDouble("temp_max");
                        Double minTemp = main.getDouble("temp_min");

                        JSONArray weatherArray = object.getJSONArray("weather");
                        JSONObject weather = weatherArray.getJSONObject(0);
                        String weth = weather.getString("main");
                        String date = object.getString("dt_txt");

                        DailyWeather weather1 = new DailyWeather(cityName,country,maxTemp.intValue(),minTemp.intValue(),currentTemp.intValue(),weth,date);
                        arrayList.add(weather1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                updateUI();
                adapetr.notifyDataSetChanged();
            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"service down !!",Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(objectRequest);
    }

    public void updateUI(){
        if (arrayList.size() > 0){
            DailyWeather dailyWeather = arrayList.get(0);
            switch (dailyWeather.getWeather()){
                case "Clouds":
                    weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.cloudy));
                    topWeatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.cloudy));
                    break;
                case "Rain":
                    weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.rainy));
                    topWeatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.rainy));
                    break;
                case "Wind":
                    weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.partially_cloudy));
                    topWeatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.partially_cloudy));
                    break;
                case "Snow":
                    weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.snow));
                    topWeatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.snow));
                    break;
                default:
                    weatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.sunny));
                    topWeatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.sunny));
                    break;
            }
            weatherDescription.setText(dailyWeather.getWeather());
            currentTemp.setText(String.valueOf(dailyWeather.getCurrent_temp()) + "°");
            minTemp.setText(String.valueOf(dailyWeather.getMin_temp()) + "°");
            cityCountry.setText(dailyWeather.getCityname() + ", "+ dailyWeather.getCountry());
            weatherDate.setText(dailyWeather.getFormatter_date());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        downloadWeatherData(location);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else {
            startLocationServices();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void startLocationServices(){
        try {
            LocationRequest request = LocationRequest.create().setPriority(LocationRequest.PRIORITY_LOW_POWER);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, request,this);
        }catch (SecurityException e){
            Toast.makeText(getApplicationContext(),"unable to get location at this time!!",Toast.LENGTH_SHORT).show();
        }
    }

    public class weatherAdapetr extends RecyclerView.Adapter<WeatherViewHolder>{

        private ArrayList<DailyWeather> weatherArrayList;

        public weatherAdapetr(ArrayList<DailyWeather> weatherArrayList) {
            this.weatherArrayList = weatherArrayList;
        }

        @Override
        public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards,parent,false);
            return new WeatherViewHolder(card);
        }

        @Override
        public void onBindViewHolder(WeatherViewHolder holder, int position) {

            DailyWeather weather = weatherArrayList.get(position);
            holder.updateUI(weather);

        }

        @Override
        public int getItemCount() {
            return weatherArrayList.size();
        }
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder{

        private ImageView lweatherIcon;
        private TextView lweatherDate;
        private TextView lweatherDescription;
        private TextView ltempHigh;
        private TextView ltempLow;

        public WeatherViewHolder(View itemView) {
            super(itemView);

            lweatherDate = (TextView) itemView.findViewById(R.id.weatherDate);
            lweatherIcon = (ImageView) itemView.findViewById(R.id.weatherIcon);
            lweatherDescription = (TextView) itemView.findViewById(R.id.weatherDiscription);
            ltempHigh = (TextView) itemView.findViewById(R.id.tempMaxMum);
            ltempLow = (TextView) itemView.findViewById(R.id.tempMinMum);
        }

        public void updateUI(DailyWeather dailyWeather){
            lweatherDate.setText(dailyWeather.getFormatter_date());
            lweatherDescription.setText(dailyWeather.getWeather());
            ltempHigh.setText(String.valueOf(dailyWeather.getMax_temp()) + "°");
            ltempLow.setText(String.valueOf(dailyWeather.getMin_temp()) + "°");
            switch (dailyWeather.getWeather()){
                case "Clouds":
                    lweatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.cloudy_mini));
                    break;
                case "Rain":
                    lweatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.rainy_mini));
                    break;
                case "Wind":
                    lweatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.partially_cloudy_mini));
                    break;
                case "Snow":
                    lweatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.snow_mini));
                    break;
                default:
                    lweatherIcon.setImageDrawable(getResources().getDrawable(R.drawable.sunny_mini));
                    break;
            }
        }
    }
}
