package com.example.weather_station;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
@EnableScheduling
public class WeatherController {

    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);
    private static final String filepath = "weather_info.ser";

    private final List<WeatherInfo> weatherInfoList = new ArrayList<>();

    @GetMapping
    public List<WeatherInfo> getWeatherData() {
        logger.info("/GET/weather");
        List<WeatherInfo> allWeatherData = readWeatherData();
        if (allWeatherData.size() > 2) {
            return allWeatherData.subList(allWeatherData.size() - 2, allWeatherData.size());
        } else {
            return allWeatherData;
        }
    }

    @Scheduled(fixedRate = 10000)
    public void updateWeatherData() {
        try {
            fetchWeatherData();
        } catch (IOException | InterruptedException e) {
            logger.error("Error when updating weather data from API: " + e.getMessage());
        }
    }

    private void fetchWeatherData() throws IOException, InterruptedException {
        logger.debug("Fetching weather data from API...");

        try (HttpClient client = HttpClient.newHttpClient()) {

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create("http://api.weatherapi.com/v1/current.json?key=79dcb7239b0b4d7daa8112246242202&q=Sarajevo"))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            Weather weather = objectMapper.readValue(response.body(), Weather.class);


            String cityName = weather.getLocation().getName();
            String weatherCondition = weather.getCurrent().getCondition().getText();
            LocalDateTime currentTime = LocalDateTime.now();

            writeWeatherData(new WeatherInfo(cityName, weatherCondition, currentTime));

        } catch (IOException e) {
            logger.error("Error when fetching weather data from API: " + e.getMessage());
        }

    }

    private void writeWeatherData(WeatherInfo weatherInfo) {
        logger.debug("Writing weather data to a file...");

        weatherInfoList.add(weatherInfo);
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filepath))) {
            outputStream.writeObject(weatherInfoList);
            logger.debug("Weather information has been written to the file: " + filepath);
        } catch (IOException e) {
            logger.error("Error when writing weather data to the file: " + e.getMessage());
        }
    }

    private List<WeatherInfo> readWeatherData() {
        logger.debug("Reading weather data from the file...");

        List<WeatherInfo> weathers = new ArrayList<>();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filepath))) {
//            weathers = (List<WeatherInfo>) inputStream.readObject();
            Object object = inputStream.readObject();
            if(object instanceof ArrayList<?>){
                        weathers = (ArrayList<WeatherInfo>) object;
            }
            logger.debug("Weather data has been read from the file: " + filepath);
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Error when reading weather data from the file: " + e.getMessage());
        }
        return weathers;
    }
}