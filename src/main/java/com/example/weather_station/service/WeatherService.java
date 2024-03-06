package com.example.weather_station.service;

import com.example.weather_station.model.WeatherInfo;
import com.example.weather_station.model.Weather;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.api.ApisApi;
import io.swagger.client.auth.ApiKeyAuth;
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

    private static final Logger logger1 = LoggerFactory.getLogger("error");
    private static final Logger logger2 = LoggerFactory.getLogger("info");

    @Value("${weather.filepath}")
    private String filepath;

    @Value("${weather.api.uri}")
    private String weatherApiURI;

    @Value("${weather.api.key}")
    private String weatherApiKey;

    private final List<WeatherInfo> weatherInfoList = new ArrayList<>();


    public WeatherInfo fetchWeatherData()  throws IOException, InterruptedException{
        WeatherInfo weatherInfo = null;
        try {
            ApiClient apiClient = new ApiClient();
            apiClient.setBasePath(weatherApiURI);
            ApiKeyAuth ApiKeyAuth = (ApiKeyAuth) apiClient.getAuthentication("ApiKeyAuth");
            ApiKeyAuth.setApiKey(weatherApiKey);
//            ApiKeyAuth.setApiKeyPrefix("Token");

            ApisApi apiInstance = new ApisApi(apiClient);
            String q = "Sarajevo";
            String lang = "en";

            Weather weather = (Weather) apiInstance.realtimeWeather(q, lang);

            //logger2.info("API Response: {}", weatherResponse.body());

            String cityName = weather.getLocation().getName();
            String weatherCondition = weather.getCurrent().getCondition().getText();
            LocalDateTime currentTime = LocalDateTime.now();

            weatherInfo = new WeatherInfo(cityName, weatherCondition, currentTime);

        } catch (ApiException e) {
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
                for (Object obj : (ArrayList<?>) object) {
                    if (obj instanceof WeatherInfo) {
                        weathers.add((WeatherInfo) obj);
                    }
                }
            }else{
                logger1.error("Error: Object read from file is not an ArrayList<WeatherInfo>");
            }
        } catch (IOException | ClassNotFoundException e) {
            logger1.error("Error when reading weather data from the file: " + e.getMessage());
        }
        return weathers;
    }
}
