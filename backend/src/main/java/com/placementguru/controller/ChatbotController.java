package com.placementguru.controller;

import com.placementguru.dto.ApiResponse;
import com.placementguru.dto.ChatRequest;
import com.placementguru.dto.ChatResponse;
import com.placementguru.model.ChatHistory;
import com.placementguru.model.User;
import com.placementguru.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    @Autowired
    private ChatbotService chatbotService;

    @PostMapping("/message")
    public ResponseEntity<?> sendMessage(@AuthenticationPrincipal User user,
                                        @RequestBody ChatRequest request) {
        try {
            ChatResponse response = chatbotService.sendMessage(user, request);
            return ResponseEntity.ok(new ApiResponse(true, "Response generated", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("/history")
    public ResponseEntity<?> getChatHistory(@AuthenticationPrincipal User user) {
        try {
            List<ChatHistory> history = chatbotService.getChatHistory(user);
            return ResponseEntity.ok(new ApiResponse(true, "History retrieved", history));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/history")
    public ResponseEntity<?> clearHistory(@AuthenticationPrincipal User user) {
        try {
            chatbotService.clearHistory(user);
            return ResponseEntity.ok(new ApiResponse(true, "History cleared"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, e.getMessage()));
        }
    }
}
