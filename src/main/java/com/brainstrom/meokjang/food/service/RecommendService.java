package com.brainstrom.meokjang.food.service;

import com.brainstrom.meokjang.food.dto.OcrFoodDto;
import com.brainstrom.meokjang.food.dto.request.OcrRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
        // API 요청 URL
        if (ocrRequest.getType().equals("document")) {
            url = document_url;
            secretKey = document_secretKey;
        } else {
            url = general_url;
            secretKey = general_secretKey;
        }
        // API 요청 헤더 정보
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-OCR-SECRET", secretKey);

        // API 요청 바디 정보 (이미지 파일)
        File imageFile = ocrRequest.getImage().getResource().getFile();
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
        HttpEntity<byte[]> requestBody = new HttpEntity<>(imageBytes, headers);

        // API 호출
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestBody, String.class);

        // API 응답 결과 파싱
        Map<Integer, OcrFoodDto> ocrResult = null;
        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            if (ocrRequest.getType() == "document") {
                ocrResult = documentToList(responseBody);
            } else {
                ocrResult = generalToList(responseBody);
            }

        } else {
            System.out.println("API 호출 실패: " + response.getStatusCode());
        }
        return ocrResult;
    }
    public Map<Integer, OcrFoodDto> documentToList(String responseBody) {
        Map<Integer, OcrFoodDto> ocrResult = new HashMap<>();
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

    public Map<Integer, OcrFoodDto> generalToList(String responseBody){
        return null;
    }
}

//주문내역 상세 아래부터 상품명 시작
//한줄 y값이 10이상 차이나는걸로 구분
// 맨뒤가 '개'로 끝나면 앞에 숫자가 상품수량
//주문완료 나오면 아랫줄이 다시 상품명
// 줄 수를 카운트 하여 예외 처리

//{
//        "version": "V2",
//        "requestId": "string",
//        "timestamp": 1684123328254,
//        "images": [
//        {
//        "uid": "bbd978c6cfeb4ee290abb06917d26840",
//        "name": "test 1",
//        "inferResult": "SUCCESS",
//        "message": "SUCCESS",
//        "validationResult": {
//        "result": "NO_REQUESTED"
//        },
//        "convertedImageInfo": {
//        "width": 810,
//        "height": 1440,
//        "pageIndex": 0,
//        "longImage": false
//        },
//        "fields": [
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 53,
//        "y": 9
//        },
//        {
//        "x": 85,
//        "y": 9
//        },
//        {
//        "x": 85,
//        "y": 31
//        },
//        {
//        "x": 53,
//        "y": 31
//        }
//        ]
//        },
//        "inferText": "LG",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 90,
//        "y": 12
//        },
//        {
//        "x": 122,
//        "y": 12
//        },
//        {
//        "x": 122,
//        "y": 30
//        },
//        {
//        "x": 90,
//        "y": 30
//        }
//        ]
//        },
//        "inferText": "U+",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 345,
//        "y": 6
//        },
//        {
//        "x": 392,
//        "y": 6
//        },
//        {
//        "x": 392,
//        "y": 33
//        },
//        {
//        "x": 345,
//        "y": 33
//        }
//        ]
//        },
//        "inferText": "오후",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 392,
//        "y": 9
//        },
//        {
//        "x": 463,
//        "y": 9
//        },
//        {
//        "x": 463,
//        "y": 31
//        },
//        {
//        "x": 392,
//        "y": 31
//        }
//        ]
//        },
//        "inferText": "10:44",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": true
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 44,
//        "y": 69
//        },
//        {
//        "x": 67,
//        "y": 69
//        },
//        {
//        "x": 67,
//        "y": 100
//        },
//        {
//        "x": 44,
//        "y": 100
//        }
//        ]
//        },
//        "inferText": "<",
//        "inferConfidence": 0.9992,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 312,
//        "y": 64
//        },
//        {
//        "x": 435,
//        "y": 64
//        },
//        {
//        "x": 435,
//        "y": 100
//        },
//        {
//        "x": 312,
//        "y": 100
//        }
//        ]
//        },
//        "inferText": "주문 내역",
//        "inferConfidence": 0.9869,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 435,
//        "y": 64
//        },
//        {
//        "x": 498,
//        "y": 64
//        },
//        {
//        "x": 498,
//        "y": 102
//        },
//        {
//        "x": 435,
//        "y": 102
//        }
//        ]
//        },
//        "inferText": "상세",
//        "inferConfidence": 0.9999,
//        "type": "NORMAL",
//        "lineBreak": true
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 166,
//        "y": 147
//        },
//        {
//        "x": 262,
//        "y": 147
//        },
//        {
//        "x": 262,
//        "y": 179
//        },
//        {
//        "x": 166,
//        "y": 179
//        }
//        ]
//        },
//        "inferText": "[비비고]",
//        "inferConfidence": 0.9999,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 264,
//        "y": 145
//        },
//        {
//        "x": 435,
//        "y": 145
//        },
//        {
//        "x": 435,
//        "y": 177
//        },
//        {
//        "x": 264,
//        "y": 177
//        }
//        ]
//        },
//        "inferText": "순살고등어구이",
//        "inferConfidence": 0.9999,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 437,
//        "y": 147
//        },
//        {
//        "x": 496,
//        "y": 147
//        },
//        {
//        "x": 496,
//        "y": 175
//        },
//        {
//        "x": 437,
//        "y": 175
//        }
//        ]
//        },
//        "inferText": "60G",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": true
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 165,
//        "y": 191
//        },
//        {
//        "x": 274,
//        "y": 189
//        },
//        {
//        "x": 274,
//        "y": 217
//        },
//        {
//        "x": 166,
//        "y": 219
//        }
//        ]
//        },
//        "inferText": "3,984원",
//        "inferConfidence": 0.9998,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 274,
//        "y": 189
//        },
//        {
//        "x": 381,
//        "y": 189
//        },
//        {
//        "x": 381,
//        "y": 219
//        },
//        {
//        "x": 274,
//        "y": 219
//        }
//        ]
//        },
//        "inferText": "4,980원",
//        "inferConfidence": 0.9995,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 396,
//        "y": 189
//        },
//        {
//        "x": 441,
//        "y": 189
//        },
//        {
//        "x": 441,
//        "y": 220
//        },
//        {
//        "x": 396,
//        "y": 220
//        }
//        ]
//        },
//        "inferText": "1개",
//        "inferConfidence": 0.9998,
//        "type": "NORMAL",
//        "lineBreak": true
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 165,
//        "y": 247
//        },
//        {
//        "x": 251,
//        "y": 247
//        },
//        {
//        "x": 251,
//        "y": 274
//        },
//        {
//        "x": 165,
//        "y": 274
//        }
//        ]
//        },
//        "inferText": "주문완료",
//        "inferConfidence": 0.9999,
//        "type": "NORMAL",
//        "lineBreak": true
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 167,
//        "y": 348
//        },
//        {
//        "x": 274,
//        "y": 348
//        },
//        {
//        "x": 274,
//        "y": 378
//        },
//        {
//        "x": 167,
//        "y": 378
//        }
//        ]
//        },
//        "inferText": "[KF365]",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 275,
//        "y": 346
//        },
//        {
//        "x": 329,
//        "y": 346
//        },
//        {
//        "x": 329,
//        "y": 378
//        },
//        {
//        "x": 275,
//        "y": 378
//        }
//        ]
//        },
//        "inferText": "한돈",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 329,
//        "y": 346
//        },
//        {
//        "x": 408,
//        "y": 346
//        },
//        {
//        "x": 408,
//        "y": 379
//        },
//        {
//        "x": 329,
//        "y": 379
//        }
//        ]
//        },
//        "inferText": "삼겹살",
//        "inferConfidence": 0.9997,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 408,
//        "y": 346
//        },
//        {
//        "x": 487,
//        "y": 346
//        },
//        {
//        "x": 487,
//        "y": 379
//        },
//        {
//        "x": 408,
//        "y": 379
//        }
//        ]
//        },
//        "inferText": "구이용",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 487,
//        "y": 348
//        },
//        {
//        "x": 563,
//        "y": 348
//        },
//        {
//        "x": 563,
//        "y": 382
//        },
//        {
//        "x": 487,
//        "y": 382
//        }
//        ]
//        },
//        "inferText": "600g",
//        "inferConfidence": 0.9996,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 562,
//        "y": 346
//        },
//        {
//        "x": 635,
//        "y": 346
//        },
//        {
//        "x": 635,
//        "y": 381
//        },
//        {
//        "x": 562,
//        "y": 381
//        }
//        ]
//        },
//        "inferText": "(냉장)",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": true
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 168,
//        "y": 388
//        },
//        {
//        "x": 260,
//        "y": 388
//        },
//        {
//        "x": 260,
//        "y": 410
//        },
//        {
//        "x": 168,
//        "y": 410
//        }
//        ]
//        },
//        "inferText": "[KF365]",
//        "inferConfidence": 0.9983,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 262,
//        "y": 386
//        },
//        {
//        "x": 309,
//        "y": 386
//        },
//        {
//        "x": 309,
//        "y": 413
//        },
//        {
//        "x": 262,
//        "y": 413
//        }
//        ]
//        },
//        "inferText": "한돈",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 309,
//        "y": 386
//        },
//        {
//        "x": 377,
//        "y": 386
//        },
//        {
//        "x": 377,
//        "y": 413
//        },
//        {
//        "x": 309,
//        "y": 413
//        }
//        ]
//        },
//        "inferText": "삼겹살",
//        "inferConfidence": 0.9995,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 377,
//        "y": 384
//        },
//        {
//        "x": 444,
//        "y": 384
//        },
//        {
//        "x": 444,
//        "y": 413
//        },
//        {
//        "x": 377,
//        "y": 413
//        }
//        ]
//        },
//        "inferText": "구이용",
//        "inferConfidence": 0.9999,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 445,
//        "y": 388
//        },
//        {
//        "x": 523,
//        "y": 388
//        },
//        {
//        "x": 523,
//        "y": 414
//        },
//        {
//        "x": 445,
//        "y": 414
//        }
//        ]
//        },
//        "inferText": "600g~",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 525,
//        "y": 386
//        },
//        {
//        "x": 588,
//        "y": 386
//        },
//        {
//        "x": 588,
//        "y": 414
//        },
//        {
//        "x": 525,
//        "y": 414
//        }
//        ]
//        },
//        "inferText": "(냉장)",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": true
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 46,
//        "y": 446
//        },
//        {
//        "x": 66,
//        "y": 446
//        },
//        {
//        "x": 66,
//        "y": 455
//        },
//        {
//        "x": 46,
//        "y": 455
//        }
//        ]
//        },
//        "inferText": "최저가",
//        "inferConfidence": 0.8717,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 166,
//        "y": 426
//        },
//        {
//        "x": 287,
//        "y": 426
//        },
//        {
//        "x": 287,
//        "y": 455
//        },
//        {
//        "x": 166,
//        "y": 455
//        }
//        ]
//        },
//        "inferText": "15,540원",
//        "inferConfidence": 0.9999,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 307,
//        "y": 426
//        },
//        {
//        "x": 351,
//        "y": 426
//        },
//        {
//        "x": 351,
//        "y": 456
//        },
//        {
//        "x": 307,
//        "y": 456
//        }
//        ]
//        },
//        "inferText": "1개",
//        "inferConfidence": 0.9997,
//        "type": "NORMAL",
//        "lineBreak": true
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 49,
//        "y": 454
//        },
//        {
//        "x": 62,
//        "y": 454
//        },
//        {
//        "x": 62,
//        "y": 462
//        },
//        {
//        "x": 49,
//        "y": 462
//        }
//        ]
//        },
//        "inferText": "도전",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": true
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 166,
//        "y": 483
//        },
//        {
//        "x": 251,
//        "y": 483
//        },
//        {
//        "x": 251,
//        "y": 510
//        },
//        {
//        "x": 166,
//        "y": 510
//        }
//        ]
//        },
//        "inferText": "주문완료",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": true
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 168,
//        "y": 588
//        },
//        {
//        "x": 274,
//        "y": 588
//        },
//        {
//        "x": 274,
//        "y": 613
//        },
//        {
//        "x": 168,
//        "y": 613
//        }
//        ]
//        },
//        "inferText": "[KF365]",
//        "inferConfidence": 0.9999,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 276,
//        "y": 585
//        },
//        {
//        "x": 355,
//        "y": 585
//        },
//        {
//        "x": 355,
//        "y": 613
//        },
//        {
//        "x": 276,
//        "y": 613
//        }
//        ]
//        },
//        "inferText": "DOLE",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 354,
//        "y": 582
//        },
//        {
//        "x": 409,
//        "y": 582
//        },
//        {
//        "x": 409,
//        "y": 616
//        },
//        {
//        "x": 354,
//        "y": 616
//        }
//        ]
//        },
//        "inferText": "실속",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 409,
//        "y": 582
//        },
//        {
//        "x": 489,
//        "y": 582
//        },
//        {
//        "x": 489,
//        "y": 616
//        },
//        {
//        "x": 409,
//        "y": 616
//        }
//        ]
//        },
//        "inferText": "바나나",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 489,
//        "y": 586
//        },
//        {
//        "x": 536,
//        "y": 586
//        },
//        {
//        "x": 536,
//        "y": 616
//        },
//        {
//        "x": 489,
//        "y": 616
//        }
//        ]
//        },
//        "inferText": "1kg",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 539,
//        "y": 582
//        },
//        {
//        "x": 635,
//        "y": 582
//        },
//        {
//        "x": 635,
//        "y": 617
//        },
//        {
//        "x": 539,
//        "y": 617
//        }
//        ]
//        },
//        "inferText": "(필리핀)",
//        "inferConfidence": 0.9999,
//        "type": "NORMAL",
//        "lineBreak": true
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 165,
//        "y": 629
//        },
//        {
//        "x": 272,
//        "y": 627
//        },
//        {
//        "x": 273,
//        "y": 656
//        },
//        {
//        "x": 166,
//        "y": 658
//        }
//        ]
//        },
//        "inferText": "3,306원",
//        "inferConfidence": 0.9995,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 274,
//        "y": 627
//        },
//        {
//        "x": 381,
//        "y": 627
//        },
//        {
//        "x": 381,
//        "y": 656
//        },
//        {
//        "x": 274,
//        "y": 656
//        }
//        ]
//        },
//        "inferText": "3,890원",
//        "inferConfidence": 0.9997,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 397,
//        "y": 627
//        },
//        {
//        "x": 441,
//        "y": 627
//        },
//        {
//        "x": 441,
//        "y": 658
//        },
//        {
//        "x": 397,
//        "y": 658
//        }
//        ]
//        },
//        "inferText": "1개",
//        "inferConfidence": 0.9998,
//        "type": "NORMAL",
//        "lineBreak": true
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 46,
//        "y": 683
//        },
//        {
//        "x": 67,
//        "y": 683
//        },
//        {
//        "x": 67,
//        "y": 693
//        },
//        {
//        "x": 46,
//        "y": 693
//        }
//        ]
//        },
//        "inferText": "죄지가",
//        "inferConfidence": 0.6824,
//        "type": "NORMAL",
//        "lineBreak": true
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 49,
//        "y": 690
//        },
//        {
//        "x": 63,
//        "y": 690
//        },
//        {
//        "x": 63,
//        "y": 698
//        },
//        {
//        "x": 49,
//        "y": 698
//        }
//        ]
//        },
//        "inferText": "도전",
//        "inferConfidence": 0.9995,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 166,
//        "y": 687
//        },
//        {
//        "x": 251,
//        "y": 687
//        },
//        {
//        "x": 251,
//        "y": 712
//        },
//        {
//        "x": 166,
//        "y": 712
//        }
//        ]
//        },
//        "inferText": "주문완료",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": true
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 167,
//        "y": 786
//        },
//        {
//        "x": 275,
//        "y": 786
//        },
//        {
//        "x": 275,
//        "y": 818
//        },
//        {
//        "x": 167,
//        "y": 818
//        }
//        ]
//        },
//        "inferText": "[착한마을",
//        "inferConfidence": 0.9989,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 276,
//        "y": 784
//        },
//        {
//        "x": 388,
//        "y": 784
//        },
//        {
//        "x": 388,
//        "y": 819
//        },
//        {
//        "x": 276,
//        "y": 819
//        }
//        ]
//        },
//        "inferText": "마음이가]",
//        "inferConfidence": 0.9987,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 387,
//        "y": 784
//        },
//        {
//        "x": 442,
//        "y": 784
//        },
//        {
//        "x": 442,
//        "y": 816
//        },
//        {
//        "x": 387,
//        "y": 816
//        }
//        ]
//        },
//        "inferText": "호박",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 444,
//        "y": 783
//        },
//        {
//        "x": 521,
//        "y": 783
//        },
//        {
//        "x": 521,
//        "y": 818
//        },
//        {
//        "x": 444,
//        "y": 818
//        }
//        ]
//        },
//        "inferText": "영양떡",
//        "inferConfidence": 0.9999,
//        "type": "NORMAL",
//        "lineBreak": true
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 165,
//        "y": 830
//        },
//        {
//        "x": 274,
//        "y": 828
//        },
//        {
//        "x": 274,
//        "y": 857
//        },
//        {
//        "x": 166,
//        "y": 860
//        }
//        ]
//        },
//        "inferText": "5,500원",
//        "inferConfidence": 0.9999,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 293,
//        "y": 828
//        },
//        {
//        "x": 337,
//        "y": 828
//        },
//        {
//        "x": 337,
//        "y": 860
//        },
//        {
//        "x": 293,
//        "y": 860
//        }
//        ]
//        },
//        "inferText": "1개",
//        "inferConfidence": 0.995,
//        "type": "NORMAL",
//        "lineBreak": true
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 165,
//        "y": 886
//        },
//        {
//        "x": 251,
//        "y": 886
//        },
//        {
//        "x": 251,
//        "y": 913
//        },
//        {
//        "x": 165,
//        "y": 913
//        }
//        ]
//        },
//        "inferText": "주문완료",
//        "inferConfidence": 0.9999,
//        "type": "NORMAL",
//        "lineBreak": true
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 166,
//        "y": 987
//        },
//        {
//        "x": 240,
//        "y": 987
//        },
//        {
//        "x": 240,
//        "y": 1020
//        },
//        {
//        "x": 166,
//        "y": 1020
//        }
//        ]
//        },
//        "inferText": "[하림]",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 238,
//        "y": 985
//        },
//        {
//        "x": 341,
//        "y": 985
//        },
//        {
//        "x": 341,
//        "y": 1018
//        },
//        {
//        "x": 238,
//        "y": 1018
//        }
//        ]
//        },
//        "inferText": "치킨너겟",
//        "inferConfidence": 0.9999,
//        "type": "NORMAL",
//        "lineBreak": true
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 166,
//        "y": 1030
//        },
//        {
//        "x": 285,
//        "y": 1030
//        },
//        {
//        "x": 285,
//        "y": 1059
//        },
//        {
//        "x": 166,
//        "y": 1059
//        }
//        ]
//        },
//        "inferText": "10,700원",
//        "inferConfidence": 0.9998,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 306,
//        "y": 1030
//        },
//        {
//        "x": 348,
//        "y": 1030
//        },
//        {
//        "x": 348,
//        "y": 1061
//        },
//        {
//        "x": 306,
//        "y": 1061
//        }
//        ]
//        },
//        "inferText": "1개",
//        "inferConfidence": 0.9844,
//        "type": "NORMAL",
//        "lineBreak": true
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 68,
//        "y": 1040
//        },
//        {
//        "x": 87,
//        "y": 1040
//        },
//        {
//        "x": 87,
//        "y": 1077
//        },
//        {
//        "x": 68,
//        "y": 1077
//        }
//        ]
//        },
//        "inferText": "中美",
//        "inferConfidence": 0.3792,
//        "type": "NORMAL",
//        "lineBreak": true
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 166,
//        "y": 1088
//        },
//        {
//        "x": 251,
//        "y": 1088
//        },
//        {
//        "x": 251,
//        "y": 1115
//        },
//        {
//        "x": 166,
//        "y": 1115
//        }
//        ]
//        },
//        "inferText": "주문완료",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": true
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 165,
//        "y": 1187
//        },
//        {
//        "x": 285,
//        "y": 1187
//        },
//        {
//        "x": 285,
//        "y": 1221
//        },
//        {
//        "x": 165,
//        "y": 1221
//        }
//        ]
//        },
//        "inferText": "[연안식당]",
//        "inferConfidence": 0.9999,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 285,
//        "y": 1187
//        },
//        {
//        "x": 341,
//        "y": 1187
//        },
//        {
//        "x": 341,
//        "y": 1220
//        },
//        {
//        "x": 285,
//        "y": 1220
//        }
//        ]
//        },
//        "inferText": "부추",
//        "inferConfidence": 0.9999,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 341,
//        "y": 1187
//        },
//        {
//        "x": 396,
//        "y": 1187
//        },
//        {
//        "x": 396,
//        "y": 1220
//        },
//        {
//        "x": 341,
//        "y": 1220
//        }
//        ]
//        },
//        "inferText": "꼬막",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 396,
//        "y": 1187
//        },
//        {
//        "x": 474,
//        "y": 1187
//        },
//        {
//        "x": 474,
//        "y": 1220
//        },
//        {
//        "x": 396,
//        "y": 1220
//        }
//        ]
//        },
//        "inferText": "비빔장",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": true
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 166,
//        "y": 1232
//        },
//        {
//        "x": 274,
//        "y": 1232
//        },
//        {
//        "x": 274,
//        "y": 1261
//        },
//        {
//        "x": 166,
//        "y": 1261
//        }
//        ]
//        },
//        "inferText": "6,900원",
//        "inferConfidence": 0.9995,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 293,
//        "y": 1232
//        },
//        {
//        "x": 337,
//        "y": 1232
//        },
//        {
//        "x": 337,
//        "y": 1263
//        },
//        {
//        "x": 293,
//        "y": 1263
//        }
//        ]
//        },
//        "inferText": "1개",
//        "inferConfidence": 0.9999,
//        "type": "NORMAL",
//        "lineBreak": true
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 166,
//        "y": 1290
//        },
//        {
//        "x": 252,
//        "y": 1290
//        },
//        {
//        "x": 252,
//        "y": 1317
//        },
//        {
//        "x": 166,
//        "y": 1317
//        }
//        ]
//        },
//        "inferText": "주문완료",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": true
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 167,
//        "y": 1390
//        },
//        {
//        "x": 311,
//        "y": 1390
//        },
//        {
//        "x": 311,
//        "y": 1422
//        },
//        {
//        "x": 167,
//        "y": 1422
//        }
//        ]
//        },
//        "inferText": "[매일식품관]",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 309,
//        "y": 1389
//        },
//        {
//        "x": 388,
//        "y": 1389
//        },
//        {
//        "x": 388,
//        "y": 1422
//        },
//        {
//        "x": 309,
//        "y": 1422
//        }
//        ]
//        },
//        "inferText": "복분자",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 388,
//        "y": 1389
//        },
//        {
//        "x": 444,
//        "y": 1389
//        },
//        {
//        "x": 444,
//        "y": 1422
//        },
//        {
//        "x": 388,
//        "y": 1422
//        }
//        ]
//        },
//        "inferText": "간장",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 442,
//        "y": 1389
//        },
//        {
//        "x": 545,
//        "y": 1389
//        },
//        {
//        "x": 545,
//        "y": 1422
//        },
//        {
//        "x": 442,
//        "y": 1422
//        }
//        ]
//        },
//        "inferText": "깐새우장",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": false
//        },
//        {
//        "valueType": "ALL",
//        "boundingPoly": {
//        "vertices": [
//        {
//        "x": 546,
//        "y": 1391
//        },
//        {
//        "x": 621,
//        "y": 1391
//        },
//        {
//        "x": 621,
//        "y": 1423
//        },
//        {
//        "x": 546,
//        "y": 1423
//        }
//        ]
//        },
//        "inferText": "300g",
//        "inferConfidence": 1,
//        "type": "NORMAL",
//        "lineBreak": true
//        }
//        ]
//        }
//        ]
//        }