package com.brainstrom.meokjang.food.service;

import com.brainstrom.meokjang.dto.OcrList;
import com.brainstrom.meokjang.food.dto.request.OcrRequest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class OcrService {

    public OcrList doOcr(OcrRequest ocrRequest) throws IOException {
        // API 요청 URL
        String url = "https://capi.clova.ai/v1/ocr";

        // API 요청 헤더 정보
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-OCR-SECRET", "{Your-Secret-Key}");

        // API 요청 바디 정보 (이미지 파일)
        File imageFile = ocrRequest.getImage().getResource().getFile();
        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
        HttpEntity<byte[]> requestBody = new HttpEntity<>(imageBytes, headers);

        // API 호출
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestBody, String.class);

        // API 응답 결과 파싱
        OcrList ocrList = null;
        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            if (ocrRequest.getType() == "document") {
                ocrList = documentToList(responseBody);
            } else {
                ocrList = generalToList(responseBody);
            }

        } else {
            System.out.println("API 호출 실패: " + response.getStatusCode());
        }
        return ocrList;
    }
    public OcrList documentToList(String responseBody){
        return null;
    }

//    {
//        result: {
//            storeInfo: {5 items},
//            paymentInfo: {4 items},
//            subResults: [
//            {
//                items: [
//                {
//                    name: {
//                        text: "씨그램 레몬350ml",
//                                formatted: {1 item},
//                        boundingBoxes: [2 items]
//                    },
//                    count: {
//                        text: "2",
//                                formatted: {1 item},
//                        boundingBoxes: [1 item]
//                    },
//                    priceInfo: {2 items}
//                },
//                {
//                    name: {
//                        text: "씨그램 라임350ml",
//                                formatted: {1 item},
//                        boundingBoxes: [2 items]
//                    },
//                    count: {
//                        text: "1",
//                                formatted: {1 item},
//                        boundingBoxes: [1 item]
//                    },
//                    priceInfo: {2 items}

    public OcrList generalToList(String responseBody){
        return null;
    }
}

//        meta: {3 items},
//        words: [
//        {
//        id: 1,
//        boundingBox: [4 items],
//        isVertical: false,
//        text: "TODAY'S",
//        confidence: 0.9837
//        },
//        {5 items},
//        ],
//        lines: [
//        {
//        id: 1,
//        wordIDs: [
//        1,
//        2
//        ],
//        boundingBox: [4 items]
//        },
//        {3 items},
//        ]
//        }