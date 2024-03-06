package com.example.weather_station.model;

import lombok.Data;
import org.threeten.bp.LocalDateTime;

import java.io.Serializable;


@Data
public class WeatherInfo implements Serializable {
    private String cityName;
    private String weatherCondition;
    private LocalDateTime localtime;

    public WeatherInfo(String cityName, String weatherCondition, LocalDateTime localtime) {
        this.cityName = cityName;
        this.weatherCondition = weatherCondition;
        this.localtime = localtime;
    }

}
