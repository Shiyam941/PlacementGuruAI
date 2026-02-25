package com.placementguru.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterviewRequest {
    
    private String interviewType; // HR, TECHNICAL, PROJECT
    private String action; // START, ANSWER, END
    private String answer; // User's answer to current question
    private String sessionId; // For continuing session
}
