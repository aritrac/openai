package com.binarylife.openai.config;

import com.binarylife.openai.advisors.TokenUsageAuditAdvisor;
import com.binarylife.openai.tools.TimeTools;
import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class TimeChatClientConfig {

    @Bean("timeChatClient")
    public ChatClient chatMemoryChatClient(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory, TimeTools timeTools) {
//        Advisor loggerAdvisor = new SimpleLoggerAdvisor();
//        Advisor tokenAuditAdvisor = new TokenUsageAuditAdvisor(); available in ChatClientBuilderCustomizerConfig
        Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        return chatClientBuilder
                .defaultTools(timeTools)
                .defaultAdvisors(List.of(memoryAdvisor))
                .build();
    }

}
