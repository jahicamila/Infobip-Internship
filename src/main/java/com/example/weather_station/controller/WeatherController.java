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

    private static final Logger infoLogger = LoggerFactory.getLogger("info");

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public List<WeatherInfo> getWeatherData() {
        infoLogger.info("/GET/weather");
        List<WeatherInfo> allWeatherData = weatherService.readWeatherDataFromFile();
        return (allWeatherData.size() > 2) ? allWeatherData.subList(allWeatherData.size() - 2, allWeatherData.size()) : allWeatherData;
    }
}