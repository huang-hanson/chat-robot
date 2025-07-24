package com.chat.robot.service.impl;

import com.chat.robot.service.ChatMemoryService;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hanson.huang
 * @version V1.0
 * @ClassName ChatMemoryServiceImpl
 * @date 2025/7/23 11:06
 **/
@Service
public class ChatMemoryServiceImpl implements ChatMemoryService {

    private final Map<String, ChatMemory> memoryMap = new ConcurrentHashMap<>();

    @Override
    public ChatMemory getMemory(String conversationId) {
        return memoryMap.computeIfAbsent(conversationId, id -> MessageWindowChatMemory.builder().build());
    }
}
