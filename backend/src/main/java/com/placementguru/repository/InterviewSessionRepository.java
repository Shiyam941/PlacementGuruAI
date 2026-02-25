package com.placementguru.repository;

import com.placementguru.model.InterviewSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewSessionRepository extends MongoRepository<InterviewSession, String> {
    
    List<InterviewSession> findByUserIdOrderByStartTimeDesc(String userId);
    
    Optional<InterviewSession> findByUserIdAndCompletedFalse(String userId);
    
    List<InterviewSession> findByUserIdAndInterviewType(String userId, String interviewType);
}
