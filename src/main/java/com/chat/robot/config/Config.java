package com.chat.robot.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hanson.huang
 * @version V1.0
 * @ClassName Config
 * @Description TODO
 * @date 2025/6/3 20:14
 **/
@Configuration
public class Config {
    @Bean
    ChatClient defaultTextChatClient(@Qualifier("deepSeekChatModel") ChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("你是一个友好的聊天机器人，用海盗的声音回答问题")
                .build();
    }

    @Bean
    ChatClient withParamTextChatClient(@Qualifier("deepSeekChatModel") ChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("你是一个友好的聊天机器人，用 {voice} 的声音回答问题")
                .build();
    }
}