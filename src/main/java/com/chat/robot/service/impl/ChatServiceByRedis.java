package com.chat.robot.service.impl;

import com.chat.robot.entity.bo.AiChatMemory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hanson.huang
 * @version V1.0
 * @ClassName ChatService
 * @date 2025/7/23 14:00
 **/
@Slf4j
@Service
public class ChatServiceByRedis {

    @Resource(name = "zhiPuAiChatClient")
    private ChatClient chatClient;

    @Resource(name = "redisChatMemory")
    private RedisChatMemory redisChatMemory;

    private final Map<String, ChatMemory> memoryMap = new ConcurrentHashMap<>();

    public Flux<String> chat(String conversationId, String message) {
        ChatMemory chatMemory = this.getMemory(conversationId);
        // 添加用户消息
        UserMessage userMessage = new UserMessage(message);
        chatMemory.add(conversationId, userMessage);
        redisChatMemory.saveAll(conversationId, List.of(userMessage));

        // 构建包含上下文的Prompt
        List<Message> messages = chatMemory.get(conversationId);

        // 调用ChatClient的流式接口
        Flux<String> responseStream = chatClient.prompt(new Prompt(messages))
                .stream()
                .content();

        // 使用StringBuilder累积完整响应
        StringBuilder fullResponse = new StringBuilder();

        return responseStream
                // 每收到一个流片段就追加到StringBuilder
                .doOnNext(chunk -> fullResponse.append(chunk))
                // 当流完成时，异步保存完整响应
                .doOnComplete(() -> {
                    // 使用异步线程执行保存操作，避免阻塞流处理
                    Mono.fromRunnable(() -> {
                                String output = fullResponse.toString();
                                log.warn("AI Response: {}", output);
                                AssistantMessage assistantMessage = new AssistantMessage(output);
                                chatMemory.add(conversationId, assistantMessage);
                                redisChatMemory.saveAll(conversationId, List.of(assistantMessage));
                            })
                            .subscribeOn(Schedulers.boundedElastic())
                            .subscribe(); // 订阅以触发异步执行
                });
    }

    public List<Message> getHistory(String conversationId) {
        return redisChatMemory.findByConversationId(conversationId);
    }

    private ChatMemory getMemory(String conversationId) {
        return memoryMap.computeIfAbsent(conversationId, id -> MessageWindowChatMemory.builder().build());
    }
}