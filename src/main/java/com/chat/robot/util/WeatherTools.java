package com.chat.robot.util;


import com.chat.robot.service.WeatherService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.function.Function;

@Configuration(proxyBeanMethods = false)
public class WeatherTools {

    WeatherService weatherService = new WeatherService();

    public static final String CURRENT_WEATHER_TOOL = "getWeather";

    @Bean(CURRENT_WEATHER_TOOL)
    @Description("获取当前位置的天气情况")
    public Function<WeatherService.WeatherRequest, WeatherService.WeatherResponse> currentWeather() {
        return weatherService;
    }

    @Bean("getTime")
    @Description("获取当前时间")
    Function<TimeRequest, TimeResponse> getTime() {
        return request -> {
            // 模拟数据：实际项目中可调用时间API
            String time = switch (request.city().toLowerCase()) {
                case "旧金山" -> LocalDateTime.now(ZoneId.of("America/Los_Angeles")).toString();
                case "东京" -> LocalDateTime.now(ZoneId.of("Asia/Tokyo")).toString();
                case "巴黎" -> LocalDateTime.now(ZoneId.of("Europe/Paris")).toString();
                default -> "未知";
            };
            return new TimeResponse(request.city(), time);
        };
    }

    public record TimeRequest(String city) {}
    public record TimeResponse(String city, String currentTime) {}
}