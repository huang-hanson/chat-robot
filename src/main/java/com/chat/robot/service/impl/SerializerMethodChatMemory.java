package com.chat.robot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chat.robot.config.MessageSerializer;
import com.chat.robot.entity.bo.AiChatMemory;
import com.chat.robot.entity.bo.Logger;
import com.chat.robot.mapper.LoggerMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hanson.huang
 * @version V1.0
 * @ClassName SerializerMethodChatMemory
 * @Description 序列化方式持久化
 * @date 2025/7/24 17:09
 **/
@Service
@Slf4j
public class SerializerMethodChatMemory implements ChatMemoryRepository {

    @Resource
    private LoggerMapper loggerMapper;

    @Override
    public List<String> findConversationIds() {
        QueryWrapper<Logger> wrapper = new QueryWrapper<>();
        List<Logger> loggerList = loggerMapper.selectList(wrapper);
        return loggerList.stream()
                .map(Logger::getId)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> findByConversationId(String conversationId) {
        Long userId = parseUserId(conversationId);
        QueryWrapper<Logger> wrapper = new QueryWrapper<>();
        wrapper.eq("id", conversationId)
                .eq("userId", userId) // 添加用户 ID 过滤
                .orderByDesc("time"); // 按时间倒序

        List<Logger> loggerList = loggerMapper.selectList(wrapper);

        List<Message> messages = new ArrayList<>();
        for (Logger logger : loggerList) {
            messages.add(MessageSerializer.deserialize(logger.getMessage()));
        }
        return messages;
    }

    /**
     * 添加多条数据到数据库中
     *
     * @param conversationId
     * @param messages
     */
    @Override
    public void saveAll(String conversationId, List<Message> messages) {
        Long userId = parseUserId(conversationId);
        List<Logger> loggerList = new ArrayList<>();
        for (Message message : messages) {
            Logger logger = new Logger();
            logger.setId(conversationId);
            logger.setUserId(userId);
            logger.setTime(LocalDateTime.now());
            logger.setMessage(MessageSerializer.serialize(message));
            loggerList.add(logger);
        }
        loggerMapper.insert(loggerList);
    }

    @Override
    public void deleteByConversationId(String conversationId) {
        Long userId = parseUserId(conversationId);
        QueryWrapper<Logger> loggerQueryWrapper = new QueryWrapper<>();
        loggerQueryWrapper.eq("id", conversationId);
        loggerMapper.deleteById(loggerQueryWrapper);
    }

    // 从 conversationId 解析用户 ID（格式：chat-{userId}）
    private long parseUserId(String conversationId) {
        String[] parts = conversationId.split("-");
        if (parts.length == 2 && "chat".equals(parts[0])) {
            return Long.parseLong(parts[1]);
        }
        throw new IllegalArgumentException("无效的 conversationId 格式: " + conversationId);
    }

    public List<Logger> findAiChatMemoryList(String conversationId) {
        LambdaQueryWrapper<Logger> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Logger::getUserId, parseUserId(conversationId));
        lqw.orderByDesc(Logger::getTime);
        return loggerMapper.selectList(lqw);
    }
}