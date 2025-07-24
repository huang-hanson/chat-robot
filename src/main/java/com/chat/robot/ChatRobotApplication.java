package com.chat.robot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.chat.robot.mapper")
@SpringBootApplication
public class ChatRobotApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatRobotApplication.class, args);
    }

}
