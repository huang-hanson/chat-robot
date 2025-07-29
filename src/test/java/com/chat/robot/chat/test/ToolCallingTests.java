package com.chat.robot.chat.test;

import com.baomidou.mybatisplus.core.conditions.interfaces.Func;
import com.chat.robot.chat.test.api.tool.WeatherService;
import com.chat.robot.util.DateTimeTools;
import com.chat.robot.util.WeatherTools;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.ai.deepseek.api.DeepSeekApi;
import org.springframework.ai.model.tool.DefaultToolCallingManager;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.ai.tool.metadata.ToolMetadata;
import org.springframework.ai.tool.method.MethodToolCallback;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.util.json.schema.JsonSchemaGenerator;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class ToolCallingTests {

    //    @Resource(name = "zhiPuAiChatModel")
    @Resource(name = "deepSeekChatModel")
    private ChatModel chatModel;

    @Test
    void test_information_retrieval_demo() {
        String response = ChatClient.create(chatModel)
                .prompt("明天是几号?")
                .tools(new DateTimeTools())
                .call()
                .content();

        System.out.println(response);
    }

    @Test
    void test_taking_action_demo() {
        String response = ChatClient.create(chatModel)
                .prompt("你能帮我设置一个 10 分钟后的闹钟吗?")
                .tools(new DateTimeTools())
                .call()
                .content();

        System.out.println(response);
    }

    @Test
    void test_information_retrieval_toolcallback_demo() {
        ToolCallback[] dateTimeTools = ToolCallbacks.from(new DateTimeTools());
        String response = ChatClient.create(chatModel)
                .prompt("明天是几号?")
                .toolCallbacks(dateTimeTools)
                .call()
                .content();

        System.out.println(response);
    }

    @Test
    void test_chatClient_defaultTools() {
        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultTools(new DateTimeTools())
                .build();

        String response = chatClient
                .prompt("明天是几号?")
                .call()
                .content();

        System.out.println(response);
    }

    @Test
    void test_chatModel_toolCallbacks() {
        ToolCallback[] dateTimeTools = ToolCallbacks.from(new DateTimeTools());
        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(dateTimeTools)
                .build();
        Prompt prompt = new Prompt("明天是几号?", chatOptions);
        ChatResponse call = chatModel.call(prompt);
        System.out.println(call.getResult().getOutput().getText());
    }

    @Test
    void test_chatModel_default_toolCallbacks() {
        DeepSeekApi deepSeekApi = DeepSeekApi.builder()
                .apiKey("sk-d4305aaa3dc84342afba42467d6d7788")
                .build();

        ToolCallback[] dateTimeTools = ToolCallbacks.from(new DateTimeTools());

        DeepSeekChatOptions options = DeepSeekChatOptions.builder()
                .model("deepseek-chat")
                .toolCallbacks(dateTimeTools)
                .build();

        ChatModel chatModel = DeepSeekChatModel.builder()
                .deepSeekApi(deepSeekApi)
                .defaultOptions(options)
                .build();

        Prompt prompt = new Prompt("明天是几号？");
        ChatResponse response = chatModel.call(prompt);
        System.out.println(response.getResult().getOutput().getText());
    }

    @Test
    void test_methodToolCallback_demo() {
        Method method = ReflectionUtils.findMethod(DateTimeTools.class, "getCurrentDateTimeWithoutTool");
        ToolCallback toolCallback = MethodToolCallback.builder()
                .toolDefinition(ToolDefinition.builder()
                        .name("getCurrentDateTimeWithoutTool")
                        .description("获取当前时间")
                        .inputSchema("{}")
                        .build())
                .toolMethod(method)
//                .toolObject(new DateTimeTools())
                .build();

        String content = ChatClient.create(chatModel)
                .prompt("明天是几号?")
                .toolCallbacks(toolCallback)  // 关键修改：使用toolCallbacks()
                .call()
                .content();

        System.out.println(content);
    }

    @Test
    void test_functionToolCallback_demo() {
        UserMessage userMessage = new UserMessage(
                "旧金山、东京和巴黎的天气怎么样？以摄氏度为单位的温度返回。");

        List<Message> messages = new ArrayList<>(List.of(userMessage));

        var promptOptions = DeepSeekChatOptions.builder()
                .model(DeepSeekApi.ChatModel.DEEPSEEK_CHAT.getValue())
                .toolCallbacks(List.of(FunctionToolCallback.builder("getCurrentWeather", new WeatherService())
                        .description("获取当前位置的天气情况")
                        .inputType(WeatherService.WeatherRequest.class)
                        .build()))
                .build();

        ChatResponse response = this.chatModel.call(new Prompt(messages, promptOptions));

        System.out.println(response.getResult().getOutput().getText());
    }

    @Test
    void test_functionToolCallback_demo1() {
        ToolCallback toolCallback = FunctionToolCallback
                .builder("currentWeather", new WeatherService())
                .description("Get the weather in location")
                .inputType(WeatherService.WeatherRequest.class)
                .build();

        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(toolCallback)
                .build();
        Prompt prompt = new Prompt("旧金山、东京和巴黎的天气怎么样？以摄氏度为单位的温度返回。", chatOptions);
        String text = chatModel.call(prompt).getResult().getOutput().getText();
        System.out.println(text);
    }

    @Test
    void test_functionToolCallback_bean_demo() {
        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolNames("getWeather")
                .build();
        Prompt prompt = new Prompt("旧金山、东京和巴黎的天气怎么样？以摄氏度为单位的温度返回。", chatOptions);
        String content = chatModel.call(prompt).getResult().getOutput().getText();
        System.out.println(content);
    }

    @Test
    void test_functionToolCallback_double_bean_demo() {
        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolNames("getWeather", "getTime")
                .build();
        Prompt prompt = new Prompt("分别说出旧金山、东京和巴黎的当前时间及当前天气怎么样？以摄氏度为单位的温度返回。", chatOptions);
        String content = chatModel.call(prompt).getResult().getOutput().getText();
        System.out.println(content);
    }

    @Test
    void test_toolCallingManager_demo() {
        ToolCallingManager toolCallingManager = DefaultToolCallingManager.builder().build();
        ChatMemory chatMemory = MessageWindowChatMemory.builder().build();
        String conversationId = UUID.randomUUID().toString();

        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(ToolCallbacks.from(new MathTools()))
                .internalToolExecutionEnabled(false)
                .build();
        Prompt prompt = new Prompt(
                List.of(new SystemMessage("你是一个乐于助人的助手。"), new UserMessage("6乘8等于多少?")),
                chatOptions);
        chatMemory.add(conversationId, prompt.getInstructions());

        Prompt promptWithMemory = new Prompt(chatMemory.get(conversationId), chatOptions);
        ChatResponse chatResponse = chatModel.call(promptWithMemory);
        System.out.println(chatResponse.getResult().getOutput().getText());
        chatMemory.add(conversationId, chatResponse.getResult().getOutput());

        while (chatResponse.hasToolCalls()) {
            ToolExecutionResult toolExecutionResult = toolCallingManager.executeToolCalls(promptWithMemory,
                    chatResponse);
            chatMemory.add(conversationId, toolExecutionResult.conversationHistory()
                    .get(toolExecutionResult.conversationHistory().size() - 1));
            promptWithMemory = new Prompt(chatMemory.get(conversationId), chatOptions);
            chatResponse = chatModel.call(promptWithMemory);
            System.out.println(chatResponse.getResult().getOutput().getText());
            chatMemory.add(conversationId, chatResponse.getResult().getOutput());
        }

        UserMessage newUserMessage = new UserMessage("我之前问你什么?");
        chatMemory.add(conversationId, newUserMessage);

        ChatResponse newResponse = chatModel.call(new Prompt(chatMemory.get(conversationId)));
        System.out.println(newResponse.getResult().getOutput().getText());
    }

    static class MathTools {

        @Tool(description = "将两个数字相乘")
        double multiply(double a, double b) {
            return a * b;
        }

    }
}