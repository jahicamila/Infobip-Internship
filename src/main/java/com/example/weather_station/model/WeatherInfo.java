package com.example.weather_station.model;

import io.swagger.client.model.Current;
import io.swagger.client.model.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.threeten.bp.LocalDateTime;

import java.io.Serializable;


@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class WeatherInfo implements Serializable {
    @NonNull
    private String cityName;
    @NonNull
    private String weatherCondition;
    @NonNull
    private LocalDateTime localtime;

}
