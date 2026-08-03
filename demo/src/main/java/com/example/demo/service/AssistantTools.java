package com.example.demo.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import com.example.demo.dto.TicketRequest;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;

@Component
public class AssistantTools {

    

    @Tool("Cria um chamado no SAP. Só chame depois de ter coletado informações suficientes na conversa — nunca com base em uma descrição vaga como 'problema no SAP'")
public String createTicket(
        @ToolMemoryId String session,
        @P("título curto e objetivo do problema, ex: 'Erro ao salvar transação VA01'") String description,
        @P("descrição completa: transação/tela, mensagem de erro exata, desde quando ocorre, se afeta outros usuários") String longText,
        @P("prioridade de 1 a 4, sendo 1 a mais urgente") String priority,
        @P("tipo do chamado: 'incident' para erro/travamento, 'request' para solicitação") String type) {

    if (longText == null || longText.length() < 30) {
        throw new IllegalArgumentException(
                "Descrição insuficiente para abrir o chamado. Pergunte ao usuário: transação/tela onde ocorreu, " +
                        "mensagem de erro exata, e desde quando o problema acontece.");
    }

    TicketCriadoHolder.set(new TicketInfo(description, longText, priority, type));
    System.out.println("chegou aqui");

        return "Informações coletadas. O chamado será registrado no sistema.";

}

public record TicketInfo(String description, String longText, String priority, String type) {}

}
