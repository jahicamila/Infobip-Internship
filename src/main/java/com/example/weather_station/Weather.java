package com.example.weather_station;

import lombok.Data;

@Data
public class Weather {
    private Location location;
    private Current current;
}
