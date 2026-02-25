package com.placementguru.repository;

import com.placementguru.model.Submission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends MongoRepository<Submission, String> {
    
    List<Submission> findByUserIdOrderBySubmittedAtDesc(String userId);
    
    List<Submission> findByProblemIdOrderBySubmittedAtDesc(String problemId);
    
    List<Submission> findByUserIdAndProblemIdOrderBySubmittedAtDesc(String userId, String problemId);
    
    long countByUserIdAndStatus(String userId, String status);
}
