package com.example.weather_station.service;

import com.example.weather_station.model.MyException;
import com.example.weather_station.model.WeatherInfo;
import com.example.weather_station.model.Weather;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.api.ApisApi;
import io.swagger.client.auth.ApiKeyAuth;
import io.swagger.client.model.Current;
import io.swagger.client.model.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDateTime;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private static final Logger errorLogger = LoggerFactory.getLogger("error");

    private ObjectMapper objectMapper;

    public WeatherService(ObjectMapper mapper) {
        objectMapper = mapper;
    }

    @Value("${weather.filepath}")
    private String filepath;

    @Value("${weather.api.uri}")
    private String weatherApiURI;

    @Value("${weather.api.key}")
    private String weatherApiKey;


    public WeatherInfo fetchWeatherDataFromAPI() throws IOException, InterruptedException, MyException {
        WeatherInfo weatherInfo = null;
        try {
            ApiClient apiClient = new ApiClient();
            ApiKeyAuth apiKeyAuth = (ApiKeyAuth) apiClient.getAuthentication("ApiKeyAuth");
            apiKeyAuth.setApiKey(weatherApiKey);
//            apiKeyAuth.setApiKeyPrefix("Token");

            ApisApi apiInstance = new ApisApi(apiClient);
            String q = "Sarajevo";
            String lang = "en";

            Object object = apiInstance.realtimeWeather(q, lang);

            Weather weatherResponse = objectMapper.readValue(objectMapper.writeValueAsString(object), Weather.class);

            Location location = weatherResponse.getLocation();
            Current weatherCondition = weatherResponse.getCurrent();
            LocalDateTime localtime = LocalDateTime.now();

            weatherInfo = new WeatherInfo(location, weatherCondition, localtime);

        } catch (ApiException e) {
            throw new MyException("Exception details");
        }
        return weatherInfo;
    }

    public void writeWeatherDataToFile(WeatherInfo weatherInfo) {
        List<WeatherInfo> weatherInfoList = readWeatherDataFromFile();
        weatherInfoList.add(weatherInfo);
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filepath))) {
            outputStream.writeObject(weatherInfoList);
        } catch (IOException e) {
            errorLogger.error("Error when writing weather data to the file: " + e.getMessage());
        }
    }

    public List<WeatherInfo> readWeatherDataFromFile() {
        List<WeatherInfo> weathers = new ArrayList<>();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filepath))) {
//            weathers = (List<WeatherInfo>) inputStream.readObject();
            Object object = inputStream.readObject();
            if (object instanceof ArrayList<?>) {
                for (Object obj : (ArrayList<?>) object) {
                    if (obj instanceof WeatherInfo) {
                        weathers.add((WeatherInfo) obj);
                    }
                }
            } else {
                errorLogger.error("Error: Object read from file is not an ArrayList<WeatherInfo>");
            }
        } catch (IOException | ClassNotFoundException e) {
            errorLogger.error("Error when reading weather data from the file: " + e.getMessage());
        }
        return weathers;
    }
}
