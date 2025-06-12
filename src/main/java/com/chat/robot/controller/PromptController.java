package com.chat.robot.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author hanson.huang
 * @version V1.0
 * @ClassName PromptController
 * @Description 提示词demo
 * @date 2025/6/12 16:10
 **/
@RestController
public class PromptController {

    @Resource(name = "zhiPuAiChatModel")
//    @Resource(name = "deepSeekChatModel")
    private ChatModel chatModel;

    @Value("classpath:/prompts/system-message.st")
    private org.springframework.core.io.Resource systemResource;

    @GetMapping("/prompt/demo1")
    Generation demo1(@RequestParam("adjective") String adjective, @RequestParam("topic") String topic) {
        PromptTemplate promptTemplate = new PromptTemplate("告诉我一个关于 {topic} 的 {adjective} 笑话");

        Prompt prompt = promptTemplate.create(Map.of("adjective", adjective, "topic", topic));

        return chatModel.call(prompt).getResult();
    }

    @GetMapping("/prompt/demo2")
    List<Generation> demo2(@RequestParam("name") String name, @RequestParam("voice") String voice) {
        String userText = """
                告诉三国演义这篇著作讲了什么内容
                """;

        Message userMessage = new UserMessage(userText);

        String systemText = """
                你是一个帮助人们查找信息的 AI 助手。
                你的名字是 {name}
                你应该用你的名字回复用户的请求，并且用 {voice} 的风格。
                """;

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("name", name, "voice", voice));

        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));

        return chatModel.call(prompt).getResults();
    }

    @GetMapping("/prompt/demo3")
    String demo3(@RequestParam("actor") String actor) {
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .renderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
                .template("""
                        告诉我 5 部由 <actor> 主演的电影名称。
                        """)
                .build();

        String prompt = promptTemplate.render(Map.of("actor", actor));

        return chatModel.call(prompt);
    }

    @GetMapping("/prompt/demo4")
    Generation demo4(@RequestParam("name") String name, @RequestParam("voice") String voice) {
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemResource);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("name", name, "voice", voice));
        Prompt prompt = new Prompt(systemMessage);
        return chatModel.call(prompt).getResult();
    }

    @GetMapping("/prompt/demo5")
    String demo5() {
        return ChatClient.create(chatModel).prompt()
                .user(u -> u.text("解释你在这张图片上看到了什么？")
                        .media(MimeTypeUtils.IMAGE_PNG, new ClassPathResource("/image/multimodal.png")))
                .call()
                .content();
    }
}