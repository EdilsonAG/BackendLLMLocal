package com.example.demo.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import com.example.demo.dto.TicketRequest;

import dev.langchain4j.agent.tool.Tool;

@Component
public class AssistantTools {


    private final RestClient restClient;
    private final String sapBaseUrl;
    private final String sapClient;

    public AssistantTools(RestClient restClient, @Value("${sap.base-url}") String sapBaseUrl,
            @Value("${sap.client}") String sapClient){
        this.restClient = restClient;
        this.sapBaseUrl = sapBaseUrl;
        this.sapClient = sapClient;
    }

    @Tool("Cria o ticket com base no objeto TicketRequest no qual possui description longText priority type")
    public String createTicket(String authHeader, String csrfToken, String cookies, TicketRequest input,
            String session) {
        boolean isIncident = input.type() == TicketRequest.TicketType.incident;

        

        String url = isIncident
                ? sapBaseUrl + "/AI_CRM_GW_CREATE_INCIDENT_SRV/IncidentSet?sap-client=" +
                        sapClient
                : sapBaseUrl +
                        "/AI_CRM_GW_MYBUSI_REQUIRE_SRV/BusinessRequirementSet?sap-client=" +
                        sapClient;

        System.out.println("antes do body");
        Map<String, Object> body = isIncident
                ? Map.of(
                        "ProcessType", "ZMIN",
                        "Description", input.description(),
                        "LongText", input.longText(),
                        "Priority", input.priority())
                : Map.of(
                        "Description", input.description(),
                        "Priority", input.priority());

        System.out.println("depois do map body ja");
        try {

            String teste = restClient.post()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .header("X-CSRF-Token", csrfToken)
                    .header(HttpHeaders.COOKIE, cookies)
                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .body(body)
                    .retrieve()
                    .body(String.class);

            System.out.println("RESPOSTA SAP: " + teste);

            return teste;
        } catch (RestClientResponseException e) {
            e.printStackTrace();
            throw new SapClientException(
                    "Erro do SAP (status " + e.getStatusCode().value() + "): " +
                            e.getResponseBodyAsString(),
                    e);
        }
    }

    public static class SapClientException extends RuntimeException {
        public SapClientException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
