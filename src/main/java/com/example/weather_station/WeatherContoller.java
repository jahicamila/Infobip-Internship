package com.example.weather_station;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import java.nio.file.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api.weatherapi.com/v1/current")
public class WeatherContoller {

    @GetMapping()
    public static void main(String[] args) throws IOException, InterruptedException {

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::updateWeatherData, 0, 1, TimeUnit.HOURS);


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

        System.out.println("City: " + weather.getLocation().getName());
        System.out.println("Weather: " + weather.getCurrent().getCondition().getText());

        String cityName = weather.getLocation().getName();
        String weatherCondition = weather.getCurrent().getCondition().getText();

//       WeatherInfo weatherInfo = new WeatherInfo(cityName, weatherCondition);
//       String jsonWeatherInfo = objectMapper.writeValueAsString(weatherInfo); //serijalizacija

        String filePath = "weather_info.txt";
        Path outputPath = Paths.get(filePath);

        if (!Files.exists(outputPath)) {
            Files.createFile(outputPath);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardOpenOption.APPEND)) {
            writer.write("City: " + cityName);
            writer.newLine();
            writer.write("Weather: " + weatherCondition);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Weather information has been written to the file: " + filePath);


//        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))){
//            outputStream.writeObject(cityName);
//            outputStream.writeObject(weatherCondition);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("Weather information has been written to the file: " + filePath);
    }
}

