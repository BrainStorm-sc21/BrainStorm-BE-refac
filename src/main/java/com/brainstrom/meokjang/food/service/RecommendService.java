package com.brainstrom.meokjang.food.service;

import com.brainstrom.meokjang.food.dto.OcrFoodDto;
import com.brainstrom.meokjang.food.dto.request.OcrRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


@Component
public class RecommendService {
    private final String document_url;
    private final String general_url;
    private final String document_secretKey;
    private final String general_secretKey;
    private String url;
    private String secretKey;

    public RecommendService(@Value("${GENERAL_URL}") String general_url,
                            @Value("${GENERAL_SECRET_KEY}") String general_secretKey,
                            @Value("${DOCUMENT_URL}") String document_url,
                            @Value("${DOCUMENT_SECRET_KEY}") String document_secretKey) {
        this.general_url = general_url;
        this.general_secretKey = general_secretKey;
        this.document_url = document_url;
        this.document_secretKey = document_secretKey;
    }

    public Map<Integer, OcrFoodDto> doOcr(OcrRequest ocrRequest) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-OCR-SECRET", secretKey);

        // API 요청 바디 정보 (이미지 파일)
        File imageFile = ocrRequest.getImage().getResource().getFile();
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
        String base64EncodedImage = Base64.getEncoder().encodeToString(imageBytes);
        JSONObject requestJson = new JSONObject();
        requestJson.put("version", "V2");
        requestJson.put("requestId", "meokjang");
        requestJson.put("timestamp", Long.toString(System.currentTimeMillis()));
        requestJson.put("images", new JSONArray().put(new JSONObject().put("format", "png").put("name", "test").put("data", base64EncodedImage)));

        if (ocrRequest.getType().equals("document")) {
            url = document_url;
            secretKey = document_secretKey;
        } else {
            url = general_url;
            secretKey = general_secretKey;
            requestJson.put("lang", "ko");
        }
        HttpEntity<JSONObject> requestBody = new HttpEntity<>(requestJson, headers);
        // API 호출
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestBody, String.class);

        // API 응답 결과 파싱
        Map<Integer, OcrFoodDto> ocrResult = new HashMap<>();
        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            if (ocrRequest.getType().equals("document")) {
                ocrResult = documentToList(responseBody, ocrResult);
            } else {
                ocrResult = generalToList(responseBody, ocrResult);
            }

        } else {
            System.out.println("API 호출 실패: " + response.getStatusCode());
            return null;
        }
        return ocrResult;
    }

    public Map<Integer, OcrFoodDto> documentToList(String responseBody, Map<Integer, OcrFoodDto> ocrResult) {
        int count = 0;

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONArray images = jsonObject.getJSONArray("images");
        JSONObject receipt = images.getJSONObject(0).getJSONObject("receipt");
        JSONObject result = receipt.getJSONObject("result");
        JSONArray subResults = result.getJSONArray("subResults");
        JSONArray items = subResults.getJSONObject(0).getJSONArray("items");
        for (int i = 0; i < items.length(); i++) {
            String foodName = items.getJSONObject(i).getJSONObject("name").getString("text");
            String foodCount = items.getJSONObject(i).getJSONObject("count").getString("text");
            ocrResult.put(count, new OcrFoodDto(foodName, Integer.parseInt(foodCount)));
            count++;
        }
        return ocrResult;
    }

    public Map<Integer, OcrFoodDto> generalToList(String responseBody, Map<Integer, OcrFoodDto> ocrResult){
        int count = 0;
        int lineCount = 0;
        String foodName = null;
        int foodCount = 0;
        int readCount = 0;

        JSONObject jsonObject = new JSONObject(responseBody);
        JSONArray images = jsonObject.getJSONArray("images");
        JSONArray fields = images.getJSONObject(0).getJSONArray("fields");
        for (int i = 0; i < fields.length(); i++) {
            String text = fields.getJSONObject(i).getString("inferText");
            String lineSwitch = fields.getJSONObject(i).getString("lineBreak");
            if (lineCount == 0) {
                StringBuilder sb = new StringBuilder();
                sb.append(text);
                if (lineSwitch.equals("true")) {
                    foodName = sb.toString();
                    lineCount++;
                }
                continue;
            } else if (lineCount == 1) {
                readCount++;
                if (lineSwitch.equals("true")) {
                    char[] result = text.toCharArray();
                    if (result[1] == '개' && Character.isDigit(result[0])){
                        foodCount = Character.getNumericValue(result[0]);
                    }
                    else {
                        lineCount = 0;
                        readCount = 0;
                        i -= readCount-1;
                        continue;
                    }
                    lineCount++;
                }
                continue;
            } else if (lineCount == 2 ) {
                if (lineSwitch.equals("true") && text.equals("주문완료")){
                    lineCount = 0;
                }
                else {
                    lineCount = 0;
                    continue;
                }
            }
            ocrResult.put(count, new OcrFoodDto(foodName, foodCount));
            count++;
        }

        return ocrResult;
    }
}