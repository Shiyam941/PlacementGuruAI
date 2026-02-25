package com.placementguru.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeAnalysisResponse {
    
    private String reportId;
    private Integer atsScore;
    private List<String> strengths;
    private List<String> weaknesses;
    private List<String> suggestions;
    private List<String> detectedSkills;
}
