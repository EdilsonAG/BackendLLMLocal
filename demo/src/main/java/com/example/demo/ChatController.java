package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CriarTicketStatus;
import com.example.demo.service.AssistantTools.TicketInfo;
import com.example.demo.service.TicketCriadoHolder;

import dev.langchain4j.service.MemoryId;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final Assistant assistant;

    public ChatController(Assistant assistant) {
        this.assistant = assistant;
    }

    @PostMapping
    public ResponseIA  chat(@RequestBody  ChatRequest request) {
        // System.out.println("processou algo?");
        // System.out.println(request.message());
        // String answer = assistant.chat(request.session(),request.message());
        // return new ChatResponse(answer);
        TicketCriadoHolder.clear();
        try {
            String answer = assistant.chat(request.session(), request.message());
            TicketInfo info = TicketCriadoHolder.get();

            CriarTicketStatus status = info != null ? CriarTicketStatus.PRONTO : CriarTicketStatus.INCOMPLETO;

            return new ResponseIA(
                    answer,
                    status,
                    info != null ? info.description() : null,
                    info != null ? info.longText() : null,
                    info != null ? info.priority() : null,
                    info != null ? info.type() : null
            );
        } finally {
            TicketCriadoHolder.clear(); // sempre limpa — thread é reusada pelo Tomcat
        }
    }

    
}
record ResponseIA(
        String resposta,
        CriarTicketStatus status,
        String description,
        String longText,
        String priority,
        String type
) {}
record ChatRequest( String message, String session) {}
record ChatResponse(String answer) {}