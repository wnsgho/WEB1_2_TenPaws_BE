package com.example.tenpaws.domain.recommendation.component;

import com.example.tenpaws.domain.recommendation.service.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChatApi {

    @Autowired
    private ApiService apiService;

    public String getRecommendation(String prompt) {
        try {
            String recommendation = apiService.getRecommendation(prompt);
            log.info("Recommended pet: {}", recommendation);
            return recommendation;
        } catch (Exception e) {
            log.error("Failed to get recommendation from OpenAI API", e);
            throw new RuntimeException("Failed to get recommendation", e);
        }
    }
}
