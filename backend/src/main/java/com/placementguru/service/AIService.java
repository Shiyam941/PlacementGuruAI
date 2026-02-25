package com.placementguru.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AIService {

    @Value("${ai.api.key}")
    private String apiKey;

    @Value("${ai.api.url}")
    private String apiUrl;

    @Value("${ai.model}")
    private String model;

    private final WebClient webClient;
    private final Gson gson;

    public AIService() {
        this.webClient = WebClient.builder().build();
        this.gson = new Gson();
    }

    public String getAIResponse(String prompt) {
        try {
            // Validate API configuration
            if (apiKey == null || apiKey.isEmpty() || apiKey.equals("your-api-key-here")) {
                return "AI service not configured. Please add a valid API key in application.properties";
            }
            
            // Build OpenAI-compatible API request format (for SambaNova, OpenAI, etc.)
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("model", model);
            
            // Create messages array
            JsonArray messages = new JsonArray();
            JsonObject message = new JsonObject();
            message.addProperty("role", "user");
            message.addProperty("content", prompt);
            messages.add(message);
            
            requestBody.add("messages", messages);
            
            // Add parameters
            requestBody.addProperty("temperature", 0.7);
            requestBody.addProperty("max_tokens", 2000);
            requestBody.addProperty("top_p", 0.9);

            // Build full URL
            String fullUrl = apiUrl + "/chat/completions";
            
            System.out.println("Making AI API call to: " + fullUrl);
            System.out.println("Model: " + model);
            System.out.println("Request: " + requestBody.toString());
            
            String response = webClient.post()
                    .uri(fullUrl)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .bodyValue(requestBody.toString())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            System.out.println("AI API Response: " + response);

            // Parse OpenAI-compatible response format
            JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);
            
            // Check for errors in response
            if (jsonResponse.has("error")) {
                JsonObject error = jsonResponse.getAsJsonObject("error");
                String errorMsg = error.has("message") ? error.get("message").getAsString() : "Unknown error";
                System.err.println("AI API Error: " + errorMsg);
                return "AI service error: " + errorMsg;
            }
            
            // Extract text from choices array (OpenAI format)
            if (jsonResponse.has("choices") && jsonResponse.getAsJsonArray("choices").size() > 0) {
                JsonObject choice = jsonResponse.getAsJsonArray("choices")
                        .get(0).getAsJsonObject();
                
                if (choice.has("message")) {
                    JsonObject messageObj = choice.getAsJsonObject("message");
                    if (messageObj.has("content")) {
                        return messageObj.get("content").getAsString();
                    }
                }
            }
            
            return "No response content received from AI service.";

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("AI Service Exception: " + e.getMessage());
            e.printStackTrace(System.err);
            return "I'm having trouble connecting to the AI service right now. Please check your network connection.";
        }
    }

    public String getChatbotResponse(String userMessage) {
        String prompt = "You are PlacementGuru AI, a friendly assistant helping students with placement preparation. " +
                "Answer the following question: " + userMessage;
        return getAIResponse(prompt);
    }

    public String getInterviewQuestion(String interviewType, String previousContext) {
        String prompt = "You are conducting a " + interviewType + " interview for a software engineering position. ";
        if (previousContext != null && !previousContext.isEmpty()) {
            prompt += "Previous context: " + previousContext + ". ";
        }
        prompt += "Ask one relevant interview question. Only provide the question, nothing else.";
        return getAIResponse(prompt);
    }

    public String evaluateInterviewAnswer(String question, String answer) {
        String prompt = "As an interviewer, evaluate this answer:\n" +
                "Question: " + question + "\n" +
                "Answer: " + answer + "\n" +
                "Provide constructive feedback (2-3 sentences) and rate from 1-5.";
        return getAIResponse(prompt);
    }

    public String analyzeResume(String resumeText) {
        String prompt = "Analyze this resume and provide:\n" +
                "1. ATS Score (0-100)\n" +
                "2. Top 3 Strengths\n" +
                "3. Top 3 Weaknesses\n" +
                "4. 3 Improvement Suggestions\n" +
                "5. Detected Skills (comma separated)\n\n" +
                "Resume:\n" + resumeText.substring(0, Math.min(resumeText.length(), 3000));
        return getAIResponse(prompt);
    }

    public String generateDailyPlan(String skillLevel) {
        String prompt = "Create a daily study plan for a " + skillLevel + " level student preparing for placements. " +
                "Include 5-6 tasks covering coding, aptitude, and interview prep. " +
                "Format: Task Title | Description | Category | Estimated Minutes (separated by |). " +
                "Provide one motivational quote at the end.";
        return getAIResponse(prompt);
    }
}
