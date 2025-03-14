package com.example.weather_station.model;

import io.swagger.client.model.Current;
import io.swagger.client.model.Location;
import lombok.Data;

import java.io.Serializable;

@Data
public class Weather implements Serializable {
    private Location location;
    private Current current;
}
