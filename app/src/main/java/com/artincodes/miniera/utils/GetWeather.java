package com.artincodes.miniera.utils;

import android.view.LayoutInflater;
import android.widget.TextView;
import com.artincodes.miniera.fragments.NowFragment;

/**
 * Created by jayadeep on 17/12/14.
 */
public class GetWeather {
    double latitude;
    double longitude;
    int temperature;
    String location;
    String URL;
    String API_KEY = "7263875678946287ab7659919ad6f";

    TextView temp;
    LayoutInflater inflater;
    TextView tempText;

    NowFragment nowFragment;

    public GetWeather() {


        //View customNav = LayoutInflater.from(NowFragment.this).inflate(R.layout.layout_toolbar, null); // layout which contains your button.
    }


    public void getLocation() {
        //NowFragment.temperatureText.setText("45");



        //return  location;
    }

    public int getTemperature() {

        return temperature;
    }
}
