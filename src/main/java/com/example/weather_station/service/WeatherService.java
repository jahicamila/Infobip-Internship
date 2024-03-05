package com.example.weather_station.service;

import com.example.weather_station.model.WeatherInfo;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.weather_station.model.Weather;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private static final Logger logger1 = LoggerFactory.getLogger("error");
    private static final Logger logger2 = LoggerFactory.getLogger("info");

    @Value("${weather.filepath}")
    private String filepath;

    @Value("${weather.api.uri}")
    private String weatherApiURI;

    private final List<WeatherInfo> weatherInfoList = new ArrayList<>();


    public WeatherInfo fetchWeatherData() throws IOException, InterruptedException {
        WeatherInfo weatherInfo = null;
        try (HttpClient client = HttpClient.newHttpClient()) {

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(weatherApiURI))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            logger2.info("API Response: {}", response.body());

            ObjectMapper objectMapper = new ObjectMapper();
            Weather weather = objectMapper.readValue(response.body(), Weather.class);

            String cityName = weather.getLocation().getName();
            String weatherCondition = weather.getCurrent().getCondition().getText();
            LocalDateTime currentTime = LocalDateTime.now();

            weatherInfo = new WeatherInfo(cityName, weatherCondition, currentTime);

        } catch (IOException e) {
            logger1.error("Error when fetching weather data from API: " + e.getMessage());
        }
        return weatherInfo;
    }

    public void writeWeatherData(WeatherInfo weatherInfo) {
        weatherInfoList.add(weatherInfo);
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filepath))) {
            outputStream.writeObject(weatherInfoList);
        } catch (IOException e) {
            logger1.error("Error when writing weather data to the file: " + e.getMessage());
        }
    }

    public List<WeatherInfo> readWeatherData() {
        List<WeatherInfo> weathers = new ArrayList<>();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filepath))) {
//            weathers = (List<WeatherInfo>) inputStream.readObject();
            Object object = inputStream.readObject();
            if (object instanceof ArrayList<?>) {
                weathers = (ArrayList<WeatherInfo>) object;
            }
        } catch (IOException | ClassNotFoundException e) {
            logger1.error("Error when reading weather data from the file: " + e.getMessage());
        }
        return weathers;
    }
}
