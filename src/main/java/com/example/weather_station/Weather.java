package com.example.weather_station;

import lombok.Data;

import java.io.Serializable;

@Data
public class Weather implements Serializable {
    private Location location;
    private Current current;
}
