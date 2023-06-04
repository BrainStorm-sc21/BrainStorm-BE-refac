package com.brainstrom.meokjang.common.service;

import com.brainstrom.meokjang.common.dto.request.RecipeRequest;
import com.brainstrom.meokjang.common.dto.response.RecipeResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RecipeService {

    private final String apiKey;
    private final String apiPrompt;
    private final RestTemplate restTemplate;

    @Autowired
    public RecipeService(@Value("${OPENAI_API_KEY}") String apiKey,
                         @Value("${OPENAI_API_PROMPT}") String apiPrompt) {
        this.apiKey = apiKey;
        this.apiPrompt = apiPrompt;
        this.restTemplate = new RestTemplate();
    }

    public RecipeResponse getRecipe(RecipeRequest req) throws JsonProcessingException {
        if (req.getFoodList().size() == 0) {
            throw new IllegalStateException("식재료를 선택하지 않았습니다.");
        }
        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        headers.set("Authorization", "Bearer " + apiKey);

        ObjectMapper objectMapper = new ObjectMapper();

        ArrayNode messagesNode = objectMapper.createArrayNode();
        ObjectNode systemMessageNode = objectMapper.createObjectNode()
                .put("role", "system")
                .put("content", apiPrompt);
        messagesNode.add(systemMessageNode);

        for (String food : req.getFoodList()) {
            ObjectNode userMessageNode = objectMapper.createObjectNode()
                    .put("role", "user")
                    .put("content", food);
            messagesNode.add(userMessageNode);
        }

        ObjectNode payload = objectMapper.createObjectNode()
                .put("model", "davinci")
                .set("prompt", messagesNode)
                .put("max_tokens", 100)
                .put("temperature", 0.7)
                .put("top_p", 0.5)
                .put("frequency_penalty", 0.5);

        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(payload), headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        if (!responseEntity.getStatusCode().is2xxSuccessful())
            throw new IllegalStateException("레시피 조회에 실패했습니다.");

        JsonNode responseBody = objectMapper.readTree(responseEntity.getBody());
        String responseText = responseBody.path("choices").path(0).path("text").asText().trim();
        return new RecipeResponse(responseText);
    }
}
