package com.example.weather_station;

import jdk.jfr.DataAmount;
import lombok.Data;

import java.io.Serializable;

@Data
public class Location implements Serializable {
    private String name;
    private String region;
    private String country;
    private double lat;
    private double lon;
    private String tz_id;
    private long localtime_epoch;
    private String localtime;

}
