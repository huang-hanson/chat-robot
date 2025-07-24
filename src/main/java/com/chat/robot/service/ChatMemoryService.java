package com.chat.robot.service;

import org.springframework.ai.chat.memory.ChatMemory;

/**
 * @author hanson.huang
 * @version V1.0
 * @InterfaceName ChatMemoryService
 * @date 2025/7/23 11:06
 **/
public interface ChatMemoryService {

    ChatMemory getMemory(String conversationId);
}
