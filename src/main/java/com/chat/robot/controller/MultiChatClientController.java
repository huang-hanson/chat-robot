package com.chat.robot.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class MultiChatClientController {

    private final ChatClient deepSeekChatClient;
    private final ChatClient zhiPuAiChatClient;

    @Autowired
    public MultiChatClientController(@Qualifier("deepSeekChatClient") ChatClient deepSeekChatClient,
                                     @Qualifier("zhiPuAiChatClient") ChatClient zhiPuAiChatClient) {
        this.deepSeekChatClient = deepSeekChatClient;
        this.zhiPuAiChatClient = zhiPuAiChatClient;
    }

    /**
     * 使用指定模型进行对话。
     *
     * @param model     模型名称："deepseek" 或 "zhipu"
     * @param userInput 用户输入的问题
     * @return 模型回复的结果
     */
    @GetMapping("/multi-ai")
    String chatWithMultiAi(
            @RequestParam(defaultValue = "deepseek") String model,
            @RequestParam String userInput) {
        ChatClient chatClient;

        switch (model.toLowerCase()) {
            case "deepseek":
                chatClient = deepSeekChatClient;
                break;
            case "zhipu":
                chatClient = zhiPuAiChatClient;
                break;
            default:
                return "不支持的模型类型，请使用 deepseek 或 zhipu";
        }
        return chatClient.prompt(userInput).call().content();
    }
}