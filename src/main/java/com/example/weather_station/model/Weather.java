package com.example.weather_station.model;

import com.example.weather_station.model.Location;
import lombok.Data;
import com.example.weather_station.model.Current;

import java.io.Serializable;

@Data
public class Weather implements Serializable {
    private Location location;
    private Current current;
}
