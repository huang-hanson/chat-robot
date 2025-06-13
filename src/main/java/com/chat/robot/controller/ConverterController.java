package com.chat.robot.controller;

import com.chat.robot.chat.ActorsFilms;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * @author hanson.huang
 * @version V1.0
 * @ClassName ConverterController
 * @date 2025/6/13 11:03
 **/
@RestController
public class ConverterController {

    @Resource(name = "zhiPuAiChatModel")
//    @Resource(name = "deepSeekChatModel")
    private ChatModel chatModel;

    @GetMapping("/converter/demo1")
    ActorsFilms demo1(@RequestParam("actor") String actor) {
        return ChatClient.create(chatModel).prompt()
                .user(u -> u.text("为 {actor} 生成 5 部电影的电影作品。")
                        .param("actor", actor))
                .call()
                .entity(ActorsFilms.class);
    }

    @GetMapping("/converter/demo2")
    ActorsFilms demo2(@RequestParam("actor") String actor) {
        // 1. 创建输出转换器
        BeanOutputConverter<ActorsFilms> beanOutputConverter =
                new BeanOutputConverter<>(ActorsFilms.class);
        String format = beanOutputConverter.getFormat();

        // 2. 新版创建PromptTemplate的方式
        PromptTemplate promptTemplate = new PromptTemplate(
                "为 {actor} 生成 5 部电影的电影作品。\n{format}"
        );

        // 3. 创建Prompt时传入变量
        Prompt prompt = promptTemplate.create(
                Map.of("actor", actor, "format", format)
        );

        // 4. 调用模型并转换结果
        Generation generation = chatModel.call(prompt).getResult();
        String text = generation.getOutput().getText();
        return beanOutputConverter.convert(text);
    }

    @GetMapping("/converter/demo3")
    List<ActorsFilms> demo3(@RequestParam("actor1") String actor1, @RequestParam("actor2") String actor2) {
        return ChatClient.create(chatModel).prompt()
                .user(u -> u.text("为 {actor1} 和 {actor2} 生成 5 部电影的电影作品。")
                        .param("actor1", actor1).param("actor2", actor2))
                .call()
                .entity(new ParameterizedTypeReference<List<ActorsFilms>>() {
                });
    }

    @GetMapping("/converter/demo4")
    List<ActorsFilms> demo4(@RequestParam("actor1") String actor1, @RequestParam("actor2") String actor2) {
        // 1. 创建输出转换器
        BeanOutputConverter<List<ActorsFilms>> outputConverter = new BeanOutputConverter<>(
                new ParameterizedTypeReference<List<ActorsFilms>>() {
                });
        String format = outputConverter.getFormat();

        // 2. 新版创建PromptTemplate的方式
        PromptTemplate promptTemplate = new PromptTemplate(
                "为 {actor1} 和 {actor2} 生成 5 部电影的电影作品。\n{format}"
        );

        // 3. 创建Prompt时传入变量
        Prompt prompt = promptTemplate.create(
                Map.of("actor1", actor1, "actor2", actor2, "format", format)
        );

        // 4. 调用模型并转换结果
        Generation generation = chatModel.call(prompt).getResult();
        return outputConverter.convert(generation.getOutput().getText());
    }

    @GetMapping("/converter/demo5")
    Map<String, Object> demo5(@RequestParam("subject") String subject) {
        return ChatClient.create(chatModel).prompt()
                .user(u -> u.text("为我提供 {subject}")
                        .param("subject", subject))
                .call()
                .entity(new ParameterizedTypeReference<Map<String, Object>>() {
                });
    }

    @GetMapping("/converter/demo6")
    Map<String, Object> demo6(@RequestParam("subject") String subject) {
        // 1. 创建输出转换器
        MapOutputConverter mapOutputConverter = new MapOutputConverter();
        String format = mapOutputConverter.getFormat();

        // 2. 新版创建PromptTemplate的方式
        PromptTemplate promptTemplate = new PromptTemplate(
                "为我提供 {subject}。\n{format}"
        );

        // 3. 创建Prompt时传入变量
        Prompt prompt = promptTemplate.create(
                Map.of("subject", subject, "format", format)
        );

        // 4. 调用模型并转换结果
        Generation generation = chatModel.call(prompt).getResult();
        return mapOutputConverter.convert(generation.getOutput().getText());
    }

    @GetMapping("/converter/demo7")
    List<String> demo7(@RequestParam("subject") String subject) {
        return ChatClient.create(chatModel).prompt()
                .user(u -> u.text("用中文列出五种 {subject}")
                        .param("subject", subject))
                .call()
                .entity(new ListOutputConverter(new DefaultConversionService()));
    }

    @GetMapping("/converter/demo8")
    List<String> demo8(@RequestParam("subject") String subject) {
        // 1. 创建输出转换器
        ListOutputConverter listOutputConverter = new ListOutputConverter(new DefaultConversionService());

        String format = listOutputConverter.getFormat();

        // 2. 新版创建PromptTemplate的方式
        PromptTemplate promptTemplate = new PromptTemplate(
                "用中文列出五种 {subject}。\n{format}"
        );

        // 3. 创建Prompt时传入变量
        Prompt prompt = promptTemplate.create(
                Map.of("subject", subject, "format", format)
        );

        // 4. 调用模型并转换结果
        Generation generation = chatModel.call(prompt).getResult();
        return listOutputConverter.convert(generation.getOutput().getText());
    }
}