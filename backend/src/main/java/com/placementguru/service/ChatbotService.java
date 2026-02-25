package com.placementguru.service;

import com.placementguru.dto.ChatRequest;
import com.placementguru.dto.ChatResponse;
import com.placementguru.model.ChatHistory;
import com.placementguru.model.User;
import com.placementguru.repository.ChatHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatbotService {

    @Autowired
    private ChatHistoryRepository chatHistoryRepository;

    @Autowired
    private AIService aiService;

    public ChatResponse sendMessage(User user, ChatRequest request) {
        String botResponse = aiService.getChatbotResponse(request.getMessage());

        ChatHistory chatHistory = new ChatHistory();
        chatHistory.setUserId(user.getId());
        chatHistory.setUserMessage(request.getMessage());
        chatHistory.setBotResponse(botResponse);
        chatHistory.setCategory(request.getCategory() != null ? request.getCategory() : "GENERAL");
        chatHistory.setTimestamp(LocalDateTime.now());

        ChatHistory saved = chatHistoryRepository.save(chatHistory);

        return new ChatResponse(botResponse, saved.getId());
    }

    public List<ChatHistory> getChatHistory(User user) {
        return chatHistoryRepository.findTop20ByUserIdOrderByTimestampDesc(user.getId());
    }

    public void clearHistory(User user) {
        List<ChatHistory> userChats = chatHistoryRepository.findByUserIdOrderByTimestampDesc(user.getId());
        chatHistoryRepository.deleteAll(userChats);
    }
}
