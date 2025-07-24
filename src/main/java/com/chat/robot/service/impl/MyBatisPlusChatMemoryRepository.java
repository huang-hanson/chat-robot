package com.chat.robot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chat.robot.entity.bo.AiChatMemory;
import com.chat.robot.mapper.AiChatMemoryMapper;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hanson.huang
 * @version V1.0
 * @ClassName MyBatisPlusChatMemory
 * @date 2025/7/24 11:23
 **/
@Primary
@Service
public class MyBatisPlusChatMemoryRepository implements ChatMemoryRepository {

    @Resource
    private AiChatMemoryMapper mapper;

    @Override
    public List<String> findConversationIds() {
        // 查询所有会话的ID
        LambdaQueryWrapper<AiChatMemory> lqw = new LambdaQueryWrapper<>();
        return mapper.selectList(lqw)
                .stream()
                .map(AiChatMemory::getConversationId)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findByConversationId(String conversationId) {
        LambdaQueryWrapper<AiChatMemory> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AiChatMemory::getConversationId, conversationId);
        lqw.orderByDesc(AiChatMemory::getCreateTime);
        List<AiChatMemory> aiChatMemories = mapper.selectList(lqw);

        List<Message> messages = new ArrayList<>();
        for (AiChatMemory aiChatMemory : aiChatMemories) {
            String type = aiChatMemory.getType();
            switch (type) {
                case "user" -> messages.add(new UserMessage(aiChatMemory.getContent()));
                case "assistant" -> messages.add(new AssistantMessage(aiChatMemory.getContent()));
                case "system" -> messages.add(new SystemMessage(aiChatMemory.getContent()));
                default -> throw new IllegalArgumentException("Unknown message type: " + type);
            }
        }
        return messages;
    }

    @Override
    public void saveAll(String conversationId, List<Message> messages) {
        List<AiChatMemory> list = new ArrayList<>();
        messages.stream().forEach(message -> {
            AiChatMemory aiChatMemory = new AiChatMemory();
            aiChatMemory.setConversationId(conversationId);
            aiChatMemory.setType(message.getMessageType().getValue());
            aiChatMemory.setContent(message.getText());
            list.add(aiChatMemory);
        });

        mapper.insertBatch(list);
    }

    @Override
    public void deleteByConversationId(String conversationId) {
        // 删除指定会话的所有消息
        LambdaQueryWrapper<AiChatMemory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AiChatMemory::getConversationId, conversationId);
        mapper.delete(wrapper);
    }

    public List<AiChatMemory> findAiChatMemoryList(String conversationId) {
        LambdaQueryWrapper<AiChatMemory> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AiChatMemory::getConversationId, conversationId);
        lqw.orderByDesc(AiChatMemory::getCreateTime);
        return mapper.selectList(lqw);
    }
}