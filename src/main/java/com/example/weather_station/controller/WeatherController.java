package com.example.weather_station.controller;

import com.example.weather_station.model.WeatherInfo;
import com.example.weather_station.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private static final Logger logger1 = LoggerFactory.getLogger("error");
    private static final Logger logger2 = LoggerFactory.getLogger("info");
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public List<WeatherInfo> getWeatherData() {
        logger2.info("/GET/weather");
        List<WeatherInfo> allWeatherData = weatherService.readWeatherData();
        if (allWeatherData.size() > 2) {
            return allWeatherData.subList(allWeatherData.size() - 2, allWeatherData.size());
        } else {
            return allWeatherData;
        }
    }

}