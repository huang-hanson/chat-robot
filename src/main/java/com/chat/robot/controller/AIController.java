package com.chat.robot.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author hanson.huang
 * @version V1.0
 * @ClassName AIController
 * @Description TODO
 * @date 2025/6/3 20:17
 **/
@RestController
public class AIController {

    @Resource
    @Qualifier("defaultTextChatClient")
    private ChatClient defaultTextChatClient;

    @GetMapping("/ai/simple")
    public Map<String, String> completion(@RequestParam(value = "message", defaultValue = "给我讲个笑话") String message) {
        return Map.of("completion", this.defaultTextChatClient.prompt().user(message).call().content());
    }

    @Resource
    @Qualifier("withParamTextChatClient")
    private ChatClient withParamTextChatClient;

    @GetMapping("/ai/withParamTextChatClient")
    Map<String, String> withParamTextChatClientCompletion(@RequestParam(value = "message", defaultValue = "给我讲个笑话") String message, @RequestParam(value = "voice", defaultValue = "语文老师") String voice) {
        return Map.of("completion",
                this.withParamTextChatClient.prompt()
                        .system(sp -> sp.param("voice", voice))
                        .user(message)
                        .call()
                        .content());
    }

}