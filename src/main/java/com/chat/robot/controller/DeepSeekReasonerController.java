package com.chat.robot.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.ai.deepseek.api.DeepSeekApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.deepseek.DeepSeekAssistantMessage;

import java.util.HashMap;
import java.util.Map;


@RestController
public class DeepSeekReasonerController {

    @Resource
    private DeepSeekChatModel chatModel;

    @GetMapping("/ai/deepseek-reasoner")
    public Map<String, String> generate(@RequestParam(value = "message", defaultValue = "9.11 和 9.8，哪个更大？") String message) {
        DeepSeekChatOptions promptOptions = DeepSeekChatOptions.builder()
                .model(DeepSeekApi.ChatModel.DEEPSEEK_REASONER.getValue())
                .build();
        Prompt prompt = new Prompt(message, promptOptions);
        ChatResponse response = chatModel.call(prompt);

        // 获取 deepseek-reasoner 生成的 CoT 内容，仅在使用 deepseek-reasoner 模型时可用
        // application.properties 中配置的 spring.ai.deepseek.chat.options.model=deepseek-reasoner
        DeepSeekAssistantMessage deepSeekAssistantMessage = (DeepSeekAssistantMessage) response.getResult().getOutput();
        String reasoningContent = deepSeekAssistantMessage.getReasoningContent();
        String text = deepSeekAssistantMessage.getText();
        HashMap<String, String> result = new HashMap<>();
        result.put("text", text);
        result.put("reasoningContent", reasoningContent);
        return result;
    }

}