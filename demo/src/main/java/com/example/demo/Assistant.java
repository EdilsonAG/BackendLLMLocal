package com.example.demo;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;


@AiService
public interface Assistant {

    @SystemMessage("Você é um assistente útil, direto e responde em português.")
    String chat(@MemoryId String sessionId,@UserMessage String mensagem);
}
