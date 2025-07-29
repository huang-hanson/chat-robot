package com.chat.robot.util;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeTools {

    @Tool(description = "获取当前的日期和时间")
    String getCurrentDateTime() {
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }

    @Tool(description = "设置一个闹钟")
    void setAlarm(@ToolParam(description = "时间，格式为 ISO 8601") String time) {
        LocalDateTime alarmTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME);
        System.out.println("设置闹钟：" + alarmTime);
    }

    static String getCurrentDateTimeWithoutTool() {
        return "2024-07-28T17:26:50.055860700+08:00[Asia/Shanghai]";
    }


    public static void main(String[] args) {
//        String currentDateTimeWithoutTool = getCurrentDateTimeWithoutTool();
//        System.out.println(currentDateTimeWithoutTool);
    }
}