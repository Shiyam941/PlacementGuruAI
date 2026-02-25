package com.placementguru.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "resume_reports")
public class ResumeReport {
    
    @Id
    private String id;
    
    private String userId;
    
    private String fileName;
    
    private String extractedText;
    
    private Integer atsScore; // 0-100
    
    private List<String> strengths;
    
    private List<String> weaknesses;
    
    private List<String> suggestions;
    
    private List<String> detectedSkills;
    
    private LocalDateTime analyzedAt;
}
