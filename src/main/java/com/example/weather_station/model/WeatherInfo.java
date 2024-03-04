package com.example.weather_station.model;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

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

    public LocalDateTime getLocalTime(){
        return localtime;
    }
}
