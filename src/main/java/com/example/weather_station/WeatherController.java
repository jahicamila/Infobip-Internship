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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
@EnableScheduling
public class WeatherController {

    private static final String filepath = "weather_info.ser";

    private final List<WeatherInfo> weatherInfoList = new ArrayList<>();

    @GetMapping
    public List<WeatherInfo> getWeatherData() {
        List<WeatherInfo> allWeatherData = readWeatherData();
        if (allWeatherData.size() > 2) {
            return allWeatherData.subList(allWeatherData.size() - 2, allWeatherData.size());
        } else {
            return allWeatherData;
        }
    }

    @Scheduled(fixedRate = 20000)
    public void updateWeatherData() {
        try {
            WeatherInfo weather = fetchWeatherData();
            writeWeatherData(weather);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private WeatherInfo fetchWeatherData() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

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

        return new WeatherInfo(cityName, weatherCondition, currentTime);
    }


    private void writeWeatherData(WeatherInfo weatherInfo) {
        weatherInfoList.add(weatherInfo);
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filepath))) {
            outputStream.writeObject(weatherInfoList);
            System.out.println("Weather information has been written to the file: " + filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<WeatherInfo> readWeatherData() {
        List<WeatherInfo> weathers = new ArrayList<>();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filepath))) {
                weathers = (List<WeatherInfo>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return weathers;
    }
}





