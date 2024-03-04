package com.example.weather_station.scheduled;

import com.example.weather_station.model.WeatherInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.example.weather_station.service.WeatherService.fetchWeatherData;
import static com.example.weather_station.service.WeatherService.writeWeatherData;

@Component
@EnableScheduling
public class ScheduledWeather {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledWeather.class);

    @Scheduled(fixedRate = 10000)
    public void updateWeatherData() {
        try {
            WeatherInfo weatherInfo = fetchWeatherData();
            writeWeatherData(weatherInfo);
        } catch (IOException | InterruptedException e) {
            logger.error("Error when updating weather data from API: " + e.getMessage());
        }
    }
}
