package com.chat.robot.controller;

import com.chat.robot.chat.ActorsFilms;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.image.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@RestController
public class ChatController {

    private final DeepSeekChatModel chatModel;

    @Autowired
    public ChatController(DeepSeekChatModel chatModel) {
        this.chatModel = chatModel;
    }

//    @Resource
//    private ImageModel imageModel;

    @GetMapping("/ai/generate")
    public Map generate(@RequestParam(value = "message", defaultValue = "给我讲个笑话") String message) {
        return Map.of("generation", chatModel.call(message));
    }

    @GetMapping("/ai/generateStream")
    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "给我讲个笑话") String message) {
        var prompt = new Prompt(new UserMessage(message));
        return chatModel.stream(prompt);
    }


    /**
     * 以下是使用高级、流畅的 ChatClient API 应用 BeanOutputConverter 的方法：
     *
     * @param message
     * @return
     */
    @GetMapping("/ai/converter")
    public ActorsFilms converter(@RequestParam(value = "actor", defaultValue = "Tom Hanks") String message) {
        return ChatClient.create(chatModel).prompt()
                .user(u -> u.text("为 {actor} 生成 5 部电影的电影作品。")
                        .param("actor", message))
                .call()
                .entity(ActorsFilms.class);
    }

    /**
     * 或直接使用低级 ChatModel API
     *
     * @param message
     * @return
     */
    @GetMapping("/ai/converter2")
    public ActorsFilms converter2(@RequestParam(value = "actor", defaultValue = "Tom Hanks") String message) {
        // 1. 创建输出转换器
        BeanOutputConverter<ActorsFilms> beanOutputConverter = new BeanOutputConverter<>(ActorsFilms.class);
        String format = beanOutputConverter.getFormat();

        // 2. 新版创建PromptTemplate的方式
        PromptTemplate promptTemplate = new PromptTemplate(
                "为 {actor} 生成 5 部电影的电影作品。\n{format}"
        );

        // 3. 创建Prompt时传入变量
        Prompt prompt = promptTemplate.create(
                Map.of("actor", message, "format", format)
        );

        // 4. 调用模型并转换结果
        Generation generation = chatModel.call(prompt).getResult();
        String text = generation.getOutput().getText();
        return beanOutputConverter.convert(text);
    }

    @GetMapping("/ai/converter3")
    public List<ActorsFilms> converter3(@RequestParam(value = "actor1", defaultValue = "Tom Hanks") String actor1,
                                        @RequestParam(value = "actor2", defaultValue = "成龙") String actor2) {
        return ChatClient.create(chatModel).prompt()
                .user(u -> u
                        .text("为 {actor1} 和 {actor2} 各生成5部电影作品，用JSON数组格式返回。")
                        .param("actor1", actor1)
                        .param("actor2", actor2)
                )
                .call()
                .entity(new ParameterizedTypeReference<List<ActorsFilms>>() {
                });
    }

    @GetMapping("/ai/mapOutputConverter")
    public Map<String, Object> mapOutputConverter(@RequestParam(value = "subject") String subject) {
        return ChatClient.create(chatModel).prompt()
                .user(u -> u.text("为我提供 {subject}")
                        .param("subject", subject))
                .call()
                .entity(new ParameterizedTypeReference<Map<String, Object>>() {
                });
    }

    @GetMapping("/ai/mapOutputConverter2")
    public Map<String, Object> mapOutputConverter2(@RequestParam(value = "subject") String subject) {
        MapOutputConverter mapOutputConverter = new MapOutputConverter();

        String format = mapOutputConverter.getFormat();

        PromptTemplate promptTemplate = new PromptTemplate(
                """
               为我提供 {subject}
               {format}
               """
        );

        Prompt prompt = promptTemplate.create(
                Map.of("subject", subject, "format", format)
        );

        // 4. 调用模型并转换结果
        Generation generation = chatModel.call(prompt).getResult();
        String text = generation.getOutput().getText();

        return mapOutputConverter.convert(text);
    }

    /**
     * 图像模型：用zhipu ai，新用户注册能白嫖400次
     *
     * model: cogview-3
     *
     * @param describe
     * @return
     */
    @GetMapping("/ai/image")
    public Image image(@RequestParam(value = "describe", defaultValue = "解释你在这张图片上看到了什么？") String describe) {
        var options = ImageOptionsBuilder.builder().height(1024).width(1024).build();
        ImagePrompt imagePrompt = new ImagePrompt(describe, options);
//        ImageResponse imageResponse = this.imageModel.call(imagePrompt);
//        var generation = imageResponse.getResult();
//        return generation.getOutput();
        return null;
    }
}