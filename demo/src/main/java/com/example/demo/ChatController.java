package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.langchain4j.service.MemoryId;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final Assistant assistant;

    public ChatController(Assistant assistant) {
        this.assistant = assistant;
    }

    @PostMapping
    public ChatResponse chat(@RequestBody  ChatRequest request) {
        System.out.println("processou algo?");
        System.out.println(request.message());
        String answer = assistant.chat(request.session(),request.message());
        return new ChatResponse(answer);
    }
}

record ChatRequest( String message, String session) {}
record ChatResponse(String answer) {}