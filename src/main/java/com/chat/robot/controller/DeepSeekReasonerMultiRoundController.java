package com.chat.robot.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekAssistantMessage;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.ai.deepseek.api.DeepSeekApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class DeepSeekReasonerMultiRoundController {

    @Resource
    private DeepSeekChatModel chatModel;

    @GetMapping("/ai/deepseek-reasoner-multi-round")
    public Map<String, String> generate(@RequestParam(value = "message", defaultValue = "9.11 和 9.8，哪个更大？") String message) {
        List<Message> messages = new ArrayList<>();
        messages.add(new UserMessage(message));
        DeepSeekChatOptions promptOptions = DeepSeekChatOptions.builder()
                .model(DeepSeekApi.ChatModel.DEEPSEEK_REASONER.getValue())
                .build();

        Prompt prompt = new Prompt(messages, promptOptions);
        ChatResponse response = chatModel.call(prompt);

        DeepSeekAssistantMessage deepSeekAssistantMessage = (DeepSeekAssistantMessage) response.getResult().getOutput();
        String reasoningContent = deepSeekAssistantMessage.getReasoningContent();
        String text = deepSeekAssistantMessage.getText();

        messages.add(new AssistantMessage(Objects.requireNonNull(text)));
        messages.add(new UserMessage("单词 'strawberry' 中有多少个 R？"));
        Prompt prompt2 = new Prompt(messages, promptOptions);
        ChatResponse response2 = chatModel.call(prompt2);

        DeepSeekAssistantMessage deepSeekAssistantMessage2 = (DeepSeekAssistantMessage) response2.getResult().getOutput();
        String reasoningContent2 = deepSeekAssistantMessage2.getReasoningContent();
        HashMap<String, String> result = new HashMap<>();
        result.put("text", text);
        result.put("reasoningContent", reasoningContent);
        result.put("text2", deepSeekAssistantMessage2.getText());
        result.put("reasoningContent2", reasoningContent2);
        return result;
    }

}