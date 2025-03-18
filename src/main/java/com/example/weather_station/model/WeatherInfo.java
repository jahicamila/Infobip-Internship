package com.example.weather_station.model;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
public class WeatherInfo implements Serializable {
    @NonNull
    private String cityName;
    @NonNull
    private String weatherCondition;
    @NonNull
    private LocalDateTime localtime;
}
