package com.brainstrom.meokjang.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class OcrList {
    private Integer count;
    // OCR 결과를 담는 리스트
    private List<Map<String, Object>> ocrList;
}
