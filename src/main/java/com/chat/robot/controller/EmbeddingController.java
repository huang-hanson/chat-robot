package com.chat.robot.controller;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author hanson.huang
 * @version V1.0
 * @ClassName EmbeddingController
 * @Description 嵌入模型
 * @date 2025/6/3 15:59
 **/
@RestController
public class EmbeddingController {

//    private final EmbeddingModel embeddingModel;
//
//    @Autowired
//    public EmbeddingController(EmbeddingModel embeddingModel) {
//        this.embeddingModel = embeddingModel;
//    }
//
//    @GetMapping("/ai/embedding")
//    public Map embed(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
//        EmbeddingResponse embeddingResponse = this.embeddingModel.embedForResponse(List.of(message));
//        return Map.of("embedding", embeddingResponse);
//    }
}