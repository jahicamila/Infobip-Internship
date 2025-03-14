package com.example.weather_station.scheduled;

import com.example.weather_station.model.MyException;
import com.example.weather_station.model.WeatherInfo;
import com.example.weather_station.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@EnableScheduling
public class ScheduledWeather {

    private static final Logger errorLogger = LoggerFactory.getLogger("error");

    private final WeatherService weatherService;

    public ScheduledWeather(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Scheduled(fixedRate = 10000)
    public void updateWeatherData() {
        try {
            WeatherInfo weatherInfo = weatherService.fetchWeatherDataFromAPI();
            weatherService.writeWeatherDataToFile(weatherInfo);
        } catch (IOException e) {
            errorLogger.error("Error when updating weather data from API: " + e.getMessage());
        } catch (InterruptedException e) {
            errorLogger.error("Process execution was interrupted");
        }
        catch(MyException me){
            errorLogger.error(String.valueOf(me));
        }
    }
}
