package com.example.weather_station.service;

import com.example.weather_station.model.MyException;
import com.example.weather_station.model.Weather;
import com.example.weather_station.model.WeatherInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDateTime;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private static final Logger errorLogger = LoggerFactory.getLogger("error");

    private final ObjectMapper objectMapper;

    @Value("${weather.filepath}")
    private String filepath;
    @Value("${weather.api.uri}")
    private String weatherApiURI;
    @Value("${weather.api.key}")
    private String weatherApiKey;

    public WeatherService(ObjectMapper mapper) {
        objectMapper = mapper;
    }

    public WeatherInfo fetchWeatherDataFromAPI() throws IOException, InterruptedException, MyException {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create(weatherApiURI))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            Weather weather = objectMapper.readValue(response.body(), Weather.class);

            if (weather == null || weather.getLocation() == null || weather.getCurrent() == null ||
                    weather.getCurrent().getCondition() == null) {
                throw new MyException("Invalid weather data received");
            }

            return new WeatherInfo(
                    weather.getLocation().getName(),
                    weather.getCurrent().getCondition().getText(),
                    LocalDateTime.now());

        } catch (IOException e) {
            throw new MyException("IOException occurred while fetching weather data: " + e.getMessage());
        }
    }

    public void writeWeatherDataToFile(WeatherInfo weatherInfo) {
        List<WeatherInfo> weatherInfoList = readWeatherDataFromFile();
        weatherInfoList.add(weatherInfo);

        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filepath))) {
            outputStream.writeObject(weatherInfoList);
        } catch (IOException e) {
            errorLogger.error("Error writing weather data to file: {}", e.getMessage());
        }
    }

    public List<WeatherInfo> readWeatherDataFromFile() {
        List<WeatherInfo> weathers = new ArrayList<>();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filepath))) {
            Object object = inputStream.readObject();
            if (object instanceof ArrayList<?>) {
                for (Object obj : (ArrayList<?>) object) {
                    if (obj instanceof WeatherInfo) {
                        weathers.add((WeatherInfo) obj);
                    }
                }
            } else {
                errorLogger.error("Invalid data format in weather file");
            }
        } catch (IOException | ClassNotFoundException e) {
            errorLogger.error("Error reading weather data from file: {}", e.getMessage());
        }
        return weathers;
    }
}
