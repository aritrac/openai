package com.binarylife.openai.config;

import com.binarylife.openai.advisors.TokenUsageAuditAdvisor;
import com.openai.models.ChatModel;
import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient openAiChatClient(ChatClient.Builder chatClientBuilder) {
        var options = OpenAiChatOptions.builder()
                .model(ChatModel.GPT_5_4_MINI.asString())
                .temperature(0.8);
//                .maxCompletionTokens(10); We don't want to restrict the response of the model
        return chatClientBuilder
                .defaultOptions(options)
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor(), new TokenUsageAuditAdvisor())) //global advisors
                .defaultSystem("""
                        You are an internal HR assistant. Your role is to help\s
                        employees with questions related to HR policies, such as \s
                        leave policies, working hours, benefits, and code of conduct.
                        If a user asks for help with anything outside of these topics,\s
                        kindly inform them that you can only assist with queries related to\s
                        HR policies.
                        """)
                .defaultUser("""
                        How can you help me?
                        """)
                .build();
    }

}
