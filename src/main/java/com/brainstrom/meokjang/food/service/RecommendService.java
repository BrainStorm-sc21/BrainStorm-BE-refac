package com.brainstrom.meokjang.food.service;

import com.brainstrom.meokjang.food.domain.FoodInfo;
import com.brainstrom.meokjang.food.dto.OcrFoodDto;
import com.brainstrom.meokjang.food.dto.request.OcrRequest;
import com.brainstrom.meokjang.food.dto.response.OcrResponse;
import com.brainstrom.meokjang.food.repository.FoodInfoRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Component
public class RecommendService {

    private final FoodInfoRepository foodInfoRepository;
    private final String document_url;
    private final String general_url;
    private final String document_secretKey;
    private final String general_secretKey;
    private String url;
    private String secretKey;

    @Autowired
    public RecommendService(FoodInfoRepository foodInfoRepository,
                            @Value("${GENERAL_URL}") String general_url,
                            @Value("${GENERAL_SECRET_KEY}") String general_secretKey,
                            @Value("${DOCUMENT_URL}") String document_url,
                            @Value("${DOCUMENT_SECRET_KEY}") String document_secretKey) {
        this.foodInfoRepository = foodInfoRepository;
        this.general_url = general_url;
        this.general_secretKey = general_secretKey;
        this.document_url = document_url;
        this.document_secretKey = document_secretKey;
    }

    public Map<Integer, OcrFoodDto> doOcr(OcrRequest ocrRequest) throws IOException {
        if (ocrRequest.getType().equals("document")) {
            url = document_url;
            secretKey = document_secretKey;
        } else {
            url = general_url;
            secretKey = general_secretKey;
        }

        URL url = new URL(this.url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setUseCaches(false);
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        con.setRequestProperty("X-OCR-SECRET", secretKey);

        // API 요청 바디 정보 (이미지 파일)
        InputStream inputStream = ocrRequest.getImage().getResource().getInputStream();
        File imageFile = File.createTempFile("temp", ocrRequest.getImage().getOriginalFilename());
        try {
            Files.copy(inputStream, imageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            inputStream.close();
        }

        byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
        String base64EncodedImage = Base64.getEncoder().encodeToString(imageBytes);
        JSONObject requestJson = new JSONObject();
        requestJson.put("version", "V2");
        requestJson.put("requestId", UUID.randomUUID().toString());
        requestJson.put("timestamp", System.currentTimeMillis());
        requestJson.put("images", new JSONArray().put(new JSONObject().put("format", "png").put("name", "test").put("data", base64EncodedImage)));

        String postParams = requestJson.toString();

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(postParams);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        BufferedReader br;

        if (responseCode == 200) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } else {
            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
        }

        String inputLine;
        StringBuffer responseStream = new StringBuffer();
        while ((inputLine = br.readLine()) != null) {
            responseStream.append(inputLine);
        }
        br.close();
        String response = responseStream.toString();

        if (responseCode == 200) {
            System.out.println("API 호출 성공: " + response);
        } else{
            System.out.println("API 호출 실패: " + response);
            return null;
        }

        Map<Integer, OcrFoodDto> ocrResult = new HashMap<>();
        if (ocrRequest.getType().equals("document")) {
            ocrResult = documentToList(response, ocrResult);
        } else {
            ocrResult = generalToList(response, ocrResult);
        }
        if (ocrResult.size() == 0) {
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

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length(); i++) {
            String text = fields.getJSONObject(i).getString("inferText");
            Boolean lineSwitch = fields.getJSONObject(i).getBoolean("lineBreak");
            if (text.equals("주문완료") || text.equals("배송완료") || text.equals("후기작성") || text.equals("배송중") || text.equals("배송준비중") || text.equals("배송준비") ) {
                continue;
            }
            if (text.equals("주문취소")){
                count --;
                ocrResult.remove(count);
                continue;
            }
            if (lineCount == 0) {
                sb.append(text+" ");
                if (lineSwitch) {
                    foodName = sb.toString();
                    foodName = foodName.substring(0, foodName.length()-1);
                    sb = new StringBuilder();
                    lineCount++;
                }
                continue;
            } else if (lineCount == 1) {
                readCount++;
                if (lineSwitch) {
                    char[] result = text.toCharArray();
                    if (result[result.length-1] == '개' && Character.isDigit(result[0])){
                        foodCount = Character.getNumericValue(result[0]);
                    }
                    else {
                        lineCount = 0;
                        i -= (readCount-1);
                        readCount = 0;
                        continue;
                    }
                    lineCount = 0;
                }
                else{
                    continue;
                }
            }
            ocrResult.put(count, new OcrFoodDto(foodName, foodCount));
            count++;
        }
        return ocrResult;
    }

    public OcrResponse recommend(Map<Integer, OcrFoodDto> ocrResult) {
        try {
            Map<Integer, Map<Integer, Integer>> recommend = new HashMap<>();
            for (Map.Entry<Integer, OcrFoodDto> ocrFood : ocrResult.entrySet()) {
                Integer idx = ocrFood.getKey();
                List<String> foodName = Arrays.asList(ocrFood.getValue().getFoodName().split(" "));
                Collections.reverse(foodName);
                List<FoodInfo> foodInfos = new ArrayList<>();
                for (String name : foodName) {
                    foodInfos.addAll(foodInfoRepository.findByInfoName(name));
                    if (foodInfos.size() != 0) {
                        break;
                    }
                }
                Map<Integer, Integer> foodInfo = new HashMap<>();
                if (foodInfos.size() == 0)
                    continue;
                for (FoodInfo info : foodInfos) {
                    int storageWay;
                    if (info.getStorageWay().equals("냉장")) {
                        storageWay = 0;
                    } else if (info.getStorageWay().equals("냉동")) {
                        storageWay = 1;
                    } else  {
                        storageWay = 2;
                    }
                    foodInfo.put(storageWay, info.getStorageDay());
                }
                for (int i = 0; i < 3; i++) {
                    if (!foodInfo.containsKey(i)) {
                        foodInfo.put(i, 0);
                    }
                }
                recommend.put(idx, foodInfo);
            }
            if (recommend.size() == 0) {
                recommend = null;
            }
            return new OcrResponse(ocrResult, recommend);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
