package com.chat.robot.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

/**
 * @author hanson.huang
 * @version V1.0
 * @ClassName ChatClientExample
 * @Description TODO
 * @date 2025/6/3 18:31
 **/
//@Configuration
//public class ChatClientExample {
//
//    @Bean
//    CommandLineRunner cli(
//            @Qualifier("deepSeekChatClient") ChatClient deepSeekChatClient,
//            @Qualifier("zhiPuAiChatClient") ChatClient zhiPuAiChatClient) {
//
//        return args -> {
//            var scanner = new Scanner(System.in);
//            ChatClient chat;
//
//            // 模型选择
//            System.out.println("\n选择您的 AI 模型：");
//            System.out.println("1. DeepSeek");
//            System.out.println("2. ZhiPuAi");
//            System.out.print("输入您的选择（1 或 2）：");
//
//            String choice = scanner.nextLine().trim();
//
//            if (choice.equals("1")) {
//                chat = deepSeekChatClient;
//                System.out.println("使用 DeepSeek 模型");
//            } else {
//                chat = zhiPuAiChatClient;
//                System.out.println("使用 ZhiPuAi 模型");
//            }
//
//            // 使用选定的聊天客户端
//            System.out.print("\n输入您的问题：");
//            String input = scanner.nextLine();
//            String response = chat.prompt(input).call().content();
//            System.out.println("助手：" + response);
//
//            scanner.close();
//        };
//    }
//}