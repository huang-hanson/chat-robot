package com.chat.robot.controller;

import com.chat.robot.entity.bo.AiChatMemory;
import com.chat.robot.entity.bo.Logger;
import com.chat.robot.service.impl.ChatService;
import com.chat.robot.service.impl.ChatServiceBySerializer;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author hanson.huang
 * @version V1.0
 * @ClassName ChatMemoryController
 * @Description 聊天内存
 * @date 2025/7/23 10:47
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatMemoryController {

    @Resource
    private ChatService chatService;

    @Resource
    private ChatServiceBySerializer chatServiceBySerializer;

    @Resource(name = "zhiPuAiChatClient")
    private ChatClient chatClient;

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestParam String conversationId, @RequestParam String message) {
        return chatService.chat(conversationId, message);
    }

    @GetMapping("/history")
    public List<AiChatMemory> history(@RequestParam String conversationId) {
        return chatService.getHistory(conversationId);
    }

    @GetMapping(value = "/chat", produces = "text/html;charset=UTF-8")
    Flux<String> chat(@RequestParam("msg") String msg) {
        Prompt prompt = new Prompt(msg);
        return chatClient.prompt(prompt)
                .stream()
                .content();
    }

    @GetMapping(value = "/testMysqlChatMemory", produces = "text/html;charset=UTF-8")
    Flux<String> testMysqlChatMemory(@RequestParam String conversationId, @RequestParam String message) {
        return chatService.chat(conversationId, message);
    }

    @GetMapping(value = "/test/serializer", produces = "text/html;charset=UTF-8")
    Flux<String> testSerializer(@RequestParam String conversationId, @RequestParam String message) {
        return chatServiceBySerializer.chat(conversationId, message);
    }

    @GetMapping("/history/serializer")
    public List<Message> historyBySerializer(@RequestParam String conversationId) {
        return chatServiceBySerializer.getHistory(conversationId);
    }
}