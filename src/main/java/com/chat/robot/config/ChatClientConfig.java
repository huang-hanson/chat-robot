package com.chat.robot.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hanson.huang
 * @version V1.0
 * @ClassName ChatClientConfig
 * @Description 多 AI 模型配置
 * @date 2025/6/3 18:28
 **/
@Configuration
public class ChatClientConfig {

    /**
     * 创建并配置一个ChatClient实例
     * 该方法通过注入的DeepSeekChatModel对象初始化一个ChatClient
     * 主要作用是将聊天模型与客户端进行绑定，以便进行后续的聊天操作
     *
     * @param chatModel 聊天模型，包含了聊天所需的配置和参数
     * @return 返回配置好的ChatClient实例
     */
    @Bean
    public ChatClient deepSeekChatClient(DeepSeekChatModel chatModel) {
        return ChatClient.create(chatModel);
    }

    /**
     * 创建ChatClient实例的Bean定义
     * 该方法将ZhiPuAiChatModel转换为ChatClient实例，供Spring框架管理
     *
     * @param chatModel 聊天模型，包含了与聊天客户端相关的信息和配置
     * @return ChatClient实例，用于与AI聊天服务进行交互
     */
    @Bean
    public ChatClient zhiPuAiChatClient(ZhiPuAiChatModel chatModel) {
        return ChatClient.create(chatModel);
    }
}