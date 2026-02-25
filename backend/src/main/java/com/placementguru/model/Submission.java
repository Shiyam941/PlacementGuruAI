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
@Document(collection = "submissions")
public class Submission {
    
    @Id
    private String id;
    
    private String userId;
    
    private String problemId;
    
    private String code;
    
    private String language; // JAVA, PYTHON, CPP
    
    private String status; // ACCEPTED, WRONG_ANSWER, COMPILATION_ERROR, RUNTIME_ERROR
    
    private Integer testCasesPassed;
    
    private Integer totalTestCases;
    
    private List<TestResult> testResults;
    
    private Long executionTime; // milliseconds
    
    private String errorMessage;
    
    private LocalDateTime submittedAt;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestResult {
        private Integer testCaseNumber;
        private boolean passed;
        private String actualOutput;
        private String expectedOutput;
        private String error;
    }
}
