package com.example.weather_station;

import lombok.Data;

@Data
public class Condition {
    private String text;
    private String icon;
    private int code;
}
