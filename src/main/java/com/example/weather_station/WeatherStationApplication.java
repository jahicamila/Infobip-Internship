package com.example.weather_station;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@SpringBootApplication
public class WeatherStationApplication{

    public static void main (String[]args) throws IOException, InterruptedException {

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(URI.create("http://api.weatherapi.com/v1/current.json?key=79dcb7239b0b4d7daa8112246242202&q=Sarajevo"))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
            System.out.printf("Status %s \n", response.statusCode());
            System.out.printf("Response %s \n", response.body());

        ObjectMapper objectMapper = new ObjectMapper();
        Weather weather = objectMapper.readValue(response.body(), Weather.class);

        Location location = weather.getLocation();
        Current current = weather.getCurrent();

        System.out.println("City: " + location.getName());
        System.out.println("Weather: " + current.getCondition().getText());
        }
    }




