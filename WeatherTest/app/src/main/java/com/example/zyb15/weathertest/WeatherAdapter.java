package com.example.zyb15.weathertest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by zyb15 on 2020/5/31.
 */

public class WeatherAdapter extends ArrayAdapter {

    private final int resourceId;

    public WeatherAdapter( Context context, int resource, List objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    @Override
    public View getView(int position, @Nullable View convertView, ViewGroup parent) {
        Weather weather = (Weather)getItem(position);
        @SuppressLint("ViewHolder") View view = LayoutInflater.from(getContext()).inflate(resourceId, null);//实例化一个对象
        TextView textView_date = (TextView)view.findViewById(R.id.textView_date);
        TextView textView_weather = (TextView)view.findViewById(R.id.textView_weather);
        TextView textView_temperature = (TextView)view.findViewById(R.id.textView_temperature);
        ImageView imageView_icon = (ImageView)view.findViewById(R.id.imageView_icon);
        assert weather != null;
        textView_date.setText(weather.getDate());
        textView_weather.setText(weather.getWeather());
        textView_temperature.setText(weather.getTemperature());
        imageView_icon.setImageResource(weather.getImg1());
        return view;
    }
}
