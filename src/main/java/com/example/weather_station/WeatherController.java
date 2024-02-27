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

    @GetMapping
    public List<WeatherInfo> getWeatherData() {
        return readWeatherData();
    }
    @Scheduled(fixedRate = 3600000)
    public void updateWeatherData(){
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
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filepath, true))) {
            outputStream.writeObject(weatherInfo);
            System.out.println("Weather information has been written to the file: " + filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<WeatherInfo> readWeatherData() {
        List<WeatherInfo> weathers = new ArrayList<>();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filepath))) {
            LocalDateTime twoHoursAgo = LocalDateTime.now().minusHours(2);
            while (true) {
                try {
                    WeatherInfo weatherInfo = (WeatherInfo) inputStream.readObject();
                    if (weatherInfo.getLocalTime().isAfter(twoHoursAgo)) {
                        weathers.add(weatherInfo);
                    }
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return weathers;
    }
}





