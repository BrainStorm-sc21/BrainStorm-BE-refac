package com.brainstrom.meokjang.common.service;

import com.brainstrom.meokjang.common.dto.request.RecipeRequest;
import com.brainstrom.meokjang.common.dto.response.RecipeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class RecipeService {

    private final String apiKey;
    private final String apiPrompt;
    private final RestTemplate restTemplate;

    @Autowired
    public RecipeService(@Value("${KOGPT_API_KEY}") String apiKey,
                         @Value("${KOGPT_API_PROMPT}") String apiPrompt) {
        this.apiKey = apiKey;
        this.apiPrompt = apiPrompt;
        this.restTemplate = new RestTemplate();
    }

    public RecipeResponse getRecipe(RecipeRequest req) {
        if (req.getFoodList().size() == 0) {
            throw new IllegalStateException("식재료를 선택하지 않았습니다.");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        headers.set("Authorization", "KakaoAK " + apiKey);

        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("prompt", apiPrompt + req.getFoodList().toString());
        reqBody.put("max_tokens", 200);
        reqBody.put("temperature", 0.3);
        reqBody.put("top_p", 0.3);
        reqBody.put("n", 1);

        HttpEntity<Object> entity = new HttpEntity<>(reqBody, headers);
        ResponseEntity<String> res = restTemplate.postForEntity("https://api.kakaobrain.com/v1/inference/kogpt/generation", entity, String.class);

        HttpStatusCode statusCode = res.getStatusCode();
        if (statusCode != HttpStatus.OK) {
            throw new IllegalStateException("API 호출에 실패했습니다.");
        }

        System.out.println(res.getBody());
        return new RecipeResponse(res.getBody());
    }
}
