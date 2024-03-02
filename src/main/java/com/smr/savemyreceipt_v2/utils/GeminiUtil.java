package com.smr.savemyreceipt_v2.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.util.Base64;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@NoArgsConstructor
public class GeminiUtil {

    @Value("${spring.cloud.gcp.gemini.api-url}")
    private String API_URL;

    public ReceiptInfo sendReceipt(MultipartFile file) {
        try {
            byte[] fileContent = file.getBytes();
            String encodedString = Base64.getEncoder().encodeToString(fileContent);

            String requestBody = "{\n" +
                "  \"contents\": [\n" +
                "    {\n" +
                "      \"parts\": [\n" +
                "        {\n" +
                "          \"text\": \"Extract purchase_date, total_price from the image in Json schema. Purchase_date should be in yyyy-MM-dd format.\\n\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"inlineData\": {\n" +
                "            \"mimeType\": \"image/jpeg\",\n" +
                "            \"data\": \"" + encodedString + "\"\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ],\n" +
                "\"generationConfig\": {\n" +
                "    \"temperature\": 0.4,\n" +
                "    \"topK\": 32,\n" +
                "    \"topP\": 1,\n" +
                "    \"maxOutputTokens\": 4096,\n" +
                "    \"stopSequences\": []\n" +
                "  },\n" +
                "  \"safetySettings\": [\n" +
                "    {\n" +
                "      \"category\": \"HARM_CATEGORY_HARASSMENT\",\n" +
                "      \"threshold\": \"BLOCK_MEDIUM_AND_ABOVE\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"category\": \"HARM_CATEGORY_HATE_SPEECH\",\n" +
                "      \"threshold\": \"BLOCK_MEDIUM_AND_ABOVE\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"category\": \"HARM_CATEGORY_SEXUALLY_EXPLICIT\",\n" +
                "      \"threshold\": \"BLOCK_MEDIUM_AND_ABOVE\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"category\": \"HARM_CATEGORY_DANGEROUS_CONTENT\",\n" +
                "      \"threshold\": \"BLOCK_MEDIUM_AND_ABOVE\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

            // RestTemplate을 사용하여 API 요청
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);
            log.info(response.getBody());
            return jsonParse(response.getBody());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public ReceiptInfo jsonParse(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            GeminiResponse response = objectMapper.readValue(json, GeminiResponse.class);
            String text = response.getCandidates().get(0).getContent().getParts().get(0).getText();
            // 불필요한 문자열 제거 (백틱 및 json 마크다운 표시)
            String cleanedJson = text
                .replace("```json", "") // 시작 부분의 ```json 제거
                .replace("```", "") // 마지막 부분의 ``` 제거
                .trim(); // 앞뒤 공백 제거
            return objectMapper.readValue(cleanedJson, ReceiptInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
