package com.binarylife.openai.config;

import com.binarylife.openai.advisors.TokenUsageAuditAdvisor;
import com.binarylife.openai.rag.PIIMaskingDocumentPostProcessor;
import com.openai.models.ChatModel;
import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatMemoryChatClientConfig {

    @Bean
    ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository) {
        return MessageWindowChatMemory.builder().maxMessages(10)
                .chatMemoryRepository(jdbcChatMemoryRepository).build();
    }

    @Bean
    public ChatClient chatMemoryChatClient(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory,
                                           RetrievalAugmentationAdvisor retrievalAugmentationAdvisor) {
        Advisor loggerAdvisor = new SimpleLoggerAdvisor();
        Advisor tokenAuditAdvisor = new TokenUsageAuditAdvisor();
        Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        return chatClientBuilder
                .defaultAdvisors(List.of(loggerAdvisor, memoryAdvisor, tokenAuditAdvisor, retrievalAugmentationAdvisor))
                .build();
    }

    @Bean
    RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(VectorStore vectorStore, ChatClient.Builder chatClientBuilder) {
        return RetrievalAugmentationAdvisor.builder()
                .queryTransformers(TranslationQueryTransformer
                        .builder()
                        .chatClientBuilder(chatClientBuilder.clone())
                        .targetLanguage("english")
                        .build())
                .documentRetriever(
                        VectorStoreDocumentRetriever.builder().vectorStore(vectorStore)
                                .topK(3).similarityThreshold(0.5).build()
                )
                .documentPostProcessors(PIIMaskingDocumentPostProcessor.builder())
                .build();
    }

}
