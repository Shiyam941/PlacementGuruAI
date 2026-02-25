package com.placementguru.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_history")
public class ChatHistory {
    
    @Id
    private String id;
    
    private String userId;
    
    private String userMessage;
    
    private String botResponse;
    
    private LocalDateTime timestamp;
    
    private String category; // GENERAL, TECHNICAL, HR, etc.
}
