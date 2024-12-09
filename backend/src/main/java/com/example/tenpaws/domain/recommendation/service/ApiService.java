package com.example.tenpaws.domain.recommendation.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
//@Transactional http 요청 보내므로, 트랜잭션 관리가 필요하지 않다.
public class ApiService {

    @Value("${openai.api.key}")
    private String API_KEY;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public String getRecommendation(String prompt) throws IOException {
        ChatRequest chatRequest = new ChatRequest(prompt);
        String json = objectMapper.writeValueAsString(chatRequest);
        logger.info("Sending request to OpenAI API: {}", json);

        Request request = new Request.Builder()
                .url(API_URL)
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                logger.error("OpenAI API call failed with status: {}", response.code());
                throw new IOException("Unexpected response: " + response);
            }

            String responseBody = response.body().string();
            logger.info("Received JSON response: {}", responseBody);

            // 응답 파싱
            Map<String, Object> jsonResponse = objectMapper.readValue(responseBody, Map.class);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) jsonResponse.get("choices");

            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                if (message != null) {
                    String content = (String) message.get("content");
                    logger.info("Parsed content: {}", content);
                    return content;
                }
            }

            throw new IOException("Unexpected response format");
        }
    }

    @Getter
    @Setter
    public static class ChatRequest {
        private String model = "gpt-4o-mini";
        private Message[] messages;

        public ChatRequest(String prompt) {
            this.messages = new Message[]{new Message("user", prompt)};
        }

        @Getter
        @Setter
        public static class Message {
            private String role;
            private String content;

            public Message(String role, String content) {
                this.role = role;
                this.content = content;
            }
        }
    }

//    @Getter
//    @Setter
//    public static class AiResponse {
//        private List<Choice> choices;
//
//        @Getter
//        @Setter
//        public static class Choice {
//            private Message message;
//        }
//
//        @Getter
//        @Setter
//        public static class Message {
//            private String content;
//        }
//    }

}