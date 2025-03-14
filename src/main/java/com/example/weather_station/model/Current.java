package com.example.weather_station.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.example.weather_station.model.Condition;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Current implements Serializable {
    private Condition condition;
}
