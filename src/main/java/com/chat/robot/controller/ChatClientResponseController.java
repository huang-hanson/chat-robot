package com.chat.robot.controller;

import com.chat.robot.chat.ActorsFilms;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hanson.huang
 * @version V1.0
 * @ClassName ChatClientResponseController
 * @Description TODO
 * @date 2025/6/3 19:02
 **/
@RestController
public class ChatClientResponseController {

    private final ChatClient deepSeekChatClient;

    @Autowired
    public ChatClientResponseController(@Qualifier("deepSeekChatClient") ChatClient deepSeekChatClient) {
        this.deepSeekChatClient = deepSeekChatClient;
    }

    @GetMapping("/ai/chat-client-response")
    ChatResponse generation() {
        return deepSeekChatClient.prompt()
                .user("给我讲个笑话")
                .call()
                .chatResponse();
    }

    @GetMapping("/ai/chat-client-response-entity")
    ActorsFilms generation_entity() {
        return deepSeekChatClient.prompt()
                .user("生成一个随机演员的电影作品。")
                .call()
                .entity(ActorsFilms.class);
    }

    @GetMapping("/ai/chat-client-response-entity/list")
    List<ActorsFilms> generation_entity_list() {
        return deepSeekChatClient.prompt()
                .user("生成成龙和李连杰的 5 部电影作品。")
                .call()
                .entity(new ParameterizedTypeReference<List<ActorsFilms>>() {
                });
    }

    @GetMapping("/ai/flux")
    Flux<String> gen_flux() {
        return deepSeekChatClient.prompt()
                .user("给我讲个笑话")
                .stream()
                .content();
    }

    @GetMapping("/ai/struct/flux")
    List<ActorsFilms> gen_struct_flux() {
        var converter = new BeanOutputConverter<>(new ParameterizedTypeReference<List<ActorsFilms>>() {
        });

        Flux<String> flux = deepSeekChatClient.prompt()
                .user(u -> u.text("""
                                  生成一个随机演员的电影作品。
                                  {format}
                                """)
                        .param("format", converter.getFormat()))
                .stream()
                .content();

        String content = flux.collectList().block().stream().collect(Collectors.joining());

        List<ActorsFilms> actorFilms = converter.convert(content);

        return actorFilms;
    }



    @GetMapping("/ai/log")
    ChatResponse gen_log() {
        return deepSeekChatClient.prompt()
                .advisors(new SimpleLoggerAdvisor())
                .user("给我讲个笑话？")
                .call()
                .chatResponse();
    }

}