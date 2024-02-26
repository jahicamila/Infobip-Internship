package com.example.weather_station;

import lombok.Data;

import java.io.Serializable;

@Data
public class Condition implements Serializable {
    private String text;
    private String icon;
    private int code;
}
