package com.placementguru.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {
    
    @Id
    private String id;
    
    private String name;
    
    @Indexed(unique = true)
    private String email;
    
    private String password;
    
    private Set<String> roles = new HashSet<>();
    
    private String skillLevel; // BEGINNER, INTERMEDIATE, ADVANCED
    
    private String targetCompany;
    
    private String preferredLanguage; // Java, Python, etc.
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private boolean active = true;
}
