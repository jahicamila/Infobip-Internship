package com.example.weather_station;

import lombok.Data;

import java.io.Serializable;

@Data
public class Weather {
    private Location location;
    private Current current;
}
