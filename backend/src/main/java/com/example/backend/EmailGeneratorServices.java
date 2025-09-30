package com.example.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class EmailGeneratorServices {
    private final WebClient webClient;
    private final String apiKey;

    public EmailGeneratorServices(WebClient.Builder webclientBuilder,
                                  @Value("${gemini.api.url}") String baseUrl, @Value("${gemini.api.key}") String geminiApiKey) {
        this.apiKey = geminiApiKey;
        this.webClient = webclientBuilder.baseUrl(baseUrl).build();
    }

    public String genereteEmailReplay(EmailRequest emailRequest) {
        //build prompt
        String prompt = buildPrompt(emailRequest);

        //prepare row Json body
        String requestBody = String.format("""
                {
                    "contents": [
                      {
                        "parts": [
                          {
                            "text": "%s"
                          }
                        ]
                      }
                    ]
                  }
                """,prompt);

        //send Request
        String response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1beta/models/gemini-2.5-flash:generateContent")
                        .build())
                .header("x-goog-api-key",apiKey)
                .header("Content-Type","application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        //Extract Response
        return extractResponseContent(response);
    }

    private String extractResponseContent(String response) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
           return root.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildPrompt(EmailRequest emailRequest) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generete a professional email replay for the following email:");
        if(emailRequest.getTone() != null && !emailRequest.getTone().isEmpty()){
            prompt.append("Use a ").append(emailRequest.getTone()).append(" tone.");
        }
        prompt.append("Original Email: \n").append(emailRequest.getEmailcontent());
       return  prompt.toString();
    }

}
