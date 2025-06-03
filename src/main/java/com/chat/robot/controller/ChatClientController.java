package com.chat.robot.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hanson.huang
 * @version V1.0
 * @ClassName ChatClientController
 * @Description Chat Client API
 * @date 2025/6/3 17:25
 **/
@RestController
public class ChatClientController {

//    private final ChatClient chatClient;
//
//    public ChatClientController(ChatClient.Builder chatClientBuilder) {
//        this.chatClient = chatClientBuilder.build();
//    }
//
//    @GetMapping("/ai")
//    String generation(@RequestParam("userInput") String userInput) {
//        return this.chatClient.prompt()
//                .user(userInput)
//                .call()
//                .content();
//    }
}