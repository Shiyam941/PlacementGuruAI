package com.placementguru.repository;

import com.placementguru.model.CodingProblem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodingProblemRepository extends MongoRepository<CodingProblem, String> {
    
    List<CodingProblem> findByDifficulty(String difficulty);
    
    List<CodingProblem> findByCategory(String category);
    
    List<CodingProblem> findByDifficultyAndCategory(String difficulty, String category);
}
