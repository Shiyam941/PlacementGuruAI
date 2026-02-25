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
@Document(collection = "coding_problems")
public class CodingProblem {
    
    @Id
    private String id;
    
    private String title;
    
    private String description;
    
    private String difficulty; // EASY, MEDIUM, HARD
    
    private String category; // ARRAY, STRING, DP, GRAPH, etc.
    
    private List<String> tags;
    
    private List<TestCase> testCases = new ArrayList<>();
    
    private String sampleInput;
    
    private String sampleOutput;
    
    private String solutionTemplate; // Optional starter code
    
    private LocalDateTime createdAt;
    
    private String createdBy; // Admin ID
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestCase {
        private String input;
        private String expectedOutput;
        private boolean hidden = false; // Some test cases can be hidden from students
    }
}
