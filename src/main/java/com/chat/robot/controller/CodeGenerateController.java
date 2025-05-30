package com.chat.robot.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CodeGenerateController {

    @Resource
    private DeepSeekChatModel chatModel;

//    @Resource
//    private final DeepSeekChatModel chatModel;
//
//    @Autowired
//    public CodeGenerateController(DeepSeekChatModel chatModel) {
//        this.chatModel = chatModel;
//    }

    @GetMapping("/ai/generatePythonCode")
    public String generate(@RequestParam(value = "message", defaultValue = "请写一个快速排序代码") String message) {
        UserMessage userMessage = new UserMessage(message);

//        Message assistantMessage = DeepSeekAssistantMessage.prefixAssistantMessage("```python\\n");
//        Prompt prompt = new Prompt(List.of(userMessage, assistantMessage), ChatOptions.builder().stopSequences(List.of("```")).build());
//        ChatResponse response = chatModel.call(prompt);
//        System.out.println(response.getResult().getOutput().getText());

        Prompt prompt = new Prompt(List.of(userMessage));
        ChatResponse response = chatModel.call(prompt);
        String result = response.getResult().getOutput().getText();
        System.out.println(result);
        return response.getResult().getOutput().getText();
    }
}