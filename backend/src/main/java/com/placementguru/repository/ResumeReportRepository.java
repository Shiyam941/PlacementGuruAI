package com.placementguru.repository;

import com.placementguru.model.ResumeReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeReportRepository extends MongoRepository<ResumeReport, String> {
    
    List<ResumeReport> findByUserIdOrderByAnalyzedAtDesc(String userId);
}
