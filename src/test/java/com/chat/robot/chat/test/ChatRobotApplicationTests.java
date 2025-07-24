package com.chat.robot.chat.test;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.model.ChatModel;

import java.util.List;
import java.util.Map;

@SpringBootTest
class ChatRobotApplicationTests {

    @Resource
    private ChatModel chatModel;

    @Test
    void templateRenderer() {
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .renderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build())
                .template("""
                        告诉我 5 部由 <composer> 作曲的电影名称。
                        """)
                .build();

        String prompt = promptTemplate.render(Map.of("composer", "John Williams"));
        System.out.println(prompt);
    }

    @Test
    void aI_Workshop_on_PromptTemplates() {
        String userText = """
                告诉我关于黄金时代海盗时期的三位著名海盗以及他们的事迹。
                为每个海盗至少写一句话。
                """;

        Message userMessage = new UserMessage(userText);

        String systemText = """
                你是一个帮助人们查找信息的 AI 助手。
                你的名字是 {name}
                你应该用你的名字回复用户的请求，并且用 {voice} 的风格。
                """;

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("name", "小爱", "voice", "欧亨利"));

        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));
        List<Generation> response = chatModel.call(prompt).getResults();
        System.out.println(response);
    }

}
