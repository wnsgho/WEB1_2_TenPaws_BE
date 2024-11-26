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

@Service
@RequiredArgsConstructor
@Transactional
public class ApiService {

    @Value("${openai.api.key}")
    private String API_KEY;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);

    private final OkHttpClient client = new OkHttpClient();

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public String getRecommendation(String prompt) throws IOException {
        // 요청 데이터 생성
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

            // 응답 JSON 반환 / 글자로 변환해주던 chatResponse 삭제
            String responseBody = response.body().string();
            logger.info("Received JSON response: {}", responseBody);
            return responseBody;
        }
    }

    @Getter
    @Setter
    public static class ChatRequest {
        private String model = "gpt-3.5-turbo";
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
}
