## Overview
This project is designed to fetch current weather data from weatherapi.com at regular intervals (every hour) and store the data in a file for later use. The application also provides a custom GET /weather endpoint to retrieve filtered weather data for the past two hours.

## Features
Periodic Data Fetching: Fetches weather data from [weatherapi.com](https://www.weatherapi.com/) every hour.

Data Storage: Stores the fetched weather data in a file for historical reference.

Custom API Endpoint: Provides a GET /weather endpoint to retrieve weather data for the past two hours.

## Technologies Used
Programming Language: Java

API: [weatherapi.com](https://www.weatherapi.com/)

Web Framework: Spring Boot

Data Storage: Local file storage (JSON)

## Usage
Fetching Weather Data:

The application automatically fetches weather data every hour and stores it in a file (weather_info.ser).

### Retrieving Filtered Data:

Use the GET /weather endpoint to retrieve weather data for the past two hours.

```
http://localhost:8080/weather
```

### Example Response
The GET /weather endpoint returns filtered weather data in the following format:

```
[
    {
        "cityName": "Sarajevo",
        "weatherCondition": "Partly cloudy",
        "localtime": {
            "nano": 496000000,
            "year": 2025,
            "monthValue": 3,
            "dayOfMonth": 17,
            "hour": 14,
            "minute": 54,
            "second": 56,
            "month": "MARCH",
            "dayOfWeek": "MONDAY",
            "dayOfYear": 76,
            "chronology": {
                "id": "ISO",
                "calendarType": "iso8601"
            }
        }
    },
    {
        "cityName": "Sarajevo",
        "weatherCondition": "Partly cloudy",
        "localtime": {
            "nano": 915000000,
            "year": 2025,
            "monthValue": 3,
            "dayOfMonth": 17,
            "hour": 14,
            "minute": 55,
            "second": 5,
            "month": "MARCH",
            "dayOfWeek": "MONDAY",
            "dayOfYear": 76,
            "chronology": {
                "id": "ISO",
                "calendarType": "iso8601"
            }
        }
    }
]
```
