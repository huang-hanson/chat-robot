package com.chat.robot.service.impl;

import com.chat.robot.config.MessageSerializer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hanson.huang
 * @version V1.0
 * @ClassName RedisChatMemory
 * @date 2025/7/24 17:54
 **/
@Service
@Slf4j
public class RedisChatMemory implements ChatMemoryRepository {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    // 用于存储所有对话ID的键
    private static final String CONVERSATION_IDS_KEY = "ALL_CONVERSATION_IDS";

    // 用于JSON序列化/反序列化
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<String> findConversationIds() {
        // 从Redis的集合中获取所有对话ID
        Set<Object> members = redisTemplate.opsForSet().members(CONVERSATION_IDS_KEY);
        if (members == null || members.isEmpty()) {
            return Collections.emptyList();
        }
        return members.stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findByConversationId(String conversationId) {
        return getFromRedis(conversationId);
    }

    @Override
    public void saveAll(String conversationId, List<Message> messages) {
        if (messages.isEmpty()) {
            return;
        }

        List<Message> messageList = getFromRedis(conversationId);
        messageList.addAll(messages);

        // 保存消息列表
        setToRedis(conversationId, messageList);

        // 将对话ID添加到集合中（自动去重）
        redisTemplate.opsForSet().add(CONVERSATION_IDS_KEY, conversationId);
    }

    @Override
    public void deleteByConversationId(String conversationId) {
        // 删除对话消息
        redisTemplate.delete(conversationId);
        // 从集合中移除对话ID
        redisTemplate.opsForSet().remove(CONVERSATION_IDS_KEY, conversationId);
    }

    /**
     * 从Redis获取数据工具方法
     * @param conversationId
     * @return
     */
    private List<Message> getFromRedis(String conversationId) {
        Object obj = redisTemplate.opsForValue().get(conversationId);
        List<Message> messageList = new ArrayList<>();

        if (obj != null) {
            try {
                // 将obj转换为List<String>
                List<String> messageJsons = objectMapper.convertValue(obj, new TypeReference<List<String>>() {});

                // 逐个反序列化为Message对象
                for (String json : messageJsons) {
                    Message message = MessageSerializer.deserialize(json);
                    messageList.add(message);
                }
            } catch (IllegalArgumentException e) {
                log.error("Failed to convert Redis value to List<String>", e);
            }
        }

        return messageList;
    }


    /**
     * 将数据存入Redis工具方法
     * @param conversationId
     * @param messages
     */
    private void setToRedis(String conversationId,List<Message> messages){
        List<String> stringList = new ArrayList<>();
        for (Message message : messages) {
            String serialize = MessageSerializer.serialize(message);
            stringList.add(serialize);
        }
        redisTemplate.opsForValue().set(conversationId,stringList);
    }
}