package com.placementguru.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "interview_sessions")
public class InterviewSession {
    
    @Id
    private String id;
    
    private String userId;
    
    private String interviewType; // HR, TECHNICAL, PROJECT
    
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    private List<QnAPair> qnaPairs = new ArrayList<>();
    
    private String overallFeedback;
    
    private Integer score;
    
    private boolean completed = false;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QnAPair {
        private String question;
        private String answer;
        private String feedback;
        private Integer rating; // 1-5
        private LocalDateTime timestamp;
    }
}
