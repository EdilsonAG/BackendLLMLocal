package com.example.demo.rag;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;

import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RagConfig {
    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        return new InMemoryEmbeddingStore<>();
    }

    @Bean
    public ChatMemoryProvider chatMemoryProvider(RedisChatMemoryStore store) {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(20)          // janela de mensagens, ajuste conforme necessidade
                .chatMemoryStore(store)
                .build();
    }
}
//     @Bean
//     public ChatMemoryProvider chatMemoryProvider() {
//         Map<Object, ChatMemory> memories = new ConcurrentHashMap<>();

//         return sessionId -> memories.computeIfAbsent(sessionId, id -> MessageWindowChatMemory.builder()
//                 .id(id)
//                 .maxMessages(20)
//                 .build());
//     }

//     @Bean
//     ChatMemoryProvider chatMemoryProvider(ChatMemoryStore redisChatMemoryStore) {
//         return memoryId -> MessageWindowChatMemory.builder()
//                 .id(memoryId)
//                 .maxMessages(20)
//                 .chatMemoryStore(redisChatMemoryStore)
//                 .build();
//     }
//     // @Bean
//     // public ChatLanguageModel chatLanguageModel() {
//     // return OllamaChatModel.builder()
//     // .baseUrl("http://localhost:11434")
//     // .modelName("llama3.2")
//     // .numPredict(100) // limite de tokens de saída
//     // .temperature(0.3) // opcional: menos "criativo", mais direto — bom pra
//     // extração de dados
//     // .build();
//     // }
//     // @Bean
//     // public ChatMemoryStore chatMemoryStore() {
//     // return RedisChatMemoryStore.builder()
//     // .host("localhost")
//     // .port(6379)
//     // .build();
//     // }

//     // @Bean
//     // public ChatMemoryProvider chatMemoryProvider(ChatMemoryStore store) {
//     // return sessionId -> MessageWindowChatMemory.builder()
//     // .id(sessionId)
//     // .maxMessages(20)
//     // .chatMemoryStore(store)
//     // .build();
//     // }

//     // // tira: import
//     // dev.langchain4j.community.store.memory.chat.redis.RedisChatMemoryStore;

//     // @Bean
//     // public ChatMemoryProvider chatMemoryProvider(RedisChatMemoryStore store) {
//     // return sessionId -> MessageWindowChatMemory.builder()
//     // .id(sessionId)
//     // .maxMessages(20)
//     // .chatMemoryStore(store)
//     // .build();
//     // }
// }
