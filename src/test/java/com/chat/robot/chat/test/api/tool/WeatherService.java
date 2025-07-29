package com.chat.robot.chat.test.api.tool;

import java.util.function.Function;

public class WeatherService implements Function<WeatherService.WeatherRequest, WeatherService.WeatherResponse> {

    public WeatherResponse apply(WeatherRequest request) {
        double temperature = 0;
        if (request.location().contains("巴黎")) {
            temperature = 15;
        } else if (request.location().contains("东京")) {
            temperature = 10;
        } else if (request.location().contains("旧金山")) {
            temperature = 30;
        }

        return new WeatherResponse(temperature, 15, 20, 2, 53, 45, Unit.C);
    }

    public enum Unit {C, F}

    public record WeatherRequest(String location, Unit unit) {
    }

    public record WeatherResponse(double temp, double feels_like, double temp_min, double temp_max, int pressure,
                                  int humidity,
                                  Unit unit) {

    }
}




