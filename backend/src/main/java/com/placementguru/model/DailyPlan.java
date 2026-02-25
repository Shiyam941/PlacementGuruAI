package com.placementguru.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "daily_plans")
public class DailyPlan {
    
    @Id
    private String id;
    
    private String userId;
    
    private LocalDate date;
    
    private String skillLevel;
    
    private List<Task> tasks = new ArrayList<>();
    
    private Integer totalTasks;
    
    private Integer completedTasks = 0;
    
    private String motivation;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Task {
        private String title;
        private String description;
        private String category; // CODING, APTITUDE, INTERVIEW_PREP, PROJECT
        private Integer estimatedMinutes;
        private boolean completed = false;
        private String completedAt;
    }
}
